package org.joht.livecoding.becomingfunctional.pure.d_pure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.joht.livecoding.becomingfunctional.pure.model.AccountEntity;
import org.joht.livecoding.becomingfunctional.pure.model.AccountRepository;
import org.joht.livecoding.becomingfunctional.pure.model.CustomerEntity;
import org.joht.livecoding.becomingfunctional.pure.model.CustomerRepository;
import org.joht.livecoding.becomingfunctional.pure.model.Customernumber;
import org.joht.livecoding.becomingfunctional.pure.model.Deleteable;

/**
 * Die Funktion {@link #zuLoeschendeElemente(CustomerEntity, Function)} erfuellt das Kriterium "output
 * only depends on input" vollstaendig, da sie sowohl "state-less" ist, als auch ausschliesslich die
 * Eingabeparameter nutzt.
 * 
 * @author JohT
 */
public class CustomerRepositoryPure implements CustomerRepository {

	private final AccountRepository accountRepository;

	// "@Inject" (ueber Dependency Injection anbindbare Abhaengigkeit)
	public CustomerRepositoryPure(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Diese Methode dient als Fassade, um die Anbindung der Funktion zu demonstrieren.
	 */
	@Override
	public void loescheKunde(CustomerEntity kunde) {
		zuLoeschendeElemente(kunde, accountRepository::getKundenKonten).forEach(Deleteable::loeschen);
	}

	List<Deleteable> zuLoeschendeElemente(CustomerEntity kunde, Function<Customernumber, Iterable<AccountEntity>> kontenLeser) {
		if (kunde.isGeloescht()) {
			return Collections.emptyList();
		}
		List<Deleteable> zuLoeschendeElemente = new ArrayList<>();
		zuLoeschendeElemente.add(kunde);
		for (AccountEntity konto : kontenLeser.apply(kunde.getKundennummer())) {
			if (!konto.isExtern()) {
				zuLoeschendeElemente.add(konto);
			}
		}
		return zuLoeschendeElemente;
	}

	@Override
	public String toString() {
		return "CustomerRepositoryPure [accountRepository=" + accountRepository + "]";
	}
}