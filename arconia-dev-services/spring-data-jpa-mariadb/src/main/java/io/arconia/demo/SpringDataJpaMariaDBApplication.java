package io.arconia.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringDataJpaMariaDBApplication {
	private static final Logger log = LoggerFactory.getLogger(SpringDataJpaMariaDBApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringDataJpaMariaDBApplication.class, args);
	}

	@Bean
	ApplicationRunner applicationRunner(BookRepository bookRepository) {
		return args -> {
			bookRepository.save(new Book("Cloud Native Spring in Action"));
			bookRepository.save(new Book("Spring Boot in Action"));

			long count = bookRepository.count();
			log.info("Total number of books: {}", count);
		};
	}
}
