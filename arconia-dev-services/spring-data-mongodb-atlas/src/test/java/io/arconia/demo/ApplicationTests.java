package io.arconia.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void createAndReadBooks() {
		var createdBook = webTestClient
			.post()
			.uri("/books")
			.bodyValue(new Book(null, "The Iliad"))
			.exchange()
			.expectStatus().isOk()
			.expectBody(Book.class)
			.value(book -> {
				assertThat(book.id()).isNotNull();
				assertThat(book.title()).isEqualTo("The Iliad");
			})
			.returnResult().getResponseBody();

		assertThat(createdBook).isNotNull();

		webTestClient
			.get()
			.uri("/books/%s".formatted(createdBook.id()))
			.exchange()
			.expectStatus().isOk()
			.expectBody(Book.class)
			.value(book -> {
				assertThat(book.id()).isEqualTo(createdBook.id());
				assertThat(book.title()).isEqualTo("The Iliad");
			});
		
		webTestClient
			.get()
			.uri("/books")
			.exchange()
			.expectBodyList(Book.class)
			.value(books -> {
				assertThat(books).isNotEmpty();
			});
	}

}
