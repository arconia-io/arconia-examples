package io.arconia.demo;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static io.arconia.demo.Constants.EXCHANGE_NAME;
import static io.arconia.demo.Constants.ROUTING_KEY;

@RestController
class MessageController {
    private final RabbitTemplate rabbitTemplate;

    MessageController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/api/messages")
    void sendMessage(@RequestBody Message message) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, message);
    }
}
