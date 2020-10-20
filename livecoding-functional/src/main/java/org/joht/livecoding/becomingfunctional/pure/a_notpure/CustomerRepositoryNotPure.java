package org.joht.livecoding.becomingfunctional.pure.a_notpure;

import java.util.ArrayList;
import java.util.List;

import org.joht.livecoding.becomingfunctional.pure.model.AccountDatabaseRepository;
import org.joht.livecoding.becomingfunctional.pure.model.AccountEntity;
import org.joht.livecoding.becomingfunctional.pure.model.CustomerEntity;
import org.joht.livecoding.becomingfunctional.pure.model.CustomerRepository;

/**
 * Die Funktion {@link #loescheKunde(CustomerEntity)} erfuellt keine der Kriterien fuer "pure
 * functions":
 * <li>Das Verhalten der Funktion ist nich einzig von den Eingabeparametern abhaengig.
 * <li>Die Funktion ist NICHT "state-less". Das Verhalten beim Folgeaufruf ist anders.
 * <li>Die Funktion ist NICHT "side-effect-free", da sie AccountEntity-Objekte aendert.
 * 
 * @author JohT
 */
public class CustomerRepositoryNotPure implements CustomerRepository {

	private final List<Long> bereitsGeloeschteKunden = new ArrayList<>(); // "state"

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void loescheKunde(CustomerEntity kunde) {
		Long kundennummer = kunde.getKundennummer().asLong();
		if (bereitsGeloeschteKunden.contains(kundennummer)) {
			return;
		}
		kunde.loeschen();
		// Externer Eingabeparameter (Liste von Konten) ausserhalb der Methoden-Eingabeparametern
		// "external/back-door dependency"
		List<AccountEntity> konten = new AccountDatabaseRepository().getKundenKonten(kunde.getKundennummer());
		for (AccountEntity konto : konten) {
			if (!konto.isExtern()) {
				konto.loeschen(); // "side-effect"
			}
		}
		bereitsGeloeschteKunden.add(kundennummer);
	}

	@Override
	public String toString() {
		return "CustomerRepositoryNotPure [bereitsGeloeschteKunden=" + bereitsGeloeschteKunden + "]";
	}
}