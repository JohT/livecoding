package org.joht.livecoding.eventsourcing.command.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.stream.Collectors;

import org.axonframework.eventhandling.DomainEventMessage;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.joht.livecoding.eventsourcing.Product;
import org.joht.livecoding.eventsourcing.infrastructure.axon.AxonConfiguration;
import org.joht.livecoding.eventsourcing.message.event.ProductCreatedEvent;
import org.joht.livecoding.eventsourcing.message.event.ProductNameChangedEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

/**
 * These tests show how Event Sourcing works on its own (without CQRS).
 * Note: Event Sourcing on its own doesn't scale or perform well when it comes to a large amount of events
 * and queries.
 */
@EnableAutoWeld
@AddPackages(ProductService.class)
@AddBeanClasses(AxonConfiguration.class)
class ProductServiceIT {
	
	@Inject
	ProductService serviceUnderTest;
	
	@Inject
	EventStore eventStore;
	
	@Test
	@DisplayName("should be able to create a new product")
	void shouldBeAbleToCreateANewProduct() {
		String newProductId = serviceUnderTest.createProduct();
		assertNotNull(newProductId);
		
		List<ProductCreatedEvent> events = getEventsOfIdAndType(newProductId, ProductCreatedEvent.class);
		assertEquals(1, events.size());
		assertEquals(newProductId, events.get(0).getProductId());
	}

	@Test
	@DisplayName("should be able to change an existing product")
	void shouldBeAbleToChangeAnExistingProduct() {
		String expectedProductName = "Test-Product-Name";
		String productId = serviceUnderTest.createProduct();
		
		Product changedProduct = serviceUnderTest.updateProductName(productId, expectedProductName);
		assertEquals(productId, changedProduct.getId());
		assertEquals(expectedProductName, changedProduct.getName());
		
		List<ProductNameChangedEvent> events = getEventsOfIdAndType(productId, ProductNameChangedEvent.class);
		assertEquals(expectedProductName, events.get(events.size() - 1).getName());
	}	
	
	@Test
	@DisplayName("should fail to change a non existing product")
	void shouldFailToChangeANonExistingProduct() {
		assertThrows(RuntimeException.class, () -> serviceUnderTest.updateProductName("non-existing-product-id", "new"));
	}	
	
	/**
	 * Gets all events of the given type for the given aggregate id as {@link List}.
	 * @param <T> Type
	 * @param id {@link String}
	 * @param eventType {@link Class}
	 * @return {@link List} of events of the given type.
	 */
	private <T> List<T> getEventsOfIdAndType(String id, Class<T> eventType) {
		return eventStore.readEvents(id).asStream()
				.map(DomainEventMessage::getPayload)
				.filter(eventType::isInstance)
				.map(eventType::cast)
				.collect(Collectors.toList());
	}

}