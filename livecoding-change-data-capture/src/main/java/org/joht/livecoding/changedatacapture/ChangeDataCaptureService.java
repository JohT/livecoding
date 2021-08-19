package org.joht.livecoding.changedatacapture;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.concurrent.ManagedExecutorService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

/**
 * This service uses the Debezium Engine to get notified about configured database changes directly (without kafka in between).
 * Every change in the example table "products" will be fired as an {@link Event} of type {@link ProductChanged}.
 * 
 * @see <a href="https://debezium.io/documentation/reference/1.6/development/engine.html#engine-properties">Debezium Engine</a>
 * @see <a href="https://github.com/debezium/debezium-examples/blob/master/cache-invalidation/src/main/java/io/debezium/examples/cacheinvalidation/persistence/DatabaseChangeEventListener.java">debezium-examples DatabaseChangeEventListener</a>
 */
@ApplicationScoped
public class ChangeDataCaptureService {

	private static final Logger LOG = Logger.getLogger(ChangeDataCaptureService.class.getName());

	@Inject
	ChangeDeserializerService changeDeserializer;
	
	@Inject
	DebeziumConfigRepository config;
	
	@Inject
	ManagedExecutorService executorService;
	
	@Inject
	Event<Change<Product>> changedProductEvent;

	private DebeziumEngine<ChangeEvent<String, String>> engine;
	
	public void startDebeziumEngine(@Observes @Initialized(ApplicationScoped.class) Object init) {
		this.engine = DebeziumEngine.create(io.debezium.engine.format.Json.class)
				.using(config.getProperties())
				.notifying(this::handleDbChangeEvent)
				.build();
		executorService.execute(engine);
		LOG.info("Debezium engine is started");
	}

	private void handleDbChangeEvent(ChangeEvent<String, String> record) {
		if (record.value() == null) {
			return;
		}
		Change<Product> changedProduct = changeDeserializer.deserializeChangeEvent(record.value(), Product.class);
		if (changedProduct.getOperation().equals(Change.Operation.READ_SNAPSHOT)) {
			return; // Skip snapshot read operation
		}
		changedProductEvent.fire(changedProduct);
		LOG.info("Record value" + record.value() + " notified as" + changedProduct);
	}

	@PreDestroy
	public void shutdown() {
		LOG.info("Stopping Debezium engine");
		try {
			engine.close();
		} catch (IOException e) {
			LOG.log(Level.FINE, e, () -> "debezium close error");
		}
	}
}