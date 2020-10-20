package org.joht.livecoding.becomingfunctional.pure.c_stateless;

import java.util.function.Function;

import org.joht.livecoding.becomingfunctional.pure.model.AccountEntity;
import org.joht.livecoding.becomingfunctional.pure.model.AccountRepository;
import org.joht.livecoding.becomingfunctional.pure.model.CustomerEntity;
import org.joht.livecoding.becomingfunctional.pure.model.CustomerRepository;
import org.joht.livecoding.becomingfunctional.pure.model.Customernumber;

/**
 * Die Funktion {@link #loescheKunde(CustomerEntity, Function)} erfuellt das Kriterium "output only
 * depends on input" vollstaendig, da sie sowohl "state-less" ist, als auch ausschliesslich
 * Eingabeparameter nutzt.
 * <li>Die Funktion ist NICHT "side-effect-free", da sie AccountEntity-Objekte aendert.
 * 
 * @author JohT
 */
public class CustomerRepositoryStateless implements CustomerRepository {

	private final AccountRepository accountRepository;

	// "@Inject" (ueber Dependency Injection anbindbare Abhaengigkeit)
	public CustomerRepositoryStateless(AccountRepository accountRepository) {
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
		if (kunde.isGeloescht()) { // "state-less" Loesung um doppelte Loeschung zu verhindern.
			return;
		}
		kunde.loeschen();
		Iterable<AccountEntity> konten = kontenLeser.apply(kunde.getKundennummer());
		for (AccountEntity konto : konten) {
			if (!konto.isExtern()) {
				konto.loeschen(); // "side-effect"
			}
		}
	}

	@Override
	public String toString() {
		return "CustomerRepositoryStateless [accountRepository=" + accountRepository + "]";
	}
}