package io.arconia.demo;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import ai.docling.api.serve.convert.request.ConvertDocumentRequest;
import ai.docling.api.serve.convert.request.source.FileSource;
import ai.docling.api.serve.DoclingServeApi;

import java.io.IOException;
import java.util.Base64;

import ai.docling.api.serve.convert.response.ConvertDocumentResponse;
import jakarta.annotation.PostConstruct;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@SpringBootApplication
public class RetrievalAugmentedGenerationApplication {

	public static void main(String[] args) {
		SpringApplication.run(RetrievalAugmentedGenerationApplication.class, args);
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
    void run() throws IOException {
		Resource file = new ClassPathResource("documents/story.pdf");
		ConvertDocumentResponse convertDocumentResponse = doclingServeApi
			.convertSource(ConvertDocumentRequest.builder()
				.source(FileSource.builder()
					.filename("story.pdf")
					.base64String(Base64.getEncoder().encodeToString(file.getContentAsByteArray()))
					.build())
				.build());
		
		Document document = new Document(convertDocumentResponse.getDocument().getMarkdownContent());

        vectorStore.add(new TokenTextSplitter().split(document));
    }

}

@RestController
class RagControllerVectorStore {

    private final ChatClient chatClient;

    RagControllerVectorStore(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
        this.chatClient = chatClientBuilder
			.defaultAdvisors(RetrievalAugmentationAdvisor.builder()
                .documentRetriever(VectorStoreDocumentRetriever.builder()
                        .vectorStore(vectorStore)
                        .build())
                .build())
			.build();
    }

    @PostMapping("/rag")
    String chatWithDocument(@RequestBody String question) {
        return chatClient.prompt()
                .user(question)
                .call()
                .content();
    }

}
