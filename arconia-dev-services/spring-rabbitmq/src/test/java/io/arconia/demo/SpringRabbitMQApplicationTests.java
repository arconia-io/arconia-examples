package io.arconia.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.time.Duration;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.verify;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
class SpringRabbitMQApplicationTests {

    @MockitoSpyBean
    private MessageProcessor messageProcessor;

    @Autowired
    private MockMvcTester mvc;

    @Test
    void shouldProcessMessageSuccessfully() {
        String uuid = UUID.randomUUID().toString();

        var testResult = mvc.post().uri("/api/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "message": "%s"
                        }
                        """.formatted(uuid))
                .exchange();
        assertThat(testResult.getResponse().getStatus()).isEqualTo(200);

        await()
                .atMost(Duration.ofSeconds(10))
                .with()
                .pollInterval(Duration.ofMillis(100))
                .untilAsserted(() -> verify(messageProcessor).process(uuid));
    }
}
