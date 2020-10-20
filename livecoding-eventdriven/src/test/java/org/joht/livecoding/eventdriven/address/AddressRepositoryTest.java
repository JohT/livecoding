package org.joht.livecoding.eventdriven.address;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AddressRepositoryTest {

	private AddressRepository repositoryUnderTest = new AddressRepository();
	
	@BeforeEach
	void setUp() {
		repositoryUnderTest.init();
	}
	
	@Test
	void containsTestAddressWithId1() {
		String id = "1";
		Address address = repositoryUnderTest.getAddress(id);
		assertEquals(id, address.getId());
	}

	@Test
	void containsTheNewAddressAfterChangingIt() {
		String id = "1";
		String expectedNewStreet = "New Street 123";
		Address address = repositoryUnderTest.getAddress(id);
		Address newAddress = address.movedToStreet(expectedNewStreet);
		repositoryUnderTest.setAddress(id, newAddress);
		
		assertEquals(newAddress, repositoryUnderTest.getAddress(id));
		assertEquals(expectedNewStreet, newAddress.getStreet());
	}
	
	@Test
	void shouldReturnTheOldAddressWhenSettingTheNewOne() {
		String id = "1";
		Address address = repositoryUnderTest.getAddress(id);
		Address newAddress = address.movedToStreet("Any Street 3");
		Address oldAddress = repositoryUnderTest.setAddress(id, newAddress);
		assertEquals(address, oldAddress);
	}
}