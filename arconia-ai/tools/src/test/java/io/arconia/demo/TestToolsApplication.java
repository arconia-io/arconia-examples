package io.arconia.demo;

import org.springframework.boot.SpringApplication;

public class TestToolsApplication {

    public static void main(String[] args) {
        SpringApplication.from(ToolsApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
