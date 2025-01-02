package io.arconia.demo;

import io.arconia.demo.domain.Author;
import io.arconia.demo.domain.Book;
import io.arconia.demo.domain.BookService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.List;
import java.util.function.Function;

@Configuration(proxyBeanMethods = false)
class Functions {

    @Bean
    @Description("Get the list of books written by the given author available in the library")
    Function<Author, List<Book>> booksByAuthor(BookService bookService) {
        return bookService::getBooksByAuthor;
    }

}
