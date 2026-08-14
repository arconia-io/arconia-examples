# Arconia Multitenancy - JDBC Tenant Details

Application that demonstrates the use of [Arconia Multitenancy](https://docs.arconia.io/arconia/latest/multitenancy/) in a Spring Boot applications where tenants are configured ina JDBC data source.

## Pre-requisites

* Java 25

## Running the application

Run the application as follows:

```shell
./gradlew bootRun
```

Alternatively, you can use the [Arconia CLI](https://arconia.io/docs/arconia-cli/latest/):

```shell
arconia dev
```

Under the hood, the Arconia framework will automatically spin up a [PostgreSQL](https://docs.arconia.io/arconia/latest/dev-services/postgresql/) database server using Testcontainers (see [Arconia Dev Services](https://docs.arconia.io/arconia/latest/dev-services/) for more information).

The application will be accessible at http://localhost:8080.

## Calling the application

> [!NOTE]
> These examples use the [httpie](https://httpie.io) CLI to send HTTP requests.

Call the application on behalf of the tenant _dukes_, which is one of the tenants configured in the application properties. You should get a response with the tenant name.

```shell
http :8080/tenant X-TenantId:dukes
```

Next, try with a tenant that is configured, but it's disabled. You should get a response with an error message stating that "The resolved tenant is invalid or disabled".

```shell
http :8080/tenant X-TenantId:pixie
```

Finally, try with a tenant that doesn't comply with the tenant identifier validation rules applied by default. You should get a response with an error message stating that "The tenant identifier must contain only alphanumeric characters, dashes (-), and underscores (_).".

```shell
http :8080/tenant X-TenantId:ban@na
```

You can list all the tenants configured in the application properties via the dedicated `/actuator/tenants` Actuator endpoint.

```shell
http :8080/actuator/tenants
```

The result will be something like this:

```json
{
    "tenants": [
        {
            "enabled": true,
            "identifier": "beans"
        },
        {
            "enabled": true,
            "identifier": "dukes"
        },
        {
            "enabled": false,
            "identifier": "pixie"
        }
    ]
}
```

You can get more information about a specific tenant by calling the `/actuator/tenants/{tenantId}` Actuator endpoint.

```shell
http :8080/actuator/tenants/pixie
```

The result will be something like this:

```json
{
    "attributeNames": [
        "status"
    ],
    "enabled": false,
    "identifier": "pixie"
}
```

Since the tenants are defined in a JDBC data source, it's important to monitor the health of the database. Arconia adds a dedicated health indicator for the tenants data source, which is considered as part of the overall application health. You can check the health of the application by calling the `/actuator/health` Actuator endpoint.

```shell
http :8080/actuator/health
```

In particular, you can check the health of the tenants data source by calling the `/actuator/health/jdbcTenantDetails` Actuator endpoint.

```shell
http :8080/actuator/health/jdbcTenantDetails
```

The result will be something like this:

```json
{
    "details": {
        "tenants": 3
    },
    "status": "UP"
}
```
