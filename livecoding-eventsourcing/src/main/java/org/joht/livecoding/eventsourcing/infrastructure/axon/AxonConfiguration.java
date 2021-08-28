package org.joht.livecoding.eventsourcing.infrastructure.axon;

import java.util.concurrent.ExecutionException;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.config.Configuration;
import org.axonframework.config.Configurer;
import org.axonframework.config.DefaultConfigurer;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.serialization.RevisionResolver;
import org.axonframework.serialization.Serializer;
import org.joht.livecoding.eventsourcing.infrastructure.axon.inject.cdi.AxonComponentDiscovery;
import org.joht.livecoding.eventsourcing.infrastructure.axon.inject.cdi.AxonComponentDiscoveryContext;
import org.joht.livecoding.eventsourcing.infrastructure.axon.serializer.jsonb.axon.JsonbSerializer;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.Typed;
import jakarta.inject.Inject;

/**
 * Axon Configuration.
 * 
 * @author JohT
 */
@Typed()
@ApplicationScoped
public class AxonConfiguration {

	@Inject
	AxonComponentDiscovery componentDiscovery;

	private Configuration configuration;

	@PostConstruct
	protected void startUp() {
		Configurer configurer = DefaultConfigurer.defaultConfiguration();
		addDiscoveredComponentsTo(configurer);
		configuration = configurer
				.configureSerializer(this::jsonbSerializer)
				.configureEmbeddedEventStore(this::eventStorageEngine)
				.buildConfiguration();
		configuration.start();
	}

	@PreDestroy
	protected void shutdown() throws InterruptedException, ExecutionException {
		configuration.shutdown();
	}

	@Produces
	@ApplicationScoped
	public CommandGateway getCommandEmitterService() {
		return configuration.commandGateway();
	}


	@Produces
	@ApplicationScoped
	public QueryGateway getQuerySubmitterService() {
		return configuration.queryGateway();
	}

	private Serializer jsonbSerializer(Configuration config) {
		return JsonbSerializer.fieldAccess()
				.revisionResolver(config.getComponent(RevisionResolver.class))
				.build();
	}

	private EventStorageEngine eventStorageEngine(Configuration config) {
		return new InMemoryEventStorageEngine();
	}

	private Configurer addDiscoveredComponentsTo(Configurer configurer) {
		AxonComponentDiscoveryContext context = AxonComponentDiscoveryContext.builder().configurer(configurer).build();
		componentDiscovery.addDiscoveredComponentsTo(context);
		return configurer;
	}
}