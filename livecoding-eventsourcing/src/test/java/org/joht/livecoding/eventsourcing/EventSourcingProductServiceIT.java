package org.joht.livecoding.eventsourcing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.axonframework.eventsourcing.eventstore.EventStore;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.joht.livecoding.eventsourcing.infrastructure.axon.AxonConfiguration;
import org.joht.livecoding.eventsourcing.infrastructure.axon.inject.cdi.AxonComponentDiscovery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

@EnableAutoWeld
@AddPackages(ProductService.class)
@AddBeanClasses({AxonConfiguration.class, AxonComponentDiscovery.class})
class EventSourcingProductServiceIT {
	
	@Inject
	ProductService productService;
	
	@Inject
	EventStore eventStore;
	
	@Test
	@DisplayName("should signal a non existing product as not present")
	void shouldSignalNonExistingProduct() {
		assertFalse(productService.getProduct("non-existing-product-id").isPresent());
	}	
	
	@Test
	@DisplayName("should return the previously created product when reading it")
	void shouldReadTheSameProductThatHadBeenCreatedBefore() {
		Product createdProduct = productService.createProduct();
		Product readProduct = productService.getProduct(createdProduct.getId()).get();
		assertEquals(createdProduct, readProduct);
	}

	@Test
	@DisplayName("should return the new name of a previously changed product when reading it")
	void shouldReadTheNewProductThatHadBeenChangedBefore() {
		String expectedProductName = "Test-Product-Name";
		Product createdProduct = productService.createProduct();
		
		Product changedProduct = productService.changeProductName(createdProduct.getId(), expectedProductName);
		assertEquals(createdProduct.getId(), changedProduct.getId());
		assertEquals(expectedProductName, changedProduct.getName());
		
		Product readProduct = productService.getProduct(createdProduct.getId()).get();
		assertEquals(changedProduct, readProduct);

		printEventStoreEntries(createdProduct.getId());
	}	
	
	/**
	 * Print event store entries for the product with the given id for demonstration and debugging purposes.
	 * @param productId {@link String}
	 */
	private void printEventStoreEntries(String productId) {
		eventStore.readEvents(productId).forEachRemaining(System.out::println);
	}	

}