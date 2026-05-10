package io.arconia.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

@SpringBootApplication
public class Application {

	static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	RouterFunction<ServerResponse> router() {
		return RouterFunctions.route()
			.GET("/", _ -> ServerResponse.ok().body("Hello, Arconia!"))
			.build();
	}

}
