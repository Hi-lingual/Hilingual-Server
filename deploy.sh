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
    echo "[ERROR] 서버 실행 실패. 롤백을 시작합니다..."

    docker compose stop spring-${AFTER_COLOR}
    docker compose up -d spring-${BEFORE_COLOR}

    # ✅ nginx만 이전 TARGET_UPSTREAM 값으로 재기동
    echo "[INFO] Nginx 롤백 환경변수 주입 중..."
    TARGET_UPSTREAM="hilingual-${BEFORE_COLOR}:8080" docker compose up -d --no-deps --force-recreate nginx

    echo "[INFO] 롤백 완료. 이전 서버(${BEFORE_COLOR})로 복구됨."
    exit 1
fi

echo "[INFO] Nginx 대상 변경: ${AFTER_COLOR}"

# ✅ nginx만 재기동 (ENV 직접 주입)
TARGET_UPSTREAM="hilingual-${AFTER_COLOR}:8080" docker compose up -d --no-deps --force-recreate nginx

echo "[INFO] 이전 서버 종료: $BEFORE_COLOR"
docker compose stop spring-${BEFORE_COLOR}
