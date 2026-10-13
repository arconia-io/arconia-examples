package io.arconia.demo;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
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
			.oauth2Login(Customizer.withDefaults())
			.logout(logout -> logout.logoutSuccessUrl("/issuer"))
			.build();
	}

}

@RestController
class UserController {

	@GetMapping("/")
	Map<String, Object> currentUser(@AuthenticationPrincipal OidcUser user) {
		var details = new LinkedHashMap<String, Object>();
		details.put("username", user.getPreferredUsername());
		details.put("name", user.getFullName());
		details.put("email", user.getEmail());
		details.put("realmRoles", realmRoles(user));
		return details;
	}

	/**
	 * Keycloak publishes realm roles under the "realm_access" claim rather than mapping them
	 * to Spring Security authorities, so they are read from the ID token directly.
	 */
	private List<?> realmRoles(OidcUser user) {
		if (user.getClaim("realm_access") instanceof Map<?, ?> realmAccess
				&& realmAccess.get("roles") instanceof List<?> roles) {
			return roles;
		}
		return List.of();
	}

}

@RestController
class IssuerController {

	private final String issuerUri;

	IssuerController(ObjectProvider<ClientRegistrationRepository> clientRegistrations) {
		// The Dev Service registers the client registration as a bean rather than contributing
		// configuration properties, so the issuer is read from the registration itself.
		var registration = clientRegistrations.stream()
			.map(repository -> repository.findByRegistrationId("keycloak"))
			.filter(Objects::nonNull)
			.findFirst();
		this.issuerUri = registration
			.map(found -> found.getProviderDetails().getIssuerUri())
			.orElse("not configured");
	}

	@GetMapping("/issuer")
	Map<String, String> issuer() {
		return Map.of("issuerUri", issuerUri);
	}

}
