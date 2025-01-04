package io.arconia.demo;

import io.arconia.ai.core.client.ArconiaChatClient;
import io.arconia.ai.core.tools.ToolCallbacks;
import io.arconia.ai.core.tools.method.MethodToolCallbackProvider;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Chat examples using the high-level ChatClient API.
 */
@RestController
class ChatController {

    private final ArconiaChatClient chatClient;
    private final Tools myTools;

    ChatController(ChatClient.Builder chatClientBuilder, Tools myTools) {
        this.chatClient = ((ArconiaChatClient.ArconiaBuilder) chatClientBuilder).build();
        this.myTools = myTools;
    }

    @GetMapping("/chat/function")
    String chat(String authorName) {
        var userPromptTemplate = "What books written by {author} are available in the library?";
        return chatClient.prompt()
                .user(userSpec -> userSpec
                        .text(userPromptTemplate)
                        .param("author", authorName)
                )
                .tools("booksByAuthor")
                .call()
                .content();
    }

    @GetMapping("/chat/function/list")
    String chatFunctionList(String bookTitle1, String bookTitle2) {
        var userPromptTemplate = "What authors wrote the books {bookTitle1} and {bookTitle2} available in the library?";
        return chatClient.prompt()
                .user(userSpec -> userSpec
                        .text(userPromptTemplate)
                        .param("bookTitle1", bookTitle1)
                        .param("bookTitle2", bookTitle2)
                )
                .tools("authorsByBooks")
                .call()
                .content();
    }

    @GetMapping("/chat/method/no-args")
    String chatMethodNoArgs() {
        return chatClient.prompt()
                .user("Welcome the user to the library")
                .tools(myTools)
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
                .tools(myTools)
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
                .tools(myTools)
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
                .tools(myTools)
                .call()
                .content();
    }

    @GetMapping("/chat/method/callback")
    String chatMethodCallback(String bookTitle1, String bookTitle2) {
        var userPromptTemplate = "What authors wrote the books {bookTitle1} and {bookTitle2} available in the library?";
        return chatClient.prompt()
                .user(userSpec -> userSpec
                        .text(userPromptTemplate)
                        .param("bookTitle1", bookTitle1)
                        .param("bookTitle2", bookTitle2)
                )
                .functions(ToolCallbacks.from(myTools))
                .call()
                .content();
    }

    @GetMapping("/chat/method/provider")
    String chatMethodResolver(String bookTitle1, String bookTitle2) {
        var userPromptTemplate = "What authors wrote the books {bookTitle1} and {bookTitle2} available in the library?";
        return chatClient.prompt()
                .user(userSpec -> userSpec
                        .text(userPromptTemplate)
                        .param("bookTitle1", bookTitle1)
                        .param("bookTitle2", bookTitle2)
                )
                .toolCallbackProviders(MethodToolCallbackProvider.builder()
                        .sources(myTools)
                        .build())
                .call()
                .content();
    }

}
