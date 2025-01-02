package io.arconia.demo;

import io.arconia.ai.core.tools.method.MethodToolCallbackResolver;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Chat examples using the high-level ChatClient API.
 */
@RestController
class ChatController {

    private final ChatClient chatClient;
    private final Tools tools;

    ChatController(ChatClient.Builder chatClientBuilder, Tools tools) {
        this.chatClient = chatClientBuilder.build();
        this.tools = tools;
    }

    @GetMapping("/chat/function")
    String chat(String authorName) {
        var userPromptTemplate = "What books written by {author} are available in the library?";
        return chatClient.prompt()
                .user(userSpec -> userSpec
                        .text(userPromptTemplate)
                        .param("author", authorName)
                )
                .functions("booksByAuthor")
                .call()
                .content();
    }

    @GetMapping("/chat/method/no-args")
    String chatMethodNoArgs() {
        return chatClient.prompt()
                .user("Welcome the user to the library")
                .functions(MethodToolCallbackResolver.builder()
                        .target(tools)
                        .build()
                        .getToolCallbacks())
                .call()
                .content();
    }

    @GetMapping("/chat/method/void")
    String chatMethodVoid(String user) {
        var userPromptTemplate = "Welcome {user} to the library";
        return chatClient.prompt()
                .user(userSpec -> userSpec
                        .text(userPromptTemplate)
                        .param("user", user)
                )
                .functions(MethodToolCallbackResolver.builder()
                        .target(tools)
                        .build()
                        .getToolCallbacks())
                .call()
                .content();
    }

    @GetMapping("/chat/method/single")
    String chatMethodSingle(String authorName) {
        var userPromptTemplate = "What books written by {author} are available in the library?";
        return chatClient.prompt()
                .user(userSpec -> userSpec
                        .text(userPromptTemplate)
                        .param("author", authorName)
                )
                .functions(MethodToolCallbackResolver.builder()
                        .target(tools)
                        .build()
                        .getToolCallbacks())
                .call()
                .content();
    }

    @GetMapping("/chat/method/list")
    String chatMethodList(String bookTitle1, String bookTitle2) {
        var userPromptTemplate = "What authors wrote the books {bookTitle1} and {bookTitle2} available in the library?";
        return chatClient.prompt()
                .user(userSpec -> userSpec
                        .text(userPromptTemplate)
                        .param("bookTitle1", bookTitle1)
                        .param("bookTitle2", bookTitle2)
                )
                .functions(MethodToolCallbackResolver.builder()
                        .target(tools)
                        .build()
                        .getToolCallbacks())
                .call()
                .content();
    }

}
