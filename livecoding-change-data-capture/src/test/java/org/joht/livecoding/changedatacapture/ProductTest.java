package org.joht.livecoding.changedatacapture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.EqualsVerifierReport;
import nl.jqno.equalsverifier.Warning;

class ProductTest {

	private Product productUnderTest;
	
	@Test
	@DisplayName("should contain its id")
	void shouldContainId() {
		int id = 12345678;
		productUnderTest = new Product(id, "", "");
		assertEquals(id, productUnderTest.getId());
	}
	
	@Test
	@DisplayName("should contain its name")
	void shouldContainName() {
		String name = "test product name";
		productUnderTest = new Product(1, name, "");
		assertEquals(name, productUnderTest.getName());
	}
	
	@Test
	@DisplayName("should contain its description")
	void shouldContainDescription() {
		String description = "test product description";
		productUnderTest = new Product(1, "", description);
		assertEquals(description, productUnderTest.getDescription());
	}
	
	@Test
	@DisplayName("should fulfill equals and hashCode contract")
	void shouldFulfillEqualsAndHashCodeContract() {
		productUnderTest = new Product(123, "name", "description");
		EqualsVerifier.simple().forClass(Product.class).suppress(Warning.STRICT_HASHCODE).verify();
		EqualsVerifierReport report = EqualsVerifier
				.simple().forClass(Product.class).suppress(Warning.STRICT_HASHCODE).report();
		assertTrue(report.isSuccessful(), report.getMessage());
	}


}