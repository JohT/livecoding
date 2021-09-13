package org.joht.livecoding.eventdriven.eventstatetransfer;

import org.joht.livecoding.eventdriven.address.Address;
import org.joht.livecoding.eventdriven.address.AddressRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

@ApplicationScoped
public class AddressWithStateTransferService {

	@Inject
	private AddressRepository repository;
		
	@Inject
	private Event<AddressStateChangedEvent> stateEvent;
	
	public Address getAddress(String id) {
		return repository.getAddress(id);
	}
	
	public void setAddress(Address newAddress) {
		String id = newAddress.getId();
		Address oldAddress = repository.setAddress(id, newAddress);
		if (oldAddress != null) {
			stateEvent.fire(new AddressStateChangedEvent(oldAddress, newAddress));
		}
	}
}