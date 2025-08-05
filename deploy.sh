#!/bin/bash
set -e                                  # 명령 실패 시 즉시 종료

## 테스트용 주석 변경! ##
######## 0) 지금 어떤 색 컨테이너가 떠 있는지 확인 ############
#  └─ 둘 다 없으면 “첫 배포” 라고 간주
if  docker ps --format '{{.Names}}' | grep -q hilingual-blue ; then
  CURRENT="blue"
elif docker ps --format '{{.Names}}' | grep -q hilingual-green ; then
  CURRENT="green"
else
  CURRENT=""                            # ← 첫 배포
fi

# 이번에 띄울 색·포트 결정
if [ "$CURRENT" = "blue" ]; then
  NEW="green"; PORT_NEW=8082
  OLD="blue" ; PORT_OLD=8081
else                                    # green 이거나 첫 배포
  NEW="blue" ; PORT_NEW=8081
  OLD="green"; PORT_OLD=8082
fi

######## 1) 새 컨테이너 기동 ##################################
docker compose up -d spring-${NEW}

######## 2) 헬스체크 (최대 100 초) ###############################
for i in {1..20}; do
  curl -fs http://localhost:${PORT_NEW}/actuator/health 2>/dev/null | grep -q '"status":"UP"' && break
  echo "  …${i}/20"
  sleep 5
done
[ $i -eq 20 ] && { echo "Health FAIL"; ROLLBACK=1; }

######## 3) Nginx EC2 upstream 전환(또는 롤백) #################
# 첫 배포 + ssh 키 미배치 상황을 고려해 “없으면 건너뜀”
SSH_KEY=~/.ssh/hilingual_actions
SSH_HOST="ubuntu@${NGINX_HOST}"
SSH="ssh -i ${SSH_KEY} -o StrictHostKeyChecking=no ${SSH_HOST}"

switch_upstream () {
  local TARGET=$1                      # ${APP_HOST}:${PORT}
  if [ -f "${SSH_KEY}" ] ; then        # 키가 있을 때만 실행
    $SSH "TARGET_UPSTREAM=${TARGET} \
          docker compose -f ~/nginx/docker-compose.yml \
          up -d --no-deps --force-recreate nginx"
  else
    echo "⚠️  ${SSH_KEY} 가 없어 Nginx 스위치를 건너뜁니다."
  fi
}

if [ -z "$ROLLBACK" ]; then
  echo "[NGINX] switch → ${NEW}"
  switch_upstream "${APP_HOST}:${PORT_NEW}"

  # 첫 배포가 아니면 이전 색 컨테이너 종료
  if [ -n "$CURRENT" ] ; then
    docker compose stop spring-${OLD}
  fi
else
  echo "[NGINX] rollback → ${OLD}"
  switch_upstream "${APP_HOST}:${PORT_OLD}"
  exit 1                                 # GitHub Actions job 을 실패로 종료
fi
