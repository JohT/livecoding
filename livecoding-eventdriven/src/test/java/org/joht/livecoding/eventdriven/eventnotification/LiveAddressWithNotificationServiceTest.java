package org.joht.livecoding.eventdriven.eventnotification;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.joht.livecoding.eventdriven.address.Address;
import org.joht.livecoding.eventdriven.address.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

@EnableAutoWeld
class LiveAddressWithNotificationServiceTest {

	private static final String ADDRESS_ID = "1";

	@Inject
	private AddressRepository repository;
	
	@Inject
	private AddressWithNotificationService addressService;

	private Address newAddressOfInsuranceService = null;

	@BeforeEach
	public void setUp() {
		repository.resetForTest();
	}
	
	@Test
	@DisplayName("insurance should be recalculated based on the subsequently changed address")
	public void addressChangeNotificated() {
		//TODO getAddress ADDRESS_ID
		//TODO set new Address using movedToStreet
		//TODO assert newStreet equals newAddressOfInsuranceService-Street
	}

	// TODO observe event and update newAddressOfInsuranceService
}