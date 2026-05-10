package io.arconia.demo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dust-readings")
class DustReadingController {

	private final DustReadingProducer producer;

	DustReadingController(DustReadingProducer producer) {
		this.producer = producer;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.ACCEPTED)
	void sendDustReading(@RequestBody DustReading reading) {
		producer.send(reading);
	}

}
