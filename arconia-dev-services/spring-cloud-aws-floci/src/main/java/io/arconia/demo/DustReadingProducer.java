package io.arconia.demo;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.stereotype.Component;

@Component
class DustReadingProducer {

	private final SqsTemplate sqsTemplate;

	DustReadingProducer(SqsTemplate sqsTemplate) {
		this.sqsTemplate = sqsTemplate;
	}

	void send(DustReading reading) {
		sqsTemplate.send(to -> to.queue("dust-readings").payload(reading));
	}

}
