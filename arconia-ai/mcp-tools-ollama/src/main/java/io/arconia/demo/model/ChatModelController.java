package io.arconia.demo.model;

import io.arconia.ai.mcp.tools.McpToolCallbackResolver;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.mcp.client.McpSyncClient;
import org.springframework.ai.model.function.FunctionCallingOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Chat examples using the low-level ChatModel API.
 */
@RestController
@RequestMapping("/model")
class ChatModelController {

    private final ChatModel chatModel;
    private final McpSyncClient mcpClient;

    ChatModelController(ChatModel chatModel, McpSyncClient mcpClient) {
        this.chatModel = chatModel;
        this.mcpClient = mcpClient;
    }

    @GetMapping("/chat/mcp")
    String chatMcp(String question) {
        var prompt = new Prompt(question, FunctionCallingOptions.builder()
                .functionCallbacks(McpToolCallbackResolver.builder()
                        .mcpClients(mcpClient)
                        .build()
                        .getToolCallbacks())
                .build());

        var chatResponse = chatModel.call(prompt);
        return chatResponse.getResult().getOutput().getText();
    }

}
