#!/usr/bin/env bash

# Script assumes ElephantSQL (https://www.elephantsql.com) is available as service in cf marketplace
# Feel free to swap out the service for other PostgreSQL providers, like:
#   * Crunchy - https://docs.pivotal.io/partners/crunchy/using.html
#   * A9S - https://docs.pivotal.io/partners/a9s-postgresql/using.html
#   * Meta Azure Service Broker - https://github.com/Azure/meta-azure-service-broker/blob/master/docs/azure-postgresql-db.md
#   * AWS Service Broker - http://docs.pivotal.io/aws-services/creating.html#rds

set -e

export APP_NAME=cf-archivist


cf push --no-start
cf create-user-provided-service $APP_NAME-secrets -p config/secrets.json
cf create-service elephantsql panda $APP_NAME-backend
while [[ $(cf service $APP_NAME-secrets) != *"succeeded"* ]]; do
    echo "$APP_NAME-secrets is not ready yet..."
    sleep 5s
done
cf bind-service $APP_NAME $APP_NAME-secrets
while [[ $(cf service $APP_NAME-backend) != *"succeeded"* ]]; do
    echo "$APP_NAME-backend is not ready yet..."
    sleep 5s
done
cf bind-service $APP_NAME $APP_NAME-backend
cf start $APP_NAME
