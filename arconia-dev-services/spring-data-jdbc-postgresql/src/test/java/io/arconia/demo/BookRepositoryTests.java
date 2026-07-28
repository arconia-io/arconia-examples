package io.arconia.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jdbc.test.autoconfigure.DataJdbcTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.Import;

import io.arconia.dev.services.postgresql.PostgresqlDevServicesAutoConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import(PostgresqlDevServicesAutoConfiguration.class)
class BookRepositoryTests {

	@Autowired
	private BookRepository bookRepository;

	@Test
	void createThenReadBook() {
		var createdBook = bookRepository.save(new Book(null, "The Iliad"));

		assertThat(createdBook.id()).isNotNull();

		var foundBook = bookRepository.findById(createdBook.id());

		assertThat(foundBook).isPresent();
		assertThat(foundBook.get().title()).isEqualTo("The Iliad");
	}

}
