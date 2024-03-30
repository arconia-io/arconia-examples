package io.arconia.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestTenantSourcePropertiesApplication {

    public static void main(String[] args) {
        SpringApplication.from(TenantSourcePropertiesApplication::main).with(TestTenantSourcePropertiesApplication.class).run(args);
    }

}
