# Arconia Multitenancy - Properties Tenant Details

Application that demonstrates the use of [Arconia Multitenancy](https://docs.arconia.io/arconia/latest/multitenancy/) in a Spring Boot applications where tenants are configured via properties.

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
