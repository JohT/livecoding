package org.joht.livecoding.becomingfunctional.pure.model;

@FunctionalInterface
public interface CustomerRepository {

	/**
	 * Markiert den angegebenen Kunden und all seine bei uns gefuehrten Geschaefte als geloescht.
	 * 
	 * @param kunde {@link CustomerEntity}
	 */
	void loescheKunde(CustomerEntity kunde);
}
