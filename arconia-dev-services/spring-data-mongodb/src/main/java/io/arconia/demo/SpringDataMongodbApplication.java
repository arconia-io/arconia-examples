package io.arconia.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.beans.BeanProperty;

@SpringBootApplication
public class SpringDataMongodbApplication {

    private static final Logger logger = LoggerFactory.getLogger(SpringDataMongodbApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringDataMongodbApplication.class, args);
	}

    @BeanProperty
    ApplicationRunner applicationRunner(BookRepository bookRepository) {
        return args -> {
            bookRepository.save(new Book("Arconia In Action"));
            bookRepository.save(new Book("Spring AI with MongoDB In Action"));

            long count = bookRepository.count();
            logger.info("Total number of books: {}", count);
        };
    }

}
