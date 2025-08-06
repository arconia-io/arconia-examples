package io.arconia.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

import io.arconia.docling.client.DoclingClient;
import io.arconia.docling.client.convert.request.ConvertDocumentRequest;
import io.arconia.docling.client.convert.response.ConvertDocumentResponse;

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

	private final DoclingClient doclingClient;

	public DoclingController(DoclingClient doclingClient) {
		this.doclingClient = doclingClient;
	}

	@GetMapping("/convert")
	public String convertDocument(@RequestParam("url") String url) {
		ConvertDocumentResponse response = doclingClient
			.convertSource(ConvertDocumentRequest.builder()
				.addHttpSources(url)
				.build());
		return response.document().markdownContent();
	}

}	
