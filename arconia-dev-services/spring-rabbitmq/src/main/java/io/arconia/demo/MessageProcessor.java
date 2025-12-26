package io.arconia.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MessageProcessor {
    private static final Logger logger = LoggerFactory.getLogger(MessageProcessor.class);

    public void process(String message) {
        logger.info("Received message: {}", message);
    }
}
