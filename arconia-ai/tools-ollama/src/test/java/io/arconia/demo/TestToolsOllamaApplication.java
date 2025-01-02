package io.arconia.demo;

import org.springframework.boot.SpringApplication;

public class TestToolsOllamaApplication {

    public static void main(String[] args) {
        SpringApplication.from(ToolsOllamaApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
