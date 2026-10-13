# Arconia Keycloak Dev Services with Spring Security OAuth2 Client

Application that demonstrates the use of [Arconia Dev Services](https://docs.arconia.io/arconia/latest/index.html) for [Keycloak](https://docs.arconia.io/arconia/latest/dev-services/keycloak/) with Spring Security OAuth2 Client. When running the application in dev mode or integration tests, a Keycloak server will be automatically started using Testcontainers, a realm with a client and users will be imported, and the OpenID Connect client registration will be automatically configured. As a developer, all you need to do is to add the `arconia-dev-services-keycloak` dependency to your project. Notice how the application contains no OAuth2 configuration at all.

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

The application will be accessible at http://localhost:8080.

## Using the application

Open http://localhost:8080 in your browser. Since every endpoint but one requires authentication, you'll be redirected to the Keycloak login page. Log in with one of the users of the realm bundled with the Dev Service:

| Username   | Password   | Realm Roles     |
|------------|------------|-----------------|
| `isabella` | `isabella` | `admin`, `user` |
| `bjorn`    | `bjorn`    | `user`          |

After logging in, the application shows the authenticated user:

```json
{
  "username": "isabella",
  "name": "Isabella Isabella",
  "email": "isabella@arconia.io",
  "realmRoles": [
    "admin",
    "user"
  ]
}
```

The Keycloak Admin Console is available at the URL printed in the application logs, where you can log in with username `arconia` and password `arconia`.

There's also a public endpoint showing which Keycloak the application resolved:

```shell
http :8080/issuer
```

## Sharing the Dev Service

The Keycloak Dev Service is shared by default, so this application and the [OAuth2 Resource Server example](../spring-security-oauth2-resource-server-keycloak) can use the same Keycloak server. Start this application first, then start the other one: instead of starting a second Keycloak, it will adopt this one, and its logs will report the Dev Service as shared from another application.

To confirm both applications resolved the same server, compare their issuers:

```shell
http :8080/issuer
http :8081/issuer
```

## Running the tests

You can run the tests using the following command:

```shell
./gradlew test
```

Alternatively, you can use the [Arconia CLI](https://docs.arconia.io/arconia-cli/latest/index.html):

```shell
arconia test
```

The integration tests will automatically get the OAuth2 client configuration from the Arconia Dev Services and run against the Keycloak server started by Testcontainers.
