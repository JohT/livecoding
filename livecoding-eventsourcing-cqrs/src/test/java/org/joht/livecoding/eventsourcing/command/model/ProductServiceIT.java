package org.joht.livecoding.eventsourcing.command.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.axonframework.eventsourcing.eventstore.DomainEventStream;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.joht.livecoding.eventsourcing.Product;
import org.joht.livecoding.eventsourcing.infrastructure.axon.AxonConfiguration;
import org.joht.livecoding.eventsourcing.message.event.ProductCreatedEvent;
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
		String productId = serviceUnderTest.createProduct();
		assertNotNull(productId);
		assertProductCreatedEventFirst(productId);
	}

	@Test
	@DisplayName("should be able to change an existing product")
	void shouldBeAbleToChangeAnExistingProduct() {
		String expectedProductName = "Test-Product-Name";
		String productId = serviceUnderTest.createProduct();
		
		Product changedProduct = serviceUnderTest.updateProductName(productId, expectedProductName);
		assertEquals(productId, changedProduct.getId());
		assertEquals(expectedProductName, changedProduct.getName());
		
		assertProductCreatedEventFirst(productId);
	}	
	
	@Test
	@DisplayName("should fail to change a non existing product")
	void shouldFailToChangeANonExistingProduct() {
		assertThrows(RuntimeException.class, () -> serviceUnderTest.updateProductName("non-existing-product-id", "new"));
	}	
	
	/**
	 * Asserts, that the first published event is {@link ProductCreatedEvent}.
	 * Prints event store entries for the product with the given id for demonstration and debugging purposes.
	 * @param productId {@link String}
	 */
	private void assertProductCreatedEventFirst(String productId) {
		DomainEventStream events = eventStore.readEvents(productId);
		assertEquals(ProductCreatedEvent.class, events.peek().getPayloadType());
		events.forEachRemaining(System.out::println);
	}	

}