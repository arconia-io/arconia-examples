# Arconia PostgreSQL Dev Services with Spring Data JPA

Application that demonstrates the use of [Arconia Dev Services](https://docs.arconia.io/arconia/latest/index.html) for [PostgreSQL](https://docs.arconia.io/arconia/latest/dev-services/postgresql/) with Spring Data JPA. When running the application in dev mode or integration tests, a PostgreSQL database will be automatically started using Testcontainers and the database connection details will be automatically configured. As a developer, all you need to do is to add the `arconia-dev-services-postgresql` dependency to your project. Everything else is taken care of by the Arconia framework, building on top of Spring Boot's support for Testcontainers.

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

Under the hood, the Arconia framework will automatically spin up a [PostgreSQL](https://docs.arconia.io/arconia/latest/dev-services/postgresql/) database server using Testcontainers (see [Arconia Dev Services](https://docs.arconia.io/arconia/latest/dev-services/) for more information).

The application will be accessible at http://localhost:8080.

## Using the application

Start by creating a book using the [httpie](https://httpie.io/) command line tool:

```shell
http POST :8080/books title="The Hitchhiker's Guide to the Galaxy"
```

You can then retrieve the list of books:

```shell
http GET :8080/books
```

You can also retrieve a specific book by its ID:

```shell
http GET :8080/books/1
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

The integration tests will automatically get the database connection details from the Arconia Dev Services and run against the PostgreSQL database started by Testcontainers.
