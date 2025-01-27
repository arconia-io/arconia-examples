package io.arconia.demo;

import io.arconia.ai.tools.ToolCallbacks;
import io.arconia.demo.domain.BookService;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ToolsTests {

    String value1 = """
            {
              "author": {
                "name" : "J.R.R. Tolkien"
              }
            }
            """;

    String value2 = """
            {
              "authors" : ["J.R.R. Tolkien", "Philip Pullman"]
            }
            """;

    @Test
    void nonStaticMethod() {
        var object = new Tools(new BookService());

        var functionCallbacks = ToolCallbacks.from(object);

        var booksByAuthor = Stream.of(functionCallbacks)
                .filter(func -> func.getName().equals("booksByAuthor"))
                .findFirst()
                .orElseThrow();

        String response1 = booksByAuthor.call(value1);

        assertThat(response1).isNotEmpty();
        assertThat(response1)
                .containsIgnoringWhitespaces("The Hobbit")
                .containsIgnoringWhitespaces("The Lord of The Rings")
                .containsIgnoringWhitespaces("The Silmarillion");

        var booksByAuthors = Stream.of(functionCallbacks)
                .filter(func -> func.getName().equals("booksByAuthors"))
                .findFirst()
                .orElseThrow();

        String response2 = booksByAuthors.call(value2);

        assertThat(response2).isNotEmpty();
    }

    @Test
    void noArgsNoReturnMethod() {
        var object = new Tools(new BookService());

        var functionCallbacks = ToolCallbacks.from(object);

        var welcome = Stream.of(functionCallbacks)
                .filter(func -> func.getName().equals("welcome"))
                .findFirst()
                .orElseThrow();

        String response = welcome.call("{}");

        assertThat(response).isEqualTo("Done");
    }

}