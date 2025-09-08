package io.arconia.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.mistralai.MistralAiEmbeddingOptions;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootApplication
public class OpenInferenceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenInferenceApplication.class, args);
	}

}

@RestController
class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    private final ChatClient chatClient;
    private final Tools tools;

    ChatController(ChatClient.Builder chatClientBuilder, Tools tools) {
        this.chatClient = chatClientBuilder.clone().build();
        this.tools = tools;
    }

    @GetMapping("/chat")
    String chat(String question) {
        logger.info("Chatting: {}", question);
        return chatClient
                .prompt(question)
                .call()
                .content();
    }

    @GetMapping("/chat/tools")
    String chatTools(String authorName) {
        logger.info("Chatting with tools: {}", authorName);
        var userPromptTemplate = "What books written by {author} are available in the library?";
        return chatClient.prompt()
                .user(userSpec -> userSpec
                        .text(userPromptTemplate)
                        .param("author", authorName)
                )
                .tools(tools)
                .call()
                .content();
    }

}

@RestController
class EmbeddingController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    private final EmbeddingModel embeddingModel;

    EmbeddingController(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    @GetMapping("/embed")
    String embed(String query) {
        logger.info("Embedding: {}", query);
        var embeddings = embeddingModel.embed(query);
        return "Size of the embedding vector: " + embeddings.length;
    }

    @GetMapping("/embed/multiple-queries")
    String embedProviderOptions(String query1, String query2) {
        logger.info("Multiple embeddings: {}, {}", query1, query2);
        var embeddings = embeddingModel.call(new EmbeddingRequest(List.of(query1, query2), MistralAiEmbeddingOptions.builder()
                        .withEncodingFormat("float")
                        .build()))
                .getResults();
        return "Size of the embedding vector 1: " + embeddings.get(0).getOutput().length + ", Size of the embedding vector 2: " + embeddings.get(1).getOutput().length;
    }

}

@Component
class Tools {

    private static final Logger logger = LoggerFactory.getLogger(Tools.class);

    private final BookService bookService = new BookService();

    @Tool(description = "Get the list of available books written by the given author")
    List<BookService.Book> booksByAuthor(BookService.Author author) {
        logger.info("Calling function to get books by author");
        return bookService.getBooksByAuthor(author);
    }

    @Tool(description = "Get the bestseller book written by the given author")
    BookService.Book bestsellerBookByAuthor(BookService.Author author) {
        logger.info("Calling function to get bestseller book by author");
        return bookService.getBestsellerByAuthor(author);
    }

}

@Service
@RegisterReflectionForBinding(classes = BookService.Book.class)
class BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);
    private static final Map<Integer,Book> books = new ConcurrentHashMap<>();

    static {
        books.put(1, new Book("His Dark Materials", "Philip Pullman"));
        books.put(2, new Book("Narnia", "C.S. Lewis"));
        books.put(3, new Book("The Hobbit", "J.R.R. Tolkien"));
        books.put(4, new Book("The Lord of The Rings", "J.R.R. Tolkien"));
        books.put(5, new Book("The Silmarillion", "J.R.R. Tolkien"));
    }

    List<Book> getBooksByAuthor(Author author) {
        return books.values().stream()
                .filter(book -> author.name().equals(book.author()))
                .toList();
    }

    Book getBestsellerByAuthor(Author author) {
        logger.info("Getting bestseller for author {}", author.name());
        return switch (author.name()) {
            case "J.R.R. Tolkien" -> books.get(4);
            case "C.S. Lewis" -> books.get(2);
            case "Philip Pullman" -> books.get(1);
            default -> null;
        };
    }

    public record Book(String title, String author) {}
    public record Author(String name) {}

}
