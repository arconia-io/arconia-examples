package io.arconia.demo;

import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/books")
class BookController {

    private static final AtomicLong idSequence = new AtomicLong();

    private final DynamoDbTemplate dynamoDbTemplate;

    BookController(DynamoDbTemplate dynamoDbTemplate) {
        this.dynamoDbTemplate = dynamoDbTemplate;
    }

    @GetMapping
    List<Book> getBooks() {
        return dynamoDbTemplate.scan(ScanEnhancedRequest.builder().build(), Book.class)
                .items()
                .stream().toList();
    }

    @GetMapping("/{id}")
    Book getBookById(@PathVariable Long id) {
        return dynamoDbTemplate.load(Key.builder().partitionValue(id).build(), Book.class);
    }

    @PostMapping
    Book createBook(@RequestBody Book book) {
        book.setId(idSequence.incrementAndGet());
        return dynamoDbTemplate.save(book);
    }

}
