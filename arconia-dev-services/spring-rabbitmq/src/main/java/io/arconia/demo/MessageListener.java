package io.arconia.demo;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static io.arconia.demo.Constants.QUEUE_NAME;

@Component
public class MessageListener {
    private final MessageProcessor processor;

    public MessageListener(MessageProcessor processor) {
        this.processor = processor;
    }

    @RabbitListener(queues = QUEUE_NAME)
    void handle(Message message) {
        processor.process(message.message());
    }
}
