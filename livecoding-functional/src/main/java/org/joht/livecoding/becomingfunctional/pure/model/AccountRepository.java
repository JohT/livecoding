package org.joht.livecoding.becomingfunctional.pure.model;

import java.util.List;

public interface AccountRepository {

	/**
	 * Liefert die zum angegebenen Kunden vernuepften Konten.
	 * 
	 * @param customernumber {@link Customernumber}
	 * @return {@link List}e mit Elemente vom Typ {@link AccountEntity}.
	 */
	List<AccountEntity> getKundenKonten(Customernumber customernumber);
}
