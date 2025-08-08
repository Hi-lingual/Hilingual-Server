#!/bin/sh
set -e
rm -f /etc/nginx/conf.d/default.conf
envsubst '$TARGET_UPSTREAM' < /etc/nginx/nginx.template.conf \
  > /etc/nginx/conf.d/default.conf
