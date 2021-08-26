package org.joht.livecoding.changedatacapture.integrationtest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.joht.livecoding.changedatacapture.integrationtest.infrastructure.DatabaseConnection;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class)
class ProductTestRepositoryIT {

	private static String productName;
	
	/**
	 * repository under test
	 */
	ProductTestRepository productRepository = new ProductTestRepository(DatabaseConnection.localPostgreSQL());

	private static String getProductName() {
		return ProductTestRepositoryIT.productName;
	}
	
	@BeforeAll
	public static void initialize() {
		productName = "Test Product " + UUID.randomUUID();
	}
	
	@Test
	@DisplayName("the product should be present after it had been inserted")
	@Order(1)
	void anInsertedProductShouldBeSelectable() {
		String expectedDescription = "Product for software testing purposes";
		productRepository.insertProduct(getProductName(), expectedDescription);
		assertEquals(expectedDescription, productRepository.getProductDescription(getProductName()));
	}
	
	@Test
	@DisplayName("the product description should be changed after it had been updated")
	@Order(2)
	void anUpdatedProductShouldBeSelectable() {
		String expectedDescription = "Product for software testing purposes updated";
		productRepository.updateProduct(getProductName(), expectedDescription);
		assertEquals(expectedDescription, productRepository.getProductDescription(getProductName()));
	}
	
	@Test
	@DisplayName("the product should be gone after it had been deleted")
	@Order(3)
	void anDeletedProductShouldBeGone() {
		productRepository.deleteProduct(getProductName());
		assertEquals("", productRepository.getProductDescription(getProductName()));
	}
}