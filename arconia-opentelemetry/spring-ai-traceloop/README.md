# Arconia OpenTelemetry with Spring AI and Traceloop

Application that demonstrates the use of [Arconia OpenTelemetry](https://docs.arconia.io/arconia/latest/opentelemetry/) with Spring AI and Traceloop, using the [Arconia OpenLLMetry Semantic Conventions](https://docs.arconia.io/arconia/latest/observability/semantic-conventions/openllmetry/).

## Pre-requisites

* Java 25
* Podman/Docker

### Ollama

The application consumes models from an [Ollama](https://ollama.ai) inference server. If you don't have an Ollama server running locally on your laptop, an Ollama service will be automatically spun up for you by the [Arconia Dev Services](https://docs.arconia.io/arconia/latest/dev-services/) when you run the application. Either way, Spring AI will take care of pulling the needed Ollama models when the application starts, if they are not available yet on your machine.

## Traceloop

This application uses [Traceloop](https://www.traceloop.com) to visualize the interactions with the language model. To use Traceloop, you need to set the `TRACELOOP_API_KEY` environment variable with your Traceloop API key.

```shell
export TRACELOOP_API_KEY=<your_traceloop_api_key>
```

The application is configured to export telemetry using the [OpenLLMetry Semantic Conventions](https://traceloop.com/docs/openllmetry) and to ship traces to Traceloop:

```yaml
arconia:
  observations:
    conventions:
      opentelemetry:
        ai:
          flavor: openllmetry
  otel:
    exporter:
      otlp:
        endpoint: "https://api.traceloop.com"
        headers:
          "Authorization": "Bearer ${TRACELOOP_API_KEY}"
```

Traceloop only supports traces, so logs and metrics export is disabled.

By default, the input and output message content, tool definitions, and tool call arguments and results are captured as span attributes, consistent with the official OpenLLMetry SDKs. You can customize this behavior via the `arconia.observations.conventions.opentelemetry.ai.*` configuration properties (see the [documentation](https://docs.arconia.io/arconia/latest/observability/semantic-conventions/openllmetry/)).

> [!WARNING]
> The OpenLLMetry Semantic Conventions are still experimental and subject to change.

## Running the application

Run the application as follows:

```shell
./gradlew bootRun
```

Alternatively, you can use the [Arconia CLI](https://arconia.io/docs/arconia-cli/latest/):

```shell
arconia dev
```

Under the hood, the Arconia framework will automatically spin up an [Ollama](https://docs.arconia.io/arconia/latest/dev-services/ollama/) service, in case you don't have one running locally (see [Arconia Dev Services](https://docs.arconia.io/arconia/latest/dev-services/) for more information).

The application will be accessible at http://localhost:8080.

Traces are exported via OTLP to Traceloop using the HTTP/Protobuf format.

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

## Exploring the traces

Open the [Traceloop dashboard](https://app.traceloop.com) and navigate to the traces view. For each request, you'll find:

* chat client spans with session tracking and input/output content;
* chat model spans with model parameters (temperature, max tokens, etc.) and token usage;
* tool execution spans with input and output content;
* embedding model spans.
