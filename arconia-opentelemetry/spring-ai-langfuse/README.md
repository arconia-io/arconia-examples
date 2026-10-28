# Arconia OpenTelemetry with Spring AI and Langfuse

Application that demonstrates the use of [Arconia OpenTelemetry](https://docs.arconia.io/arconia/latest/opentelemetry/) with Spring AI and Langfuse, using the [Arconia Langfuse Semantic Conventions](https://docs.arconia.io/arconia/latest/observability/semantic-conventions/langfuse/) and the [Arconia Langfuse Dev Service](https://docs.arconia.io/arconia/latest/dev-services/langfuse/).

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

Under the hood, the Arconia framework will automatically spin up a [Langfuse](https://docs.arconia.io/arconia/latest/dev-services/langfuse/) LLM engineering platform using Testcontainers, together with the services it needs. It will also spin up an [Ollama](https://docs.arconia.io/arconia/latest/dev-services/ollama/) service, in case you don't have one running locally (see [Arconia Dev Services](https://docs.arconia.io/arconia/latest/dev-services/) for more information).

Langfuse takes a bit longer to start than most services, since it runs several containers and applies its database migrations on startup.

The application will be accessible at http://localhost:8080.

## Accessing Langfuse

The application logs will show you the URL where you can access the Langfuse UI.

```logs
...Langfuse UI: http://localhost:<port>
```

Sign in with `arconia@example.com` (email) and `password` (password), and open the `Arconia` project to explore the traces of the application.

The traces are exported via OTLP using the HTTP/Protobuf format, authenticated with the API keys of the `Arconia` project. Langfuse accepts traces only, so logs and metrics are not exported while the Langfuse Dev Service is active.

## Calling the application

> [!NOTE]
> These examples use the [httpie](https://httpie.io) CLI to send HTTP requests.

### Chat

Call the application that will use a chat model to answer your question.

```shell
http :8080/chat question=="What is the capital of Italy?" -b
```

In Langfuse, the call is an agent observation for the chat client, containing a generation for the call to the chat model, with the input and output messages, the token usage, and the model parameters.

Next, try a request which uses tool calling.

```shell
http :8080/chat/tools authorName=="Philip Pullman" -b
```

In Langfuse, the tool executions are tool observations, with their arguments and results, between the generations of the chat model.

### Embedding

Call the application that will use an embedding model to generate embeddings for your query.

```shell
http :8080/embed query=="The capital of Italy is Rome"
```

Let's try now generating embeddings for multiple queries.

```shell
http :8080/embed/multiple-queries query1=="The capital of Italy is Rome" query2=="The capital of Denmark is Copenhagen" -b
```

In Langfuse, each call to the embedding model is an embedding observation.

## Using Langfuse Cloud

To send the traces to [Langfuse Cloud](https://langfuse.com) instead, disable the Langfuse Dev Service and configure the OTLP exporter with the API keys of your Langfuse project, encoded as `base64(<public-key>:<secret-key>)` in a `LANGFUSE_AUTH` environment variable:

```yaml
arconia:
  dev:
    services:
      langfuse:
        enabled: false
  otel:
    exporter:
      otlp:
        endpoint: https://cloud.langfuse.com/api/public/otel
        headers:
          "[Authorization]": "Basic ${LANGFUSE_AUTH}"
          "[x-langfuse-ingestion-version]": "4"
    logs:
      exporter:
        type: none
    metrics:
      exporter:
        type: none
```

For the US region of Langfuse Cloud, use `https://us.cloud.langfuse.com/api/public/otel` as the endpoint.
