# Arconia OpenTelemetry with Spring AI and OpenLit

Application that demonstrates the use of [Arconia OpenTelemetry](https://docs.arconia.io/arconia/latest/opentelemetry/) with Spring AI and OpenLit, using the [Arconia OpenTelemetry Semantic Conventions](https://docs.arconia.io/arconia/latest/observation/semantic-conventions/opentelemetry/) and the [Arconia OpenLit Dev Service](https://docs.arconia.io/arconia/latest/dev-services/openlit/).

## Pre-requisites

* Java 25
* Podman/Docker

### Ollama

The application consumes models from an [Ollama](https://ollama.ai) inference server. If you don't have an Ollama server running locally on your laptop, an Ollama service will be automatically spun up for you by the [Arconia Dev Services](https://docs.arconia.io/arconia/latest/dev-services/) when you run the application. Either way, Spring AI will take care of pulling the needed Ollama models when the application starts, if they are not available yet on your machine.

## Running the application

Run the application as follows:

```shell
./gradlew bootRun
```

Alternatively, you can use the [Arconia CLI](https://arconia.io/docs/arconia-cli/latest/):

```shell
arconia dev
```

Under the hood, the Arconia framework will automatically spin up an [OpenLit](https://docs.arconia.io/arconia/latest/dev-services/openlit/) AI observability platform using Testcontainers. It will also spin up an [Ollama](https://docs.arconia.io/arconia/latest/dev-services/ollama/) service, in case you don't have one running locally (see [Arconia Dev Services](https://docs.arconia.io/arconia/latest/dev-services/) for more information).

The application will be accessible at http://localhost:8080.

## Accessing OpenLit

The application logs will show you the URL where you can access the OpenLit AI observability platform.

```logs
...OpenLit UI: http://localhost:<port>
```

By default, traces are exported via OTLP using the HTTP/Protobuf format.

## Calling the application

> [!NOTE]
> These examples use the [httpie](https://httpie.io) CLI to send HTTP requests.

### Chat

Call the application that will use a chat model to answer your question.

```shell
http :8080/chat question=="What is the capital of Italy?" -b
```

Next, try a request which uses tool calling.

```shell
http :8080/chat/tools authorName=="Philip Pullman" -b
```

### Embedding

Call the application that will use an embedding model to generate embeddings for your query.

```shell
http :8080/embed query=="The capital of Italy is Rome"
```

Let's try now generating embeddings for multiple queries.

```shell
http :8080/embed/multiple-queries query1=="The capital of Italy is Rome" query2=="The capital of Denmark is Copenhagen" -b
```
