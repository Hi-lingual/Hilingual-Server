#!/bin/sh

set -e

# 기존 default.conf 제거
rm -f /etc/nginx/conf.d/default.conf

# envsubst로 템플릿 치환
envsubst '$TARGET_UPSTREAM' < /etc/nginx/nginx.template.conf > /etc/nginx/conf.d/default.conf

# Nginx 실행
nginx -g 'daemon off;'
