#!/usr/bin/env bash

set -x

export APP_NAME=cf-archivist
export REGISTRY_NAME=hooverRegistry

cf app ${APP_NAME} --guid

if [ $? -eq 0 ]; then
	cf stop $APP_NAME
	cf unbind-service $APP_NAME $REGISTRY_NAME
	cf delete $APP_NAME -r -f
else
    echo "$APP_NAME does not exist"
fi
