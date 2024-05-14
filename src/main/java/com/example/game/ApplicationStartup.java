package com.example.game;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import jdk.jfr.consumer.EventStream;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {
	public static final String JDK_TLS_HANDSHAKE = "jdk.TLSHandshake";
	public static final String JDK_X509_CERT = "jdk.X509Certificate";
	public static final String JDK_X509_VALIDATION = "jdk.X509Validation";

	public static final String CERT_ISSUER = "issuer";
	public static final String VALIDATION_COUNTER = "validationCounter";


	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		CompositeMeterRegistry metricsRegistry = Metrics.globalRegistry;

		try (var es = EventStream.openRepository()) {

			countTLSHandshake(metricsRegistry, es);
			countX509Parser(metricsRegistry, es);

			es.onEvent(JDK_X509_VALIDATION, recordedEvent -> {
				Gauge.builder(JDK_X509_VALIDATION + VALIDATION_COUNTER,
								recordedEvent, e -> e.getLong(VALIDATION_COUNTER))
						.description("X509 Certificate Validation Gauge").register(metricsRegistry);
			});

			es.start();
		} catch (IOException e) {
			throw new RuntimeException("Couldn't process event", e);
		}
	}

	private static void countTLSHandshake(CompositeMeterRegistry metricsRegistry, EventStream es) {
		es.onEvent(JDK_TLS_HANDSHAKE, recordedEvent -> {

			Counter counter = metricsRegistry.find(JDK_TLS_HANDSHAKE).counter();
			if (Objects.nonNull(counter)) {
				metricsRegistry.counter(JDK_TLS_HANDSHAKE).increment();
			} else {
				counter = Counter.builder(JDK_TLS_HANDSHAKE)
						.description("TLS Handshake counter")
						.register(metricsRegistry);
				counter.increment();
			}
		});
	}

	private static void countX509Parser(CompositeMeterRegistry metricsRegistry, EventStream es) {
		es.onEvent(JDK_X509_CERT, recordedEvent -> {
			String issuer =  recordedEvent.getString(CERT_ISSUER);
			Counter counter = metricsRegistry.find(JDK_X509_CERT + issuer).counter();

			if (Objects.nonNull(counter)) {
				metricsRegistry.counter(JDK_X509_CERT + issuer).increment();
			} else {
				counter = Counter.builder(JDK_X509_CERT + issuer)
						.description("X509 Certificate Parsing Counter")
						.register(metricsRegistry);
				counter.increment();
			}
		});
	}

}