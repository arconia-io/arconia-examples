# Tools

Integrating with Tools, including `@Tools`-annotated methods.

## Ollama

The application consumes models from an [Ollama](https://ollama.ai) inference server. You can either run Ollama locally on your laptop,
or rely on the Testcontainers support in Spring Boot to spin up an Ollama service automatically.
If you choose the first option, make sure you have Ollama installed and running on your laptop.
Either way, Spring AI will take care of pulling the needed Ollama models when the application starts,
if they are not available yet on your machine.

## Running the application

If you're using the native Ollama application, run the application as follows.

```shell
./gradlew bootRun
```

If you want to rely on the native Testcontainers support in Spring Boot to spin up an Ollama service at startup time,
run the application as follows.

```shell
./gradlew bootTestRun
```

## Calling the application

> [!NOTE]
> These examples use the [httpie](https://httpie.io) CLI to send HTTP requests.

Call the application that will use a `@Tool`-annotated method to retrieve the context to answer your question.

```shell
http :8080/chat/method/void -b
```

```shell
http :8080/chat/method/single authorName=="J.R.R. Tolkien" -b
```

```shell
http :8080/chat/method/list authorName1=="J.R.R. Tolkien" authorName2=="Philip Pullman" -b
```
