package io.arconia.demo;

import io.awspring.cloud.s3.S3Template;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.List;

@RestController
@RequestMapping("/scrolls")
public class ScrollController {

	static final String BUCKET = "magisterium-archives";

	private final S3Template s3Template;
	private final S3Client s3Client;

	ScrollController(S3Template s3Template, S3Client s3Client) {
		this.s3Template = s3Template;
		this.s3Client = s3Client;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	Scroll storeScroll(@RequestBody Scroll scroll) {
		s3Template.store(BUCKET, scroll.name(), scroll);
		return scroll;
	}

	@GetMapping("/{name}")
	Scroll getScroll(@PathVariable String name) {
		return s3Template.read(BUCKET, name, Scroll.class);
	}

	@GetMapping
	List<String> listScrolls() {
		return s3Client.listObjectsV2(r -> r.bucket(BUCKET))
			.contents()
			.stream()
			.map(S3Object::key)
			.toList();
	}

}
