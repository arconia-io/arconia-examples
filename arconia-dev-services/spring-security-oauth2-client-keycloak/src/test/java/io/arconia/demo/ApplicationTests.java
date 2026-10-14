package io.arconia.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.web.servlet.client.RestTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureRestTestClient
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ApplicationTests {

	@Autowired
	private RestTestClient restTestClient;

	@Autowired
	private ClientRegistrationRepository clientRegistrations;

	@Test
	void clientRegistrationConfiguredFromDevService() {
		// The Dev Service registers the whole client registration itself, so the application
		// declares no OAuth2 configuration at all.
		ClientRegistration registration = clientRegistrations.findByRegistrationId("keycloak");
		assertThat(registration).isNotNull();
		assertThat(registration.getClientId()).isEqualTo("arconia-app");
		assertThat(registration.getProviderDetails().getIssuerUri()).endsWith("/realms/arconia");
	}

	@Test
	void unauthenticatedRequestRedirectsToKeycloak() {
		restTestClient
			.get()
			.uri("/")
			.exchange()
			.expectStatus().isFound()
			.expectHeader().value("Location", location ->
				assertThat(location).endsWith("/oauth2/authorization/keycloak"));
	}

	@Test
	void issuerEndpointIsPublic() {
		restTestClient
			.get()
			.uri("/issuer")
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.jsonPath("$.issuerUri").value(issuerUri ->
				assertThat(issuerUri.toString()).endsWith("/realms/arconia"));
	}

}
