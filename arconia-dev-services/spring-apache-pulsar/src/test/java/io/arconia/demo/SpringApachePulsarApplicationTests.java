package io.arconia.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.time.Duration;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.verify;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
class SpringApachePulsarApplicationTests {

    @MockitoSpyBean
    private Consumer consumer;

    @Autowired
    private MockMvcTester mvc;

    @Test
    void shouldProcessMessageSuccessfully() {
        String uuid = UUID.randomUUID().toString();

        var result = mvc.get()
                .uri("/send?message={msg}", uuid)
                .exchange();

        assertThat(result.getResponse().getStatus()).isEqualTo(200);

        await()
                .atMost(Duration.ofSeconds(10))
                .pollInterval(Duration.ofMillis(100))
                .untilAsserted(() -> verify(consumer).receive(uuid));
    }
}