package io.arconia.demo;

import io.micrometer.core.instrument.MeterRegistry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class ObservabilitySignalsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ObservabilitySignalsApplication.class, args);
	}

}

@RestController
class GreetingController {

	private static final Logger logger = LoggerFactory.getLogger(GreetingController.class);

	// Micrometer API
	private final MeterRegistry registry;

	// OpenTelemetry API
	private final Meter meter;
	private final Tracer tracer;

	GreetingController(MeterRegistry registry, Meter meter, Tracer tracer) {
		this.registry = registry;
		this.meter = meter;
		this.tracer = tracer;
	}

	@GetMapping("/metrics/micrometer")
	public String metricsMicrometer(@RequestParam(defaultValue = "World") String name) {
		logger.info("Sending greetings to %s".formatted(name));
		registry.counter("micrometer.greetings.total", "name", name).increment();
		return "Hello " + name;
	}

	@GetMapping("/metrics/otel")
	public String metricsOtel(@RequestParam(defaultValue = "World") String name) {
		logger.info("Sending greetings to %s".formatted(name));
		meter.counterBuilder("otel.greetings.total")
			.build()
			.add(1L, Attributes.builder().put("name", name).build());
		return "Hello " + name;	
	}

	@GetMapping("/traces/otel")
	public String tracesOtel(@RequestParam(defaultValue = "World") String name) {
		Span span = tracer.spanBuilder("otel.greetings")
			.setAttribute("name", name)
			.startSpan();
		logger.info("Sending greetings to %s".formatted(name));
		span.end();
		return "Hello " + name;	
	}

}
