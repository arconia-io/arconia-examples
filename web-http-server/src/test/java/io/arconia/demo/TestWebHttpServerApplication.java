package io.arconia.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.time.Duration;

@TestConfiguration(proxyBeanMethods = false)
public class TestWebHttpServerApplication {

    @Bean
    @ServiceConnection("otel/opentelemetry-collector-contrib")
    GenericContainer<?> lgtmContainer() {
        return new GenericContainer<>("docker.io/grafana/otel-lgtm")
                .withExposedPorts(3000, 4317, 4318)
                .withEnv("OTEL_METRIC_EXPORT_INTERVAL", "500")
                .waitingFor(Wait.forLogMessage(".*The OpenTelemetry collector and the Grafana LGTM stack are up and running.*\\s", 1))
                .withStartupTimeout(Duration.ofMinutes(2));
    }

    public static void main(String[] args) {
        SpringApplication.from(WebHttpServerApplication::main).with(TestWebHttpServerApplication.class).run(args);
    }

}
