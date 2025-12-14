package io.arconia.demo;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ai.docling.serve.api.DoclingServeApi;
import io.arconia.ai.document.docling.DoclingDocumentReader;
import jakarta.annotation.PostConstruct;

import java.util.List;

@SpringBootApplication
public class DoclingRagApplication {

	public static void main(String[] args) {
		SpringApplication.run(DoclingRagApplication.class, args);
	}

}

@Component
class IngestionPipeline {

    private final DoclingServeApi doclingServeApi;

    private final VectorStore vectorStore;

    IngestionPipeline(DoclingServeApi doclingServeApi, VectorStore vectorStore) {
        this.doclingServeApi = doclingServeApi;
        this.vectorStore = vectorStore;
    }

    @PostConstruct
    void run() {
        Resource file = new ClassPathResource("documents/story.pdf");

        List<Document> documents = DoclingDocumentReader.builder()
                .doclingServeApi(doclingServeApi)
                .files(file)
                .build()
                .get();
        
        vectorStore.add(documents);
    }

}

@RestController
class ChatController {

    private final ChatClient chatClient;

    ChatController(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
        this.chatClient = chatClientBuilder
            .defaultAdvisors(RetrievalAugmentationAdvisor.builder()
                .documentRetriever(VectorStoreDocumentRetriever.builder()
                    .vectorStore(vectorStore)
                    .build())
                .build())
            .build();
    }

    @GetMapping("/chat")
    String chat(@RequestParam("question") String question) {
        return chatClient
                .prompt(question)
                .call()
                .content();
    }

}
