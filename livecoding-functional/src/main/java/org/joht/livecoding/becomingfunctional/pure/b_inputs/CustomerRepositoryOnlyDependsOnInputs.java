package org.joht.livecoding.becomingfunctional.pure.b_inputs;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.joht.livecoding.becomingfunctional.pure.model.AccountEntity;
import org.joht.livecoding.becomingfunctional.pure.model.AccountRepository;
import org.joht.livecoding.becomingfunctional.pure.model.CustomerEntity;
import org.joht.livecoding.becomingfunctional.pure.model.CustomerRepository;
import org.joht.livecoding.becomingfunctional.pure.model.Customernumber;

/**
 * Die Funktion {@link #loescheKunde(CustomerEntity, Function)} erfuellt nur teilweise das Kriterium
 * "output only depends on input".
 * <li>Die Funktion ist NICHT "state-less". Das Verhalten beim Folgeaufruf ist anders.
 * <li>Die Funktion ist NICHT "side-effect-free", da sie AccountEntity-Objekte aendert.
 * 
 * @author JohT
 */
public class CustomerRepositoryOnlyDependsOnInputs implements CustomerRepository {

	private final List<Long> bereitsGeloeschteKunden = new ArrayList<>(); // "state"

	private final AccountRepository accountRepository;

	// "@Inject" (ueber Dependency Injection anbindbare Abhaengigkeit)
	public CustomerRepositoryOnlyDependsOnInputs(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Diese Methode dient als Fassade, um die Anbindung der Funktion zu demonstrieren.
	 */
	@Override
	public void loescheKunde(CustomerEntity kunde) {
		loescheKunde(kunde, accountRepository::getKundenKonten);
	}

	void loescheKunde(CustomerEntity kunde, Function<Customernumber, Iterable<AccountEntity>> kontenLeser) {
		Long kundennummer = kunde.getKundennummer().asLong();
		if (bereitsGeloeschteKunden.contains(kundennummer)) {
			return;
		}
		kunde.loeschen();
		Iterable<AccountEntity> konten = kontenLeser.apply(kunde.getKundennummer());
		for (AccountEntity konto : konten) {
			if (!konto.isExtern()) {
				konto.loeschen(); // "side-effect"
			}
		}
		bereitsGeloeschteKunden.add(kundennummer);
	}

	@Override
	public String toString() {
		return "CustomerRepositoryOnlyDependsOnInputs [bereitsGeloeschteKunden=" + bereitsGeloeschteKunden
				+ ", accountRepository=" + accountRepository + "]";
	}
}