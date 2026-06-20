package io.arconia.demo;

import org.springframework.pulsar.core.PulsarTemplate;
import org.springframework.stereotype.Service;

@Service
public class Producer {

    private final PulsarTemplate<String> pulsarTemplate;

    public Producer(PulsarTemplate<String> pulsarTemplate) {
        this.pulsarTemplate = pulsarTemplate;
    }

    public void send(String msg) {
        pulsarTemplate.send("my-topic", msg);
    }
}
