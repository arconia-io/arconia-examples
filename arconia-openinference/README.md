# Arconia OpenInference

Application that demonstrates the use of [Arconia OpenTelemetry](https://docs.arconia.io/arconia/latest/opentelemetry/) with Spring AI and Phoenix, using the [Arconia OpenInference Semantic Conventions](https://docs.arconia.io/arconia/latest/observation/semantic-conventions/openinference/).

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

Under the hood, the Arconia framework will automatically spin up an [Arize Phoenix](https://docs.arconia.io/arconia/latest/dev-services/phoenix/) AI observability platform using Testcontainers (see [Arconia Dev Services](https://docs.arconia.io/arconia/latest/dev-services/) for more information).

The application will be accessible at http://localhost:8080.

## Accessing Phoenix

The application logs will show you the URL where you can access the Phoenix AI observability platform.

```logs
...Phoenix UI: http://localhost:<port>
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
