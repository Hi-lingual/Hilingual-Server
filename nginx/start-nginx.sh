#!/bin/sh
set -e
rm -f /etc/nginx/conf.d/default.conf                 # 기본 welcome conf 제거
# 템플릿의 $TARGET_UPSTREAM 값을 실제 IP:PORT 로 치환하여 conf 생성
envsubst '$TARGET_UPSTREAM' < /etc/nginx/nginx.template.conf \
  > /etc/nginx/conf.d/default.conf
nginx -g 'daemon off;'                               # foreground에 바로 실행
