package io.arconia.demo;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.annotation.Observed;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

@SpringBootApplication
public class ObservabilitySignalsApplication {

	private static final Logger logger = LoggerFactory.getLogger(ObservabilitySignalsApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ObservabilitySignalsApplication.class, args);
	}

	@Bean
	RouterFunction<ServerResponse> routerFunction() {
		return RouterFunctions.route()
			.GET("/", _ -> {
				logger.info("Calling root endpoint");
				return ServerResponse.ok().body("Observability Rocks!");
			})
			.build();
	}

}

@RestController
@RequestMapping("/micrometer")
class MicrometerController {

	private final ObservationRegistry observationRegistry;

	MicrometerController(ObservationRegistry observationRegistry) {
		this.observationRegistry = observationRegistry;
	}

	@GetMapping("/programmatic")
	String micrometerObservation() {
		return Observation.createNotStarted("micrometer.greeting", observationRegistry)
			.lowCardinalityKeyValue("type", "programmatic")
			.observe(() -> "Hello from programmatic Micrometer Observation!");
	}

	@GetMapping("/declarative")
	@Observed(name = "micrometer.greeting", lowCardinalityKeyValues = "type=declarative")
	String micrometerAnnotation() {
		return "Hello from declarative Micrometer Observation!";
	}
	
}

@RestController
@RequestMapping("/opentelemetry")
class OpenTelemetryController {

	private final Meter meter;
	private final Tracer tracer;

	OpenTelemetryController(Meter meter, Tracer tracer) {
		this.meter = meter;
		this.tracer = tracer;
	}

	@GetMapping("/metrics")
	String otelMetrics() {
		meter.counterBuilder("otel.greeting")
			.build()
			.add(1L, Attributes.builder().put("type", "programmatic").build());
		return "Hello from OpenTelemetry Metrics!";
	}

	@GetMapping("/traces")
	String otelTraces() {
		Span span = tracer.spanBuilder("otel.greeting")
			.setAttribute("type", "programmatic")
			.startSpan();
		try {
			return "Hello from OpenTelemetry Tracing!";
		} finally {
			span.end();
		}
	}

}
