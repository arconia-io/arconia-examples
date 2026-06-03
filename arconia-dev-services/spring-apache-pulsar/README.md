# Spring Apache Pulsar with Arconia Dev Services

A Spring Boot sample application demonstrating zero-configuration Apache Pulsar messaging using [Arconia Dev Services](https://docs.arconia.io/arconia/latest/dev-services/pulsar/). This project shows how to produce and consume messages with [Spring for Apache Pulsar](https://spring.io/projects/spring-pulsar) — with no manual broker setup required.

## Pre-requisites

* Java 25
* Podman/Docker


## Running the Application

Run the application as follows:
```shell
./gradlew bootRun
```

Alternatively, you can use the [Arconia CLI](https://docs.arconia.io/arconia-cli/latest/index.html):
```shell
arconia dev
```

Under the hood, the Arconia framework will automatically spin up a [Apache Pulsar](https://docs.arconia.io/arconia/latest/dev-services/pulsar/) using Testcontainers (see [Arconia Dev Services](https://docs.arconia.io/arconia/latest/dev-services/) for more information).

The application will be accessible at http://localhost:8080/send
## Running Tests

Integration tests pick up the Dev Services automatically — no `@Import` or `@ServiceConnection` annotations needed:

Run the tests using the following command:

```shell
./gradlew test
```

Alternatively, you can use the [Arconia CLI](https://docs.arconia.io/arconia-cli/latest/index.html):

```shell
arconia test
```