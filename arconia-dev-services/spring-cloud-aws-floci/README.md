# Arconia Floci Dev Services with Spring Cloud AWS

Application that demonstrates the use of [Arconia Dev Services](https://docs.arconia.io/arconia/latest/index.html) for [Floci](https://docs.arconia.io/arconia/latest/dev-services/floci/) with Spring Cloud AWS. When running the application in dev mode or integration tests, a Floci instance will be automatically started using Testcontainers and the AWS connection details will be automatically configured. As a developer, all you need to do is to add the `arconia-dev-services-floci` dependency to your project. Everything else is taken care of by the Arconia framework, building on top of Spring Boot's support for Testcontainers.

The application showcases three AWS services:

- **DynamoDB** — store and retrieve books;
- **SQS** — publish and consume events;
- **S3** — store and retrieve objects in a bucket.

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

Under the hood, the Arconia framework will automatically spin up a [Floci](https://docs.arconia.io/arconia/latest/dev-services/floci/) instance using Testcontainers (see [Arconia Dev Services](https://docs.arconia.io/arconia/latest/dev-services/) for more information).

The application will be accessible at http://localhost:8080.

## Using the application

### DynamoDB

Create a book using the [httpie](https://httpie.io/) command line tool:

```shell
http POST :8080/books title="The Golden Compass"
```

Retrieve all books:

```shell
http GET :8080/books
```

Retrieve a specific book by its ID:

```shell
http GET :8080/books/1
```

### SQS

Publish a dust reading event to the `dust-readings` queue:

```shell
http POST :8080/dust-readings event="dust.surge" location="svalbard" observer="lord_asriel"
```

The `DustReadingListener` will pick up the message and log it.

### S3

Store a scroll in the `magisterium-archives` bucket:

```shell
http POST :8080/scrolls name="panserbjorn-dust" content="Panserbjørne armor gathers Dust."
```

Retrieve a scroll by name:

```shell
http GET :8080/scrolls/panserbjorn-dust
```

List all scrolls in the bucket:

```shell
http GET :8080/scrolls
```

## Running the tests

Run the tests using the following command:

```shell
./gradlew test
```

Alternatively, you can use the [Arconia CLI](https://docs.arconia.io/arconia-cli/latest/index.html):

```shell
arconia test
```

The integration tests will automatically get the AWS connection details from the Arconia Dev Services and run against the Floci instance started by Testcontainers.
