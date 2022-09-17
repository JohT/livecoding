package org.joht.livecoding.eventsourcing.infrastructure.axon;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.config.Configuration;
import org.axonframework.config.Configurer;
import org.axonframework.config.DefaultConfigurer;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.axonframework.queryhandling.QueryGateway;
import org.joht.livecoding.eventsourcing.infrastructure.axon.inject.cdi.AxonComponentDiscovery;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;

/**
 * Provides selected tools of the AxonFramework and their configuration for the use with CDI.
 */
@ApplicationScoped
public class AxonConfiguration {

	@Inject
	BeanManager beanManager;

	private Configuration configuration;

	@PostConstruct
	protected void startUp() {
		Configurer configurer = DefaultConfigurer.defaultConfiguration();
		querySideEventProcessing(configurer);
		AxonComponentDiscovery.ofBeanManager(beanManager).addDiscoveredComponentsTo(configurer);
		configuration = configurer.configureEmbeddedEventStore(this::eventStorageEngine).buildConfiguration();
		configuration.start();
	}

	@PreDestroy
	protected void shutdown() {
		configuration.shutdown();
	}

	@Produces
	@ApplicationScoped
	public CommandGateway getCommandGateway() {
		return configuration.commandGateway();
	}

	@Produces
	@ApplicationScoped
	public QueryGateway getQueryGateway() {
		return configuration.queryGateway();
	}

	@Produces
	@ApplicationScoped
	public EventGateway getEventGateway() {
		return configuration.eventGateway();
	}

	@Produces
	@ApplicationScoped
	public EventStore getEventStore() {
		return configuration.eventStore();
	}

	private EventStorageEngine eventStorageEngine(Configuration config) {
		return new InMemoryEventStorageEngine(); // Only for demonstration purposes.
	}

	private Configurer querySideEventProcessing(Configurer configurer) {
		EventProcessingConfigurer eventProcessing = configurer.eventProcessing();
		// For demonstration purposes the default event processor is set to "subscribing".
		// This means that events are notified synchronously within the publishing thread
		// and within the publishing transaction. If something goes wrong, everything
		// inside the transaction including the event itself is rolled back.
		eventProcessing.registerSubscribingEventProcessor("subscribing");
		eventProcessing.byDefaultAssignTo("subscribing");
		return configurer;
	}

}