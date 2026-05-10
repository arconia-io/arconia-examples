package io.arconia.demo;

import io.awspring.cloud.sqs.annotation.SqsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
class DustReadingListener {

	private static final Logger log = LoggerFactory.getLogger(DustReadingListener.class);

	@SqsListener("dust-readings")
	void onDustReading(@Payload DustReading reading) {
		log.info("Dust reading observed in {} by {}: {}", reading.location(), reading.observer(), reading.event());
	}

}
