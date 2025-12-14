# Arconia Docling: Retrieval Augmented Generation

Application that demonstrates the use of [Docling Document Reader](https://arconia.io/docs/arconia/latest/generative-ai/docling-document-reader) which integrates with Spring AI for building a data ingestion pipeline to be used with Retrieval Augmented Generation (RAG) systems.

## Pre-requisites

* Java 25
* Podman/Docker

## Ollama

The application consumes models from an [Ollama](https://ollama.ai) inference server. You can either run Ollama locally on your laptop, or rely on the Testcontainers support in Spring Boot to spin up an Ollama service automatically. If you choose the first option, make sure you have Ollama installed and running on your laptop. Either way, Spring AI will take care of pulling the needed Ollama models when the application starts, if they are not available yet on your machine.

## Running the application

If you're using the native Ollama application, run the application as follows:

```shell
./gradlew bootRun
```

If instead you want to rely on the Ollama Dev Service via Testcontainers, run the application as follows.

```shell
./gradlew bootRun -Darconia.dev.services.ollama.enabled=true
```

Alternatively, you can use the [Arconia CLI](https://arconia.io/docs/arconia-cli/latest/):

```shell
arconia dev
```

Under the hood, the Arconia framework will automatically spin up a [Docling](https://arconia.io/docs/arconia/latest/dev-services/docling/) server and a [PostgreSQL](https://arconia.io/docs/arconia/latest/dev-services/postgresql/) database using Testcontainers (see [Arconia Dev Services](https://arconia.io/docs/arconia/latest/dev-services/) for more information).

The application will be accessible at http://localhost:8080.

## Calling the application

The application provides an HTTP endpoint for chatting with a large language model. You can ask questions about the document located at `src/main/resources/documents/story.pdf` which is ingested at application startup using Docling. You can test it out using [HTTPie](https://httpie.io).

```shell
http :8080/chat question=="What's Iorek's dream?"
```

You should get an answer similar to the following, which the chat model generates based on the relevant data retrieved from the vector store.

```shell
Iorek dreams of one day going on an adventure around the North Pole and seeing the Northern Lights.
```
