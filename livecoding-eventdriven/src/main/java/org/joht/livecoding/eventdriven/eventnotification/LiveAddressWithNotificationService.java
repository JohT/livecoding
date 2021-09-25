package org.joht.livecoding.eventdriven.eventnotification;

import org.joht.livecoding.eventdriven.address.Address;
import org.joht.livecoding.eventdriven.address.AddressRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

@ApplicationScoped
public class LiveAddressWithNotificationService {

	@Inject
	private AddressRepository repository;
	
	@Inject
	private Event<AddressChangedEvent> notifyEvent;
	
	public Address getAddress(String id) {
		return repository.getAddress(id);
	}
	
	public void setAddress(Address newAddress) {
		//TODO call repository set
		//TODO fire event when old address exists
	}
}