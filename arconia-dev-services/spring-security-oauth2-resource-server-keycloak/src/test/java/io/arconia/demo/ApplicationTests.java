package io.arconia.demo;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureRestTestClient
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ApplicationTests {

	@Autowired
	private RestTestClient restTestClient;

	@Autowired
	private Environment environment;

	@Test
	void issuerConfiguredFromDevService() {
		// The Dev Service contributes the issuer, so the application itself declares no
		// OAuth2 configuration at all.
		assertThat(environment.getProperty("spring.security.oauth2.resourceserver.jwt.issuer-uri"))
			.isNotNull()
			.endsWith("/realms/arconia");
	}

	@Test
	void requestWithoutTokenIsRejected() {
		restTestClient
			.get()
			.uri("/messages")
			.exchange()
			.expectStatus().isUnauthorized();
	}

	@Test
	void requestWithTokenIsAccepted() {
		String accessToken = requestAccessToken("isabella", "isabella");

		restTestClient
			.get()
			.uri("/messages")
			.headers(headers -> headers.setBearerAuth(accessToken))
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.jsonPath("$.message").isEqualTo("Hello, isabella!")
			.jsonPath("$.realmRoles").value(roles ->
				assertThat(roles.toString()).contains("admin").contains("user"));
	}

	/**
	 * Request a token from the Keycloak Dev Service with the direct access grant, using the
	 * client and users of the realm bundled with the Dev Service.
	 */
	private String requestAccessToken(String username, String password) {
		String issuerUri = environment.getProperty("spring.security.oauth2.resourceserver.jwt.issuer-uri");

		MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
		form.add("grant_type", "password");
		form.add("username", username);
		form.add("password", password);
		form.add("client_id", "arconia-app");
		form.add("client_secret", "arconia-secret");

		Map<?, ?> token = RestClient.create()
			.post()
			.uri(issuerUri + "/protocol/openid-connect/token")
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.body(form)
			.retrieve()
			.body(Map.class);

		assertThat(token).isNotNull();
		return (String) token.get("access_token");
	}

}
