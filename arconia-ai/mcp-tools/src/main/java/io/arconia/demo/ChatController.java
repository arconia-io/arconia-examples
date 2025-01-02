package io.arconia.demo;

import io.arconia.ai.mcp.tools.McpToolCallbackResolver;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.mcp.client.McpSyncClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Chat examples using the high-level ChatClient API.
 */
@RestController
class ChatController {

    private final ChatClient chatClient;
    private final McpSyncClient mcpClient;

    ChatController(ChatClient.Builder chatClientBuilder, McpSyncClient mcpClient) {
        this.chatClient = chatClientBuilder.build();
        this.mcpClient = mcpClient;
    }

    @GetMapping("/chat/mcp")
    String chatMcp(String question) {
        return chatClient.prompt()
                .user(question)
                .functions(McpToolCallbackResolver.builder()
                        .mcpClients(mcpClient)
                        .build()
                        .getToolCallbacks())
                .call()
                .content();
    }

}
