package io.arconia.demo;

import org.springframework.boot.SpringApplication;

public class TestWebHttpServerApplication {

    public static void main(String[] args) {
        SpringApplication.from(WebHttpServerApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
