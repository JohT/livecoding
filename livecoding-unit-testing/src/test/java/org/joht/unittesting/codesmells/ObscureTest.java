package org.joht.unittesting.codesmells;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ObscureTest {

	@Test
	void testFlightMileage_asKm2() {
		String validFlightNumber = "HR 1234";
		// set up fixture
		// exercise constructor
		Flight newFlight = Flight.ofFlightNumber(validFlightNumber);
		// verify constructed object
		assertEquals(validFlightNumber, newFlight.getFlightNumber());
		assertEquals(1234, newFlight.getNumber());
		assertEquals("HR", newFlight.getAirlineCode());
		assertNull(newFlight.getAirline());
		// set up mileage
		newFlight = newFlight.withMileage(1122);
		// exercise mileage translator
		int actualKilometres = newFlight.getMileageAsKm();
		// verify results
		int expectedKilometres = 1806;
		assertEquals(expectedKilometres, actualKilometres);
		// now try it with a canceled flight
		newFlight = newFlight.cancel();
		try {
			newFlight.getMileageAsKm();
			fail("Expected exception");
		} catch (IllegalArgumentException e) {
			assertEquals("Cannot get cancelled flight mileage", e.getMessage());
		}
	}

}
