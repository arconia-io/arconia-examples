package io.arconia.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.pulsar.annotation.PulsarListener;
import org.springframework.stereotype.Service;

@Service
public class Consumer {

    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

    @PulsarListener(topics = "my-topic", subscriptionName = "sub1")
    public void receive(String message) {
        logger.info("Received: {}", message);
    }
}