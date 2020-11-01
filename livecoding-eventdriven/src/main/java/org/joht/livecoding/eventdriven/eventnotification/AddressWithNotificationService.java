package org.joht.livecoding.eventdriven.eventnotification;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.joht.livecoding.eventdriven.address.Address;
import org.joht.livecoding.eventdriven.address.AddressRepository;

@ApplicationScoped
public class AddressWithNotificationService {

	@Inject
	private AddressRepository repository;
	
	public Address getAddress(String id) {
		return repository.getAddress(id);
	}
	
	public void setAddress(Address newAddress) {
		//TODO call repository set
		//TODO fire event when old address exists
	}
}