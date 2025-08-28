#!/bin/bash
set -e  # 명령 실패 시 즉시 종료

# 입력 환경변수:
# - APP_HOST:   App EC2 Private IP (예: 10.0.2.177)
# - NGINX_HOST: Nginx EC2 Private IP (예: 10.0.1.18)

SSH_KEY=~/.ssh/hilingual_actions
SSH_HOST="ubuntu@${NGINX_HOST}"
SSH="ssh -i ${SSH_KEY} -o IdentitiesOnly=yes -o StrictHostKeyChecking=no ${SSH_HOST}"

######## 0) 현재 떠 있는 색 확인 ########
if  docker ps --format '{{.Names}}' | grep -q hilingual-blue ; then
  CURRENT="blue"
elif docker ps --format '{{.Names}}' | grep -q hilingual-green ; then
  CURRENT="green"
else
  CURRENT=""  # 첫 배포
fi

######## 새로 띄울 색/포트 결정 ########
if [ "$CURRENT" = "blue" ]; then
  NEW="green"; PORT_NEW=8082
  OLD="blue" ; PORT_OLD=8081
else
  NEW="blue" ; PORT_NEW=8081
  OLD="green"; PORT_OLD=8082
fi

echo "[INFO] CURRENT=$CURRENT NEW=$NEW PORT_NEW=$PORT_NEW"

./gradlew clean bootJar -x test

docker compose build spring-${NEW} --no-cache

######## 1) 새 컨테이너 기동 ########
docker compose up -d spring-${NEW}

######## 2) 헬스체크 (최대 100초) ########
SUCCESS=0
for i in {1..20}; do
  if curl -fs "http://localhost:${PORT_NEW}/actuator/health" 2>/dev/null | grep -q '"status":"UP"'; then
    SUCCESS=1
    break
  fi
  echo "  …health ${i}/20"
  sleep 5
done

if [ "$SUCCESS" -ne 1 ]; then
  echo "[ERROR] Health check failed on :${PORT_NEW}"
  ROLLBACK=1
fi

############################################
# Nginx 스위치 (TARGET_UPSTREAM만 사용)
# - TARGET: "APP_HOST:PORT" (예: 10.0.2.177:8082)
############################################
switch_upstream () {
  local TARGET="$1"  # 예: "10.0.2.177:8082"

  if [ -f "${SSH_KEY}" ]; then
    $SSH bash -lc "
      set -e
      cd /home/ubuntu/nginx

      C='sudo docker compose -p hilingual -f /home/ubuntu/nginx/docker-compose.yml'
      sudo systemctl start docker
      sudo systemctl enable docker >/dev/null 2>&1 || true
      \$C up -d --remove-orphans nginx

      # nginx 컨테이너 CID 조회
      i=0; cid=''
      while [ \$i -lt 30 ]; do
        cid=\$(sudo docker ps -q \
          -f 'label=com.docker.compose.project=hilingual' \
          -f 'label=com.docker.compose.service=nginx')
        [ -n \"\$cid\" ] && break
        i=\$((i+1)); sleep 1
      done
      [ -n \"\$cid\" ] || { echo 'nginx container not found'; exit 1; }

      # INC 디렉토리 보장
      sudo docker exec \"\$cid\" /bin/sh -lc 'mkdir -p /etc/nginx/includes'

      # (A) 환경 inc 파일 원자적 갱신
      if [ '${UPSTREAM_ENV}' = 'dev' ]; then
        sudo docker exec \"\$cid\" /bin/sh -lc \
          \"printf 'set \\\$service_url \\\"http://${TARGET}\\\";\\n' > /etc/nginx/includes/dev.inc.new && \
           mv /etc/nginx/includes/dev.inc.new /etc/nginx/includes/dev.inc\"
      elif [ '${UPSTREAM_ENV}' = 'prod' ]; then
        sudo docker exec \"\$cid\" /bin/sh -lc \
          \"printf 'set \\\$service_url \\\"http://${TARGET}\\\";\\n' > /etc/nginx/includes/prod.inc.new && \
           mv /etc/nginx/includes/prod.inc.new /etc/nginx/includes/prod.inc\"
      else
        echo '[WARN] UPSTREAM_ENV not set (dev|prod). inc 파일 갱신 생략'
      fi

      # (C) 문법 검사 성공 시에만 리로드
      sudo docker exec \"\$cid\" /bin/sh -lc 'nginx -t && nginx -s reload'
    "
  else
    echo "⚠️  ${SSH_KEY} 가 없어 Nginx 스위치를 건너뜁니다."
  fi
}
