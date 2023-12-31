#!\usr\bin\env pwsh

# Script assumes ElephantSQL (https://www.elephantsql.com) is available as service in cf marketplace
# Feel free to swap out the service for other PostgreSQL providers, like:
#   * Crunchy - https://docs.pivotal.io/partners/crunchy/using.html
#   * A9S - https://docs.pivotal.io/partners/a9s-postgresql/using.html
#   * Meta Azure Service Broker - https://github.com/Azure/meta-azure-service-broker/blob/master/docs/azure-postgresql-db.md
#   * AWS Service Broker - http://docs.pivotal.io/aws-services/creating.html#rds

param (
    [string]$Provider = "--with-user-provided-service"
)

$AppName=cf-archivist



switch ($Provider) {

	"--with-credhub" {
		cf push --no-start
		cf create-service credhub default $AppName-secrets -c config\secrets.json
        cf create-service elephantsql panda $AppName-backend
		while (cf service $AppName-secrets -notcontains "succeeded") {
			Write-Host "$APP_NAME-secrets is not ready yet..."
			Start-Sleep -Seconds 5
		}
		cf bind-service $AppName $AppName-secrets
		while (cf service $AppName-backend -notcontains "succeeded") {
			Write-Host "$APP_NAME-backend is not ready yet..."
			Start-Sleep -Seconds 5
		}
		cf start $AppName
	}

	"--with-user-provided-service" {
		cf push --no-start
		cf create-user-provided-service $AppName-secrets -p config\secrets.json
        cf create-service elephantsql panda $AppName-backend
		while (cf service $AppName-secrets -notcontains "succeeded") {
			Write-Host "$APP_NAME-secrets is not ready yet..."
			Start-Sleep -Seconds 5
		}
		cf bind-service $AppName $AppName-secrets
		while (cf service $AppName-backend -notcontains "succeeded") {
			Write-Host "$APP_NAME-backend is not ready yet..."
			Start-Sleep -Seconds 5
		}
		cf start $AppName
	}

}
