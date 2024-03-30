package io.arconia.demo;

import io.arconia.core.multitenancy.context.TenantContextHolder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

@SpringBootApplication
public class WebHttpServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebHttpServerApplication.class, args);
	}

	@Bean
	RouterFunction<ServerResponse> routerFunctions() {
		return RouterFunctions.route()
				.GET("/tenant", request -> ServerResponse.ok()
						.body(TenantContextHolder.getRequiredTenantIdentifier()))
				.build();
	}

}
