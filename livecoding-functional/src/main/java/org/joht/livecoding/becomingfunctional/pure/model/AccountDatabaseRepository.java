package org.joht.livecoding.becomingfunctional.pure.model;

import java.util.Collections;
import java.util.List;

public class AccountDatabaseRepository implements AccountRepository {

	// Stellvertretend fuer die Datenbank
	private static AccountRepository testrepository = null;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<AccountEntity> getKundenKonten(Customernumber customernumber) {
		return (testrepository != null) ? testrepository.getKundenKonten(customernumber) : Collections.emptyList();
	}

	/**
	 * Nur fuer Testzwecke und wenn {@link AccountDatabaseRepository} nicht unmittlebar austauschbar
	 * ist.<br>
	 * Diese Methode muss nur dann verwendet werden, wenn im Design austauschbare Abhaengigkeiten nicht
	 * beruecksichtigt wurden.
	 * <p>
	 * Ermoeglicht es, einzelne {@link CustomerAccountLinkEntity} zu Testzwecken zu hinterlegen.
	 * 
	 * @param repository {@link AccountRepository}
	 * @deprecated {@link AccountRepository} sollte austauschbar sein.
	 */
	@Deprecated
	public static void setTestRepository(AccountRepository repository) {
		AccountDatabaseRepository.testrepository = repository;
	}
}
