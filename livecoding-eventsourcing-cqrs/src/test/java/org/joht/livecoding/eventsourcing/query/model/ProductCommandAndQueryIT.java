package org.joht.livecoding.eventsourcing.query.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
class ProductCommandAndQueryIT {
	
	@Inject
	ProductService productService;
	
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
		// Command-side
		String productId = productService.createProduct(); // command side
		// Query-side
		Product product = queryServiceUnderTest.getProduct(productId).get();
		assertEquals(productId, product.getId());
	}

	@Test
	@DisplayName("should be able to read the new name of a product that had been changed before")
	void readProductAfterNameChange() {
		String expectedName = "New test product name";
		// Command-side
		String productId = productService.createProduct();
		Product changedProduct = productService.updateProductName(productId, expectedName);
		// Query-side
		Product readProduct = queryServiceUnderTest.getProduct(productId).get();
		assertEquals(changedProduct, readProduct);
	}
}