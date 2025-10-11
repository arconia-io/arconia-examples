package io.arconia.demo;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	RouterFunction<ServerResponse> router() {
		return RouterFunctions.route()
			.GET("/", _ -> ServerResponse.ok().body("Hello, Arconia!"))
			.build();
	}

}

record Book(@Id Long id, String title) {}

interface BookRepository extends ListCrudRepository<Book, Long> {}

@RestController
@RequestMapping("/books")
class BookController {

	private final BookRepository bookRepository;

	BookController(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	@GetMapping
	List<Book> getBooks() {
		return bookRepository.findAll();
	}

	@GetMapping("/{id}")
	Optional<Book> getBookById(@PathVariable("id") Long id) {
		return bookRepository.findById(id);
	}

	@PostMapping
	Book createBook(@RequestBody Book book) {
		return bookRepository.save(book);
	}

}
