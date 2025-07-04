#!/bin/bash

EXIST_BLUE=$(docker ps | grep "hilingual-blue" | grep Up)

if [ -z "$EXIST_BLUE" ]; then
    docker compose up -d spring-blue
    BEFORE_COLOR="green"
    AFTER_COLOR="blue"
    AFTER_PORT=8080
else
    docker compose up -d spring-green
    BEFORE_COLOR="blue"
    AFTER_COLOR="green"
    AFTER_PORT=8081
fi

echo "[INFO] ${AFTER_COLOR} 서버 기동 완료, 헬스체크 중..."
for cnt in {1..10}; do
    UP=$(curl -s http://localhost:${AFTER_PORT}/actuator/health | grep UP)
    if [ -z "$UP" ]; then
        echo "[INFO] 응답 없음 (${cnt}/10), 재시도..."
        sleep 5
    else
        echo "[INFO] 서버 응답 확인됨."
        break
    fi
done

if [ $cnt -eq 10 ]; then
    echo "[ERROR] 서버 실행 실패. 롤백하세요."
    exit 1
fi

echo "[INFO] Nginx 대상 변경: ${AFTER_COLOR}"
echo "deployment_target=${AFTER_COLOR}" > ./nginx/.env

echo "[INFO] Nginx 컨테이너 재시작 중..."
docker compose up -d --force-recreate nginx

echo "[INFO] 이전 서버 종료: $BEFORE_COLOR"
docker compose stop spring-${BEFORE_COLOR}
