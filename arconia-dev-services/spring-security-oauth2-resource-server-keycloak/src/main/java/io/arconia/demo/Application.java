package io.arconia.demo;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class Application {

	static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/issuer").permitAll()
				.anyRequest().authenticated())
			.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
			.csrf(csrf -> csrf.disable())
			.build();
	}

}

@RestController
class MessageController {

	@GetMapping("/messages")
	Map<String, Object> messages(@AuthenticationPrincipal Jwt jwt) {
		var response = new LinkedHashMap<String, Object>();
		response.put("message", "Hello, %s!".formatted(jwt.getClaimAsString("preferred_username")));
		response.put("subject", jwt.getSubject());
		response.put("issuer", jwt.getIssuer().toString());
		response.put("realmRoles", realmRoles(jwt));
		return response;
	}

	/**
	 * Keycloak publishes realm roles under the "realm_access" claim rather than mapping them
	 * to Spring Security authorities, so they are read from the token directly.
	 */
	private List<?> realmRoles(Jwt jwt) {
		if (jwt.getClaim("realm_access") instanceof Map<?, ?> realmAccess
				&& realmAccess.get("roles") instanceof List<?> roles) {
			return roles;
		}
		return List.of();
	}

}

@RestController
class IssuerController {

	private final String issuerUri;

	IssuerController(@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri:not configured}") String issuerUri) {
		this.issuerUri = issuerUri;
	}

	@GetMapping("/issuer")
	Map<String, String> issuer() {
		return Map.of(
			"issuerUri", issuerUri,
			"tokenEndpoint", issuerUri + "/protocol/openid-connect/token");
	}

}
