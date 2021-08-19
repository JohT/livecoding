package org.joht.livecoding.changedatacapture.integrationtest;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.util.BlockingArrayQueue;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.jboss.weld.junit5.auto.ExcludeBeanClasses;
import org.joht.livecoding.changedatacapture.Change;
import org.joht.livecoding.changedatacapture.ChangeDataCaptureService;
import org.joht.livecoding.changedatacapture.Product;
import org.joht.livecoding.changedatacapture.integrationtest.infrastructure.Integrationtest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@EnableAutoWeld
@AddPackages(Integrationtest.class)
@AddPackages(ChangeDataCaptureService.class)
@ExcludeBeanClasses(ChangeDataCaptureService.class)
@AddBeanClasses(ProductTestRepository.class)
@TestMethodOrder(OrderAnnotation.class)
class ChangeDataCaptureExampleServiceIT {

	private static String uniqueId = UUID.randomUUID().toString();
	private static String productName = "Test Product " + uniqueId;
	private static String productDescription = "Test Product " + uniqueId;
	private static String productDescriptionUpdated = productDescription + " updated";
	
	private final BlockingQueue<Change<Product>> productChanges = new BlockingArrayQueue<>();

	// Livecoding:
	// Reference: ChangeDataCaptureServiceIT
	// TODO Use "exploratoryManualDatabaseChangeExample" as a first step to manually display data changes
	// TODO Observe product changes and collect them in a list
	// TODO Insert, Update, Delete products and assert, if these changes appear in the list of changes

	/**
	 * Waits a small amount of time to assure that data capturing is ready.
	 * @throws InterruptedException
	 */
	@BeforeEach
	public void beforeEach() throws InterruptedException {
		Thread.sleep(1000);
	}
	
	@Test
	void exploratoryManualDatabaseChangeExample() throws InterruptedException {
		productChanges.poll(20, TimeUnit.SECONDS);
	}

	@Disabled
	@Test
	@DisplayName("there should be no events if the products hadn't been changed")
	@Order(1)
	void noEventsIfThereAreNoChanges() throws InterruptedException {
		//TODO implement and then remove @Disabled
	}

	@Disabled
	@Test
	@DisplayName("inserted products should appear as change events with empty 'before' and 'after' reflecting the new state ")
	@Order(2)
	void insertedProductsShouldLeadToAChangeEvent() throws InterruptedException {
		//TODO implement and then remove @Disabled
	}

	@Disabled
	@Test
	@DisplayName("updated products should appear as change events with 'before' and 'after' reflecting the old and new state ")
	@Order(3)
	void updatedProductsShouldLeadToAChangeEvent() throws InterruptedException {
		//TODO implement and then remove @Disabled
	}

	@Disabled
	@Test
	@DisplayName("deleted products should appear as change events with empty 'after' and 'before' reflecting the old state ")
	@Order(4)
	void deletedProductsShouldLeadToAChangeEvent() throws InterruptedException {
		//TODO implement and then remove @Disabled
	}

}