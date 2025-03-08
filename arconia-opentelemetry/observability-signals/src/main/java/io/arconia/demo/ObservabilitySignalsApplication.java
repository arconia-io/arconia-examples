package io.arconia.demo;

import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class ObservabilitySignalsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ObservabilitySignalsApplication.class, args);
	}

}

@RestController
class GreetingController {

	private static final Logger logger = LoggerFactory.getLogger(GreetingController.class);

	private final MeterRegistry registry;

	GreetingController(MeterRegistry registry) {
		this.registry = registry;
	}

	@GetMapping("/greeting")
	public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		logger.info("Sending greetings to %s".formatted(name));
		registry.counter("greetings.total", "name", name).increment();
		return "Hello " + name;
	}

}
