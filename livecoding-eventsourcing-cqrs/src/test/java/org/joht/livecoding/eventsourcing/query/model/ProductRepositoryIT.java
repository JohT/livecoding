package org.joht.livecoding.eventsourcing.query.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.UUID;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.joht.livecoding.eventsourcing.Product;
import org.joht.livecoding.eventsourcing.command.model.ProductService;
import org.joht.livecoding.eventsourcing.infrastructure.axon.AxonConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

/**
 * These tests show how Event Sourcing and CQRS complement each other well.
 */
@EnableAutoWeld
@AddPackages(ProductService.class)
@AddPackages(ProductQueryService.class)
@AddBeanClasses(AxonConfiguration.class)
class ProductRepositoryIT {
	
	@Inject
	ProductRepository repositoryUnderTest;
	
	@Test
	@DisplayName("should signal a non existing product as not present")
	void shouldSignalNonExistingProduct() {
		assertFalse(repositoryUnderTest.read("non-existing-product-id").isPresent());
	}	
	
	@Test
	@DisplayName("should return the previously created product when reading it")
	void shouldReadTheSameProductThatHadBeenCreatedBefore() {
		String productId = UUID.randomUUID().toString();
		repositoryUnderTest.create(productId);
		Product readProduct = repositoryUnderTest.read(productId).get();
		assertEquals(productId, readProduct.getId());
	}

	@Test
	@DisplayName("should return the new name of a previously changed product when reading it")
	void shouldReadTheNewProductThatHadBeenChangedBefore() {
		String expectedProductName = "Test-Product-Name";

		String productId = UUID.randomUUID().toString();
		repositoryUnderTest.create(productId);
		repositoryUnderTest.update(new Product(productId, expectedProductName));
		
		Product readProduct = repositoryUnderTest.read(productId).get();
		assertEquals(productId, readProduct.getId());
		assertEquals(expectedProductName, readProduct.getName());
	}	
}