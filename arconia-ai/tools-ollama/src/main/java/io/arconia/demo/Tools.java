package io.arconia.demo;

import io.arconia.ai.tools.annotation.Tool;
import io.arconia.demo.domain.Author;
import io.arconia.demo.domain.Book;
import io.arconia.demo.domain.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Tools {

    private static final Logger logger = LoggerFactory.getLogger(Tools.class);

    private final BookService bookService;

    Tools(BookService bookService) {
        this.bookService = bookService;
    }

    @Tool("Welcome users to the library")
    void welcome() {
        logger.info("Welcoming users to the library");
    }

    @Tool("Welcome a specific user to the library")
    void welcomeUser(String user) {
        logger.info("Welcoming {} to the library", user);
    }

    @Tool("Get the list of books written by the given author available in the library")
    List<Book> booksByAuthor(String author) {
        logger.info("Getting books by author: {}", author);
        return bookService.getBooksByAuthor(new Author(author));
    }

    @Tool("Get the list of authors who wrote the given books available in the library")
    List<Author> authorsByBooks(List<String> books) {
        logger.info("Getting authors by books: {}", String.join(", ", books));
        return bookService.getAuthorsByBook(books.stream().map(b -> new Book(b, "")).toList());
    }

}
