package org.joht.livecoding.changedatacapture.integrationtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.util.BlockingArrayQueue;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.joht.livecoding.changedatacapture.Change;
import org.joht.livecoding.changedatacapture.ChangeDataCaptureService;
import org.joht.livecoding.changedatacapture.Product;
import org.joht.livecoding.changedatacapture.integrationtest.infrastructure.Integrationtest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import jakarta.enterprise.event.Event;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

@EnableAutoWeld
@AddPackages(Integrationtest.class)
@AddPackages(ChangeDataCaptureService.class)
@AddBeanClasses(ProductTestRepository.class)
@TestMethodOrder(OrderAnnotation.class)
class ChangeDataCaptureServiceIT {

	private static String productName;
	private static String productDescription;
	private static String productDescriptionUpdated ;

	@BeforeAll
	public static void initialize() {
		UUID randomUUID = UUID.randomUUID();
		productName = "Test Product " + randomUUID;
		productDescription = "Test umbrella " + randomUUID;
		productDescriptionUpdated = productDescription + " updated";
	}

	@Inject
	ProductTestRepository productRepository;
	
	private final BlockingQueue<Change<Product>> productChanges = new BlockingArrayQueue<>();

	/**
	 * Listens to {@link Change} {@link Product} {@link Event}s and collects them.
	 * @param changedProduct {@link Change} {@link Product}
	 */
	public void onProductChanged(@Observes Change<Product> changedProduct) {
		productChanges.add(changedProduct);
	}

	/**
	 * Waits a small amount of time to assure that data capturing is ready.
	 * @throws InterruptedException
	 */
	@BeforeEach
	public void beforeEach() throws InterruptedException {
		Thread.sleep(1000);
	}

	@Disabled
	@Test
	void exploratoryManualDatabaseChangeExample() throws InterruptedException {
		Change<Product> changedProduct = productChanges.poll(30, TimeUnit.SECONDS);
		System.out.println("Received product change: " + changedProduct);
	}

	@Test
	@DisplayName("there should be no events if the products hadn't been changed")
	@Order(1)
	void noEventsIfThereAreNoChanges() throws InterruptedException {
		Change<Product> changedProduct = productChanges.poll(3, TimeUnit.SECONDS);
		assertNull(changedProduct);
	}

	@Test
	@DisplayName("inserted products should appear as change events with empty 'before' and 'after' reflecting the new state ")
	@Order(2)
	void insertedProductsShouldLeadToAChangeEvent() throws InterruptedException {
		productRepository.insertProduct(productName, productDescription);
		
		Change<Product> changedProduct = productChanges.poll(10, TimeUnit.SECONDS);
		assertNull(changedProduct.getBefore());
		assertEquals(productName, changedProduct.getAfter().getName());
		assertEquals(productDescription, changedProduct.getAfter().getDescription());
	}

	@Test
	@DisplayName("updated products should appear as change events with 'before' and 'after' reflecting the old and new state ")
	@Order(3)
	void updatedProductsShouldLeadToAChangeEvent() throws InterruptedException {
		productRepository.updateProduct(productName, productDescriptionUpdated);

		Change<Product> changedProduct = productChanges.poll(10, TimeUnit.SECONDS);
		
		assertEquals(productName, changedProduct.getBefore().getName());
		assertEquals(productName, changedProduct.getAfter().getName());
		assertEquals(productDescription, changedProduct.getBefore().getDescription());
		assertEquals(productDescriptionUpdated, changedProduct.getAfter().getDescription());
	}

	@Test
	@DisplayName("deleted products should appear as change events with empty 'after' and 'before' reflecting the old state ")
	@Order(4)
	void deletedProductsShouldLeadToAChangeEvent() throws InterruptedException {
		productRepository.deleteProduct(productName);
		
		Change<Product> changedProduct = productChanges.poll(10, TimeUnit.SECONDS);
		assertNull(changedProduct.getAfter());
		assertEquals(productName, changedProduct.getBefore().getName());
	}
}