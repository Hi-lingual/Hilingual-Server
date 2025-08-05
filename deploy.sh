#!/bin/bash
set -e                                 # 명령 오류 시 즉시 스크립트 종료하도록 설정

### 0) 현재 떠 있는 색깔 판별, app-blue 컨테이너가 up이면 다음 배포색은 green
if docker ps | grep -q hilingual-blue.*Up ; then
  NEW="green"; PORT_NEW=8082           # 이번에 올릴 색/포트
  OLD="blue";  PORT_OLD=8081           # 이전 버전 색/포트
  docker-compose up -d spring-green    # green 컨테이너만 백그라운드 기동
else
  NEW="blue";  PORT_NEW=8081
  OLD="green"; PORT_OLD=8082
  docker-compose up -d spring-blue
fi

### 1) 새 컨테이너 헬스체크
for i in {1..10}; do                   # 최대 10×5s = 50초 기다림
  curl -fs http://localhost:${PORT_NEW}/actuator/health | grep -q UP && break
  echo "  …${i}/10"                    # 아직 안 뜨면 진행상황 로그
  sleep 5
done
[ $i -eq 10 ] && { echo "Health FAIL"; ROLLBACK=1; }   # 10회 모두 실패 → 롤백 플래그

### 2) Nginx EC2 원격으로 upstream 전환 (또는 롤백)
SSH="ssh -i ~/.ssh/hilingual_actions -o StrictHostKeyChecking=no ubuntu@${NGINX_HOST}"

if [ -z "$ROLLBACK" ]; then
  echo "[NGINX] switch → ${NEW}"
  # TARGET_UPSTREAM 변수만 바꿔서 Nginx 컨테이너 1초 재기동 (무중단)
  $SSH "TARGET_UPSTREAM=${APP_HOST}:${PORT_NEW} \
        docker-compose -f ~/nginx/docker-compose.yml \
        up -d --no-deps --force-recreate nginx"
  docker-compose stop spring-${OLD}    # 이전 버전 자원 반환
else
  echo "[NGINX] rollback → ${OLD}"
  $SSH "TARGET_UPSTREAM=${APP_HOST}:${PORT_OLD} \
        docker-compose -f ~/nginx/docker-compose.yml \
        up -d --no-deps --force-recreate nginx"
  exit 1                               # GitHub Actions Job 을 실패로 마킹
fi
