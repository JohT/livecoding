package org.joht.livecoding.eventdriven.address;

import java.util.HashMap;
import java.util.Map;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AddressRepository {

	private static final Address ADDRESS_JOHN_DOE = new Address("1", "John Doe", "Mainstreet 1", "Hampshire");
	private Map<String, Address> addresses = new HashMap<>();

	@PostConstruct
	void init() {
		setAddress(ADDRESS_JOHN_DOE.getId(), ADDRESS_JOHN_DOE);
	}

	/**
	 * Resets the data to its initial value for testing purposes.
	 */
	public void resetForTest() {
		init();
	}
	
	/**
	 * Reads the address by its id.
	 * 
	 * @param id {@link String}
	 * @return {@link Address}
	 */
	public Address getAddress(String id) {
		return addresses.get(id);
	}

	/**
	 * Changes the address to the new {@link Address} and returns the old
	 * {@link Address}, or <code>null</code> if it was newly created.
	 * 
	 * @param id {@link String}
	 * @param newAddress
	 * @return {@link Address}
	 */
	public Address setAddress(String id, Address newAddress) {
		return addresses.put(id, newAddress);
	}
}