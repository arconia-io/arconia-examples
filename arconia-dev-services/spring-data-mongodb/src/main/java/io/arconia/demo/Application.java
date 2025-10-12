package io.arconia.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}

@Document("books")
record Book(@Id String id, String title) {}

interface BookRepository extends ListCrudRepository<Book, String> {}

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
	Optional<Book> getBookById(@PathVariable("id") String id) {
		return bookRepository.findById(id);
	}

	@PostMapping
	Book createBook(@RequestBody Book book) {
		return bookRepository.save(book);
	}

}
