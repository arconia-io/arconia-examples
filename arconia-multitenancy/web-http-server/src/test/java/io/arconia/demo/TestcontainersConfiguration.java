package io.arconia.demo;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.grafana.LgtmStackContainer;

import java.time.Duration;

@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

    @Bean
    @ServiceConnection
    LgtmStackContainer lgtmContainer() {
        return new LgtmStackContainer("grafana/otel-lgtm:0.8.1")
                .withStartupTimeout(Duration.ofMinutes(2))
                .withReuse(true);
    }

}
