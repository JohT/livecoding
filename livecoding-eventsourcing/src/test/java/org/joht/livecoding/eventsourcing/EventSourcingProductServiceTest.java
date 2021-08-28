package org.joht.livecoding.eventsourcing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.joht.livecoding.eventsourcing.infrastructure.axon.AxonConfiguration;
import org.joht.livecoding.eventsourcing.infrastructure.axon.inject.cdi.AxonComponentDiscovery;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

@EnableAutoWeld
@AddPackages(ProductService.class)
@AddBeanClasses({AxonConfiguration.class, AxonComponentDiscovery.class})
public class EventSourcingProductServiceTest {
	
	@Inject
	ProductService productService;
	
	@Test
	void testCreateRead() {
		Product createdProduct = productService.createProduct();
		Product readProduct = productService.getProduct(createdProduct.getProductId());
		assertEquals(createdProduct, readProduct);
	}	
}