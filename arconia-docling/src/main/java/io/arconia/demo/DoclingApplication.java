package io.arconia.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

import ai.docling.api.serve.convert.request.ConvertDocumentRequest;
import ai.docling.api.serve.convert.request.source.HttpSource;
import ai.docling.api.serve.DoclingServeApi;

import java.net.URI;
import ai.docling.api.serve.convert.response.ConvertDocumentResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@SpringBootApplication
public class DoclingApplication {

	public static void main(String[] args) {
		SpringApplication.run(DoclingApplication.class, args);
	}

}

@RestController
class DoclingController {

	private final DoclingServeApi doclingClient;

	public DoclingController(DoclingServeApi doclingClient) {
		this.doclingClient = doclingClient;
	}

	@GetMapping("/convert")
	public String convertDocument(@RequestParam("url") String url) {
		ConvertDocumentResponse response = doclingClient
			.convertSource(ConvertDocumentRequest.builder()
				.source(HttpSource.builder().url(URI.create(url)).build())
				.build());
		return response.getDocument().getMarkdownContent();
	}

}	
