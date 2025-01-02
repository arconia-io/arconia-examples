package io.arconia.demo;

import io.arconia.multitenancy.core.context.TenantContextHolder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

@SpringBootApplication
public class TenantSourcePropertiesApplication {

	public static void main(String[] args) {
		SpringApplication.run(TenantSourcePropertiesApplication.class, args);
	}

	@Bean
	RouterFunction<ServerResponse> routerFunctions() {
		return RouterFunctions.route()
				.GET("/tenant", request -> ServerResponse.ok()
						.body(TenantContextHolder.getRequiredTenantIdentifier()))
				.build();
	}

}
