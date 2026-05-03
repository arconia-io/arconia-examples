package io.arconia.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class LoggingLog4j2Application {

	static void main(String[] args) {
		SpringApplication.run(LoggingLog4j2Application.class, args);
	}

}

@RestController
class GreetingController {

	private static final Logger logger = LoggerFactory.getLogger(GreetingController.class);


	@GetMapping("/hello")
	String hello(@RequestParam(name = "name", defaultValue = "Mable") String name) {
		logger.info("Sending greetings to %s".formatted(name));
		return "Hello " + name;
	}

}
