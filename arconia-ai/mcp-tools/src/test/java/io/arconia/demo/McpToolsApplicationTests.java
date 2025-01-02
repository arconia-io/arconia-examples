package io.arconia.demo;

import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "180s")
@Import(TestcontainersConfiguration.class)
@EnabledIfEnvironmentVariable(named = "BRAVE_API_KEY", matches = ".*")
class McpToolsApplicationTests {

    @Autowired
    WebTestClient webTestClient;

    @ParameterizedTest
    @ValueSource(strings = {"/chat/mcp"})
    void chatMcp(String path) {
        webTestClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .queryParam("question", "Does Spring AI supports integrations with Ollama?")
                        .build())
                .exchange()
                .expectStatus().isOk();
    }

}
