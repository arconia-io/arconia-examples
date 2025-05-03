package io.arconia.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SpringDataRedisApplicationTests {

	@Autowired
	BookRepository bookRepository;

	@BeforeEach
	void setUp() {
		bookRepository.deleteAll();
	}

	@Test
	void shouldSaveAndRetrieveBooks() {
		bookRepository.save(new Book("Cloud Native Spring in Action"));
		bookRepository.save(new Book("Spring Boot in Action"));

		long count = bookRepository.count();
		assertThat(count).isEqualTo(2);
	}
}
