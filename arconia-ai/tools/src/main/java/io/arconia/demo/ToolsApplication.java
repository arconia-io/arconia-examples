package io.arconia.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ToolsApplication {

    private static final Logger logger = LoggerFactory.getLogger(ToolsApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ToolsApplication.class, args);
    }

}
