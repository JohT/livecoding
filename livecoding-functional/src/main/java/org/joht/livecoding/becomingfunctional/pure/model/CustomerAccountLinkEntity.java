package org.joht.livecoding.becomingfunctional.pure.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class CustomerAccountLinkEntity {

	private final Customernumber customernumber; // Entity ID

	private final List<AccountEntity> konten = new ArrayList<>();

	public static final CustomerAccountLinkEntity empty(Customernumber customernumber) {
		return new CustomerAccountLinkEntity(customernumber);
	}

	public CustomerAccountLinkEntity(Customernumber customernumber) {
		this.customernumber = customernumber;
	}

	public Customernumber getKundennummer() {
		return customernumber;
	}

	public List<AccountEntity> getKonten() {
		return konten;
	}

	public CustomerAccountLinkEntity verknuepfeKonto(AccountEntity konto) {
		this.konten.add(konto);
		return this;
	}

	public CustomerAccountLinkEntity verknuepfeKonten(Collection<AccountEntity> konten) {
		this.konten.addAll(konten);
		return this;
	}

	@Override
	public boolean equals(final Object other) {
		if ((other == null) || (!getClass().equals(other.getClass()))) {
			return false;
		}
		return Objects.equals(customernumber, ((CustomerAccountLinkEntity) other).customernumber);
	}

	@Override
	public int hashCode() {
		return Objects.hash(customernumber);
	}

	@Override
	public String toString() {
		return "CustomerAccountLinkEntity [customernumber=" + customernumber + ", konten=" + konten + "]";
	}
}
