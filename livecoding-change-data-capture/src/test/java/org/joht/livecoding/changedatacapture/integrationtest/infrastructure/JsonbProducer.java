package org.joht.livecoding.changedatacapture.integrationtest.infrastructure;

import java.util.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

/**
 * The {@link Jsonb} can only be injected in a full Jakarta EE or MicroProfile environment.
 * This producer provides it for the "Weld" (= CDI only) test environment.
 */
public class JsonbProducer {

	private static final Logger LOG = Logger.getLogger(JsonbProducer.class.getName());
	
	@Produces
	@ApplicationScoped
	Jsonb produceJsonb() {
		LOG.info("Starting Jsonb");
		return JsonbBuilder.create(); 
	}

}
