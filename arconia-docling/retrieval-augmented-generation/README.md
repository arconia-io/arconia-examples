# Arconia Docling

Application that demonstrates the use of [Arconia Docling](https://arconia.io/docs/arconia/latest/integrations/docling) which provides a seamless Spring Boot integration with https://docling-project.github.io/docling[Docling], a powerful AI-powered document conversion service that transforms documents into structured formats like Markdown.

## Pre-requisites

* Java 25
* Podman/Docker

## Running the application

Run the application as follows:

```shell
./gradlew bootRun
```

Alternatively, you can use the [Arconia CLI](https://arconia.io/docs/arconia-cli/latest/):

```shell
arconia dev
```

Under the hood, the Arconia framework will automatically spin up a [Docling](https://arconia.io/docs/arconia/latest/dev-services/docling/) server using Testcontainers (see [Arconia Dev Services](https://arconia.io/docs/arconia/latest/dev-services/) for more information).

The application will be accessible at http://localhost:8080.

## Calling the application

The application provides a simple HTTP endpoint to convert a document into Markdown format. You can test it out using [HTTPie](https://httpie.io) and providing the URL to a document you want to convert:

```shell
http :8080/convert/http url=="https://docs.arconia.io/arconia-cli/latest/update/spring-boot/"
```

You can also convert documents from local files. The application includes a sample PDF document located at `src/main/resources/documents/story.pdf`. You can convert it as follows:

```shell
http :8080/convert/file
```

## Accessing Docling

The Docling Dev Service is configured to expose the Docling Serve UI on a specific port. The application logs will show you the URL where you can access that.

```logs
... Docling Serve UI: http://localhost:<port>/ui
```
