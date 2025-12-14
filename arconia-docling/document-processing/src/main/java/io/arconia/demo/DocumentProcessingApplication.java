package io.arconia.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RestController;

import ai.docling.serve.api.convert.request.ConvertDocumentRequest;
import ai.docling.serve.api.convert.request.source.FileSource;
import ai.docling.serve.api.convert.request.source.HttpSource;
import ai.docling.serve.api.convert.response.ConvertDocumentResponse;
import ai.docling.serve.api.DoclingServeApi;

import java.io.IOException;
import java.net.URI;
import java.util.Base64;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@SpringBootApplication
public class DocumentProcessingApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocumentProcessingApplication.class, args);
	}

}

@RestController
@RequestMapping("/convert")
class DocumentProcessingController {

	private final DoclingServeApi doclingServeApi;

	DocumentProcessingController(DoclingServeApi doclingServeApi) {
		this.doclingServeApi = doclingServeApi;
	}

	@GetMapping("/http")
	String convertDocumentFromHttp(@RequestParam("url") String url) {
		ConvertDocumentResponse response = doclingServeApi
			.convertSource(ConvertDocumentRequest.builder()
				.source(HttpSource.builder().url(URI.create(url)).build())
				.build());
		return response.getDocument().getMarkdownContent();
	}

	@GetMapping("/file")
	String convertDocumentFromFile() throws IOException {
		Resource file = new ClassPathResource("documents/story.pdf");
		String base64File = Base64.getEncoder().encodeToString(file.getContentAsByteArray());
		ConvertDocumentResponse response = doclingServeApi
			.convertSource(ConvertDocumentRequest.builder()
				.source(FileSource.builder()
					.filename("story.pdf")
					.base64String(base64File)
					.build())
				.build());
		return response.getDocument().getMarkdownContent();
	}

}
