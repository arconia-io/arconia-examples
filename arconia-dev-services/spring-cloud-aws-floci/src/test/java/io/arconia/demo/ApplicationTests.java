package io.arconia.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.servlet.client.RestTestClient;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@AutoConfigureRestTestClient
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ApplicationTests {

	@Autowired
	private RestTestClient restTestClient;

	@Test
	void readBooks() {
		restTestClient
			.post()
			.uri("/books")
			.body(new Book(null, "Northern Lights"))
			.exchange()
			.expectStatus().isOk();

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
			.body(new Book(null, "The Subtle Knife"))
			.exchange()
			.expectStatus().isOk()
			.expectBody(Book.class)
			.returnResult().getResponseBody();

		assertThat(createdBook).isNotNull();

		restTestClient
			.get()
			.uri("/books/%s".formatted(createdBook.getId()))
			.exchange()
			.expectStatus().isOk()
			.expectBody(Book.class)
			.value(book -> {
				assertThat(book.getId()).isEqualTo(createdBook.getId());
				assertThat(book.getTitle()).isEqualTo("The Subtle Knife");
			});
	}

	@Test
	void createBook() {
		restTestClient
			.post()
			.uri("/books")
			.body(new Book(null, "The Amber Spyglass"))
			.exchange()
			.expectStatus().isOk()
			.expectBody(Book.class)
			.value(book -> {
				assertThat(book.getId()).isNotNull();
				assertThat(book.getTitle()).isEqualTo("The Amber Spyglass");
			});
	}

	@Test
	void sendDustReading() {
		restTestClient
			.post()
			.uri("/dust-readings")
			.body(new DustReading("dust.surge", "svalbard", "lord_asriel"))
			.exchange()
			.expectStatus().is2xxSuccessful();
	}

	@Test
	void storeAndReadScroll() {
		var scroll = new Scroll("prophecy-of-dust", "The Dust will settle where the heart is true.");

		restTestClient
			.post()
			.uri("/scrolls")
			.body(scroll)
			.exchange()
			.expectStatus().isCreated()
			.expectBody(Scroll.class)
			.value(s -> {
				assertThat(s.name()).isEqualTo("prophecy-of-dust");
				assertThat(s.content()).isEqualTo("The Dust will settle where the heart is true.");
			});

		restTestClient
			.get()
			.uri("/scrolls/prophecy-of-dust")
			.exchange()
			.expectStatus().isOk()
			.expectBody(Scroll.class)
			.value(s -> {
				assertThat(s.name()).isEqualTo("prophecy-of-dust");
				assertThat(s.content()).isEqualTo("The Dust will settle where the heart is true.");
			});
	}

}
