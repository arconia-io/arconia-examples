# Arconia OpenTelemetry: Observability Signals

Application that demonstrates the use of [Arconia OpenTelemetry](https://arconia.io/docs/arconia/latest/opentelemetry/) to collect and export observability signals (logs, metrics, and traces) from a Spring Boot application.

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

Under the hood, the Arconia framework will automatically spin up a [Grafana LGTM](https://arconia.io/docs/arconia/latest/dev-services/lgtm/) observability platform using Testcontainers (see [Arconia Dev Services](https://arconia.io/docs/arconia/latest/dev-services/) for more information).

The application will be accessible at http://localhost:8080.

## Accessing Grafana

The application logs will show you the URL where you can access the Grafana observability platform.

```logs
...o.t.grafana.LgtmStackContainer: Access to the Grafana dashboard: http://localhost:38125
```

By default, logs, metrics, and traces are exported via OTLP using the HTTP/Protobuf format.

In Grafana, you can query the telemetry from the "Drilldown" and "Explore" sections.

## Using the application

Start by calling the root endpoint:

```shell
http :8080
```

Then, explore the endpoints using Micrometer instrumentation generating `micrometer.greeting` observations:

```shell
http :8080/micrometer/programmatic
http :8080/micrometer/declarative
```

Finally, explore the endpoints using OpenTelemetry instrumentation generating `otel.greeting` observations:

```shell
http :8080/opentelemetry/metrics
http :8080/opentelemetry/traces
```
