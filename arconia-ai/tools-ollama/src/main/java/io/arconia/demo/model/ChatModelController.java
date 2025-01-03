package io.arconia.demo.model;

import io.arconia.ai.core.tools.ToolCallbacks;
import io.arconia.demo.Tools;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.model.function.FunctionCallingOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Chat examples using the low-level ChatModel API.
 */
@RestController
@RequestMapping("/model")
class ChatModelController {

    private final ChatModel chatModel;
    private final Tools myTools;

    ChatModelController(ChatModel chatModel, Tools myTools) {
        this.chatModel = chatModel;
        this.myTools = myTools;
    }

    @GetMapping("/chat/method/single")
    String chatMethodSingle(String authorName) {
        var userPromptTemplate = new PromptTemplate("""
                What books written by {author} are available in the library?
                """);
        Map<String,Object> model = Map.of("author", authorName);
        var prompt = userPromptTemplate.create(model, FunctionCallingOptions.builder()
                .functionCallbacks(ToolCallbacks.from(myTools))
                .build());

        var chatResponse = chatModel.call(prompt);
        return chatResponse.getResult().getOutput().getText();
    }

}
