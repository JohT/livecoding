package org.joht.livecoding.becomingfunctional.pure.model;

import java.util.Objects;

public class CustomerEntity implements Deleteable {

	private final Customernumber customernumber; // Entity ID
	private boolean geloescht = false;
	private long anzahlAenderungen = 0;

	public CustomerEntity(Customernumber customernumber) {
		this.customernumber = customernumber;
	}

	public Customernumber getKundennummer() {
		return customernumber;
	}

	@Override
	public boolean isGeloescht() {
		return geloescht;
	}

	@Override
	public void loeschen() {
		this.geloescht = true;
		this.anzahlAenderungen++;
	}

	/**
	 * Nur zu Testzwecken.
	 * 
	 * @return
	 */
	public long getAnzahlAenderungen() {
		return anzahlAenderungen;
	}

	@Override
	public boolean equals(final Object other) {
		if ((other == null) || (!getClass().equals(other.getClass()))) {
			return false;
		}
		return Objects.equals(customernumber, ((CustomerEntity) other).customernumber);
	}

	@Override
	public int hashCode() {
		return Objects.hash(customernumber);
	}

	@Override
	public String toString() {
		return "CustomerEntity [customernumber=" + customernumber + ", geloescht=" + geloescht + ", anzahlAenderungen="
				+ anzahlAenderungen + "]";
	}
}