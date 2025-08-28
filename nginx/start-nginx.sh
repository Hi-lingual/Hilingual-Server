#!/bin/sh
set -e
nginx -t && nginx -s reload
