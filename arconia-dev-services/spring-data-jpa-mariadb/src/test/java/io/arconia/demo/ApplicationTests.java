package io.arconia.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/data.sql")
class ApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void createAndReadBooks() {
		var createdBook = webTestClient
			.post()
			.uri("/books")
			.bodyValue(new Book("The Iliad"))
			.exchange()
			.expectStatus().isOk()
			.expectBody(Book.class)
			.value(book -> {
				assertThat(book.getId()).isNotNull();
				assertThat(book.getTitle()).isEqualTo("The Iliad");
			})
			.returnResult().getResponseBody();

		assertThat(createdBook).isNotNull();

		webTestClient
			.get()
			.uri("/books/%s".formatted(createdBook.getId()))
			.exchange()
			.expectStatus().isOk()
			.expectBody(Book.class)
			.value(book -> {
				assertThat(book.getId()).isEqualTo(createdBook.getId());
				assertThat(book.getTitle()).isEqualTo("The Iliad");
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
