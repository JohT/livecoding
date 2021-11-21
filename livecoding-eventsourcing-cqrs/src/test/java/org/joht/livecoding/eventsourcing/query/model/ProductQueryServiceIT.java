package org.joht.livecoding.eventsourcing.query.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.UUID;

import org.axonframework.eventhandling.gateway.EventGateway;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.joht.livecoding.eventsourcing.Product;
import org.joht.livecoding.eventsourcing.command.model.ProductService;
import org.joht.livecoding.eventsourcing.infrastructure.axon.AxonConfiguration;
import org.joht.livecoding.eventsourcing.message.event.ProductCreatedEvent;
import org.joht.livecoding.eventsourcing.message.event.ProductNameChangedEvent;
import org.joht.livecoding.eventsourcing.message.event.ProductNamePresetEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

/**
 * These tests show how the query side (CQRS) of an event sourced system can be tested based on events.
 */
@EnableAutoWeld
@AddPackages(ProductService.class)
@AddPackages(ProductQueryService.class)
@AddBeanClasses(AxonConfiguration.class)
class ProductQueryServiceIT {
	
	@Inject
	EventGateway eventGateway;
	
	@Inject
	ProductQueryService queryServiceUnderTest;
	
	@Test
	@DisplayName("should detect a product that is not present")
	void detectNonPresentProduct() {
		assertFalse(queryServiceUnderTest.getProduct("non-existing-product-id").isPresent());
	}	
	
	@Test
	@DisplayName("should be able to read a product that had been created before")
	void readProductAfterCreation() {
		String productId = createTestProductId();
		eventGateway.publish(new ProductCreatedEvent(productId));
		
		Product product = queryServiceUnderTest.getProduct(productId).get();
		assertEquals(productId, product.getId());
	}

	@Test
	@DisplayName("should be able to read the new name of a product that had been changed before")
	void readProductAfterNameChange() {
		String productId = createTestProductId();
		eventGateway.publish(new ProductCreatedEvent(productId));

		String expectedName = "New test product name";
		eventGateway.publish(new ProductNameChangedEvent(productId, expectedName, ""));
		
		Product readProduct = queryServiceUnderTest.getProduct(productId).get();
		assertEquals(productId, readProduct.getId());
		assertEquals(expectedName, readProduct.getName());
	}
	
	@Test
	@DisplayName("should be able to read the latest name of a product that had been changed multiple times")
	void readProductAfterMultipleChangs() {
		String productId = createTestProductId();
		eventGateway.publish(new ProductCreatedEvent(productId));

		String expectedName = "New test product name";
		eventGateway.publish(new ProductNamePresetEvent(productId, ""));
		eventGateway.publish(new ProductNameChangedEvent(productId, "any name the product had in between", ""));
		eventGateway.publish(new ProductNameChangedEvent(productId, expectedName, ""));
		
		Product readProduct = queryServiceUnderTest.getProduct(productId).get();
		assertEquals(productId, readProduct.getId());
		assertEquals(expectedName, readProduct.getName());
	}
	
	private static String createTestProductId() {
		return "Test-" + UUID.randomUUID();
	}
}