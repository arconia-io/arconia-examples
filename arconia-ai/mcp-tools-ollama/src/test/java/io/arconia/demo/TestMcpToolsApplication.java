package io.arconia.demo;

import org.springframework.boot.SpringApplication;

public class TestMcpToolsApplication {

    public static void main(String[] args) {
        SpringApplication.from(McpToolsApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
