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
class AddressWithNotificationServiceIT {

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
		Address address = addressService.getAddress(ADDRESS_ID);
		String newStreet = "NewStreet 10b";
		addressService.setAddress(address.movedToStreet(newStreet));
		assertEquals(newStreet, newAddressOfInsuranceService.getStreet());
	}

	/**
	 * Represents a "downstream" service, that should recalculates an insurance
	 * when an address has changed.
	 * 
	 * @param event {@link AddressChangedEvent}
	 */
	public void recalculateInsurance(@Observes AddressChangedEvent event) {
		// since the event only contains minimal information about the
		// changed address (=id), the "downstream" service needs to
		// query all needed informations it needs.
		Address queriedAddress = addressService.getAddress(event.getId());
		newAddressOfInsuranceService = queriedAddress;

	}
}