package org.joht.livecoding.becomingfunctional.pure.model;

import java.util.Objects;

public class AccountEntity implements Deleteable {

	private final long kontonummer; // Entity ID

	private boolean geloescht = false;
	private boolean extern = false;

	public AccountEntity(long kontonummer) {
		this.kontonummer = kontonummer;
	}

	public long getKontonummer() {
		return kontonummer;
	}

	@Override
	public boolean isGeloescht() {
		return geloescht;
	}

	public boolean isExtern() {
		return extern;
	}

	@Override
	public void loeschen() {
		this.geloescht = true;
	}

	public void setExtern(boolean extern) {
		this.extern = extern;
	}

	@Override
	public boolean equals(final Object other) {
		if ((other == null) || (!getClass().equals(other.getClass()))) {
			return false;
		}
		return Objects.equals(kontonummer, ((AccountEntity) other).kontonummer);
	}

	@Override
	public int hashCode() {
		return Objects.hash(kontonummer);
	}

	@Override
	public String toString() {
		return "AccountEntity [kontonummer=" + kontonummer + ", geloescht=" + geloescht + ", extern=" + extern + "]";
	}
}
