package io.arconia.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.client.RestTestClient;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@AutoConfigureRestTestClient
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/data.sql")
class ApplicationTests {

	@Autowired
	private RestTestClient restTestClient;

	@Test
	void readBooks() {
		restTestClient
			.get()
			.uri("/books")
			.exchange()
			.expectBody(new ParameterizedTypeReference<List<Book>>() {})
			.value(books -> {
				assertThat(books).isNotEmpty();
			});
	}

	@Test
	void readBookById() {
		var createdBook = restTestClient
			.post()
			.uri("/books")
			.body(new Book(null, "The Iliad"))
			.exchange()
			.expectStatus().isOk()
			.expectBody(Book.class)
			.returnResult().getResponseBody();

		assertThat(createdBook).isNotNull();

		restTestClient
			.get()
			.uri("/books/%s".formatted(createdBook.id()))
			.exchange()
			.expectStatus().isOk()
			.expectBody(Book.class)
			.value(book -> {
				assertThat(book.id()).isEqualTo(createdBook.id());
				assertThat(book.title()).isEqualTo("The Iliad");
			});
	}

	@Test
	void createBook() {
		restTestClient
			.post()
			.uri("/books")
			.body(new Book(null, "De Bello Gallico"))
			.exchange()
			.expectStatus().isOk()
			.expectBody(Book.class)
			.value(book -> {
				assertThat(book.id()).isNotNull();
				assertThat(book.title()).isEqualTo("De Bello Gallico");
			});
	}

}
