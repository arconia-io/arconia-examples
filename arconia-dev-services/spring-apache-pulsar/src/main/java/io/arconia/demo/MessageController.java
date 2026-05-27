package io.arconia.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    private final Producer producer;

    public MessageController(Producer producer) {
        this.producer = producer;
    }

    @GetMapping("/send")
    public String send(@RequestParam String message) {
        producer.send(message);
        return "Sent!";
    }
}