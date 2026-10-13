# Arconia Keycloak Dev Services with Spring Security OAuth2 Resource Server

Application that demonstrates the use of [Arconia Dev Services](https://docs.arconia.io/arconia/latest/index.html) for [Keycloak](https://docs.arconia.io/arconia/latest/dev-services/keycloak/) with Spring Security OAuth2 Resource Server. When running the application in dev mode or integration tests, a Keycloak server will be automatically started using Testcontainers, a realm with a client and users will be imported, and the JWT issuer will be automatically configured. As a developer, all you need to do is to add the `arconia-dev-services-keycloak` dependency to your project. Notice how the application contains no OAuth2 configuration at all.

## Pre-requisites

* Java 25
* Podman/Docker

## Running the application

Run the application as follows:

```shell
./gradlew bootRun
```

Alternatively, you can use the [Arconia CLI](https://docs.arconia.io/arconia-cli/latest/index.html):

```shell
arconia dev
```

Under the hood, the Arconia framework will automatically spin up a [Keycloak](https://docs.arconia.io/arconia/latest/dev-services/keycloak/) server using Testcontainers (see [Arconia Dev Services](https://docs.arconia.io/arconia/latest/dev-services/) for more information).

The application will be accessible at http://localhost:8081, a different port from the [OAuth2 Client example](../spring-security-oauth2-client-keycloak) so that both can run at the same time.

## Using the application

The `/messages` endpoint requires a valid access token:

```shell
http :8081/messages
```

```
HTTP/1.1 401
```

To obtain a token, first ask the application which Keycloak it resolved, since the server runs on a random port:

```shell
http :8081/issuer
```

```json
{
  "issuerUri": "http://localhost:42441/realms/arconia",
  "tokenEndpoint": "http://localhost:42441/realms/arconia/protocol/openid-connect/token"
}
```

Then request a token for one of the users of the realm bundled with the Dev Service, using the direct access grant:

| Username   | Password   | Realm Roles     |
|------------|------------|-----------------|
| `isabella` | `isabella` | `admin`, `user` |
| `bjorn`    | `bjorn`    | `user`          |

```shell
http --form POST http://localhost:42441/realms/arconia/protocol/openid-connect/token \
  grant_type=password username=isabella password=isabella \
  client_id=arconia-app client_secret=arconia-secret
```

Finally, call the endpoint with the `access_token` from the response:

```shell
http :8081/messages "Authorization:Bearer <access_token>"
```

```json
{
  "issuer": "http://localhost:42441/realms/arconia",
  "message": "Hello, isabella!",
  "realmRoles": [
    "admin",
    "user"
  ],
  "subject": "..."
}
```

The Keycloak Admin Console is available at the URL printed in the application logs, where you can log in with username `arconia` and password `arconia`.

## Sharing the Dev Service

The Keycloak Dev Service is shared by default, so this application and the [OAuth2 Client example](../spring-security-oauth2-client-keycloak) can use the same Keycloak server. Start either one first, then start the other: instead of starting a second Keycloak, it will adopt the running one, and its logs will report the Dev Service as shared from another application.

To confirm both applications resolved the same server, compare their issuers:

```shell
http :8080/issuer
http :8081/issuer
```

This is the recommended topology: one OAuth2 Client application together with any number of resource servers, all sharing a single Keycloak.

## Running the tests

You can run the tests using the following command:

```shell
./gradlew test
```

Alternatively, you can use the [Arconia CLI](https://docs.arconia.io/arconia-cli/latest/index.html):

```shell
arconia test
```

The integration tests will automatically get the JWT issuer from the Arconia Dev Services, request a token from the Keycloak server started by Testcontainers, and use it to call the protected endpoint.
