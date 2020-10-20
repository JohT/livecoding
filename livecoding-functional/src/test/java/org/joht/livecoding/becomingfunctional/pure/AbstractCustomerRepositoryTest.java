package org.joht.livecoding.becomingfunctional.pure;

import static org.joht.livecoding.becomingfunctional.pure.CustomerAccountLinkEntityTestdata.ALLE_KONTEN;
import static org.joht.livecoding.becomingfunctional.pure.CustomerAccountLinkEntityTestdata.EXTERNE_KONTEN;
import static org.joht.livecoding.becomingfunctional.pure.CustomerAccountLinkEntityTestdata.LOESCHBARE_KONTEN;
import static org.junit.Assert.assertEquals;

import org.joht.livecoding.becomingfunctional.pure.model.AccountEntity;
import org.joht.livecoding.becomingfunctional.pure.model.CustomerEntity;
import org.joht.livecoding.becomingfunctional.pure.model.CustomerAccountLinkEntity;
import org.joht.livecoding.becomingfunctional.pure.model.CustomerRepository;
import org.joht.livecoding.becomingfunctional.pure.model.Customernumber;
import org.junit.Ignore;
import org.junit.Test;

/**
 * In diesem speziellen Fall, bei dem meherere Implementierungen eines Interfaces mit den exakt
 * gleichen Testfaellen getestet werden sollen, eignet sich eine abstrakte Super-Klasse am besten.
 * Grundsaetzlich gilt aber: Composition over inheritance.
 * 
 * @author JohT
 */
@Ignore
public abstract class AbstractCustomerRepositoryTest {

	protected abstract Customernumber getKundennummer();

	protected abstract CustomerRepository getKundeRepository(CustomerAccountLinkEntity verknuepfung);

	@Test
	public void loeschbareKontenGeloescht() {
		CustomerEntity kunde = CustomerEntityTestdaten.AKTIVER_KUNDE.buildFor(getKundennummer());
		CustomerAccountLinkEntity verknuepfung = erzeugeVerknuepfung(LOESCHBARE_KONTEN);
		long erwarteteAnzahl = anzahlKontenAus(verknuepfung);

		getKundeRepository(verknuepfung).loescheKunde(kunde);

		assertEquals(erwarteteAnzahl, anzahlGeloeschterKontenAus(verknuepfung));
	}

	@Test
	public void externeKontenWerdenNichtGeloescht() {
		CustomerEntity kunde = CustomerEntityTestdaten.AKTIVER_KUNDE.buildFor(getKundennummer());
		CustomerAccountLinkEntity verknuepfung = erzeugeVerknuepfung(EXTERNE_KONTEN);
		long erwarteteAnzahl = anzahlGeloeschterKontenAus(verknuepfung);

		getKundeRepository(verknuepfung).loescheKunde(kunde);

		assertEquals(erwarteteAnzahl, anzahlGeloeschterKontenAus(verknuepfung));
	}

	@Test
	public void loeschungWirdNurEinmalDurchgefuehrt() {
		CustomerEntity kunde = CustomerEntityTestdaten.AKTIVER_KUNDE.buildFor(getKundennummer());
		CustomerAccountLinkEntity verknuepfung = erzeugeVerknuepfung(ALLE_KONTEN);
		assertEquals(0, kunde.getAnzahlAenderungen());

		CustomerRepository customerRepository = getKundeRepository(verknuepfung);
		customerRepository.loescheKunde(kunde);
		assertEquals(1, kunde.getAnzahlAenderungen());

		customerRepository.loescheKunde(kunde);
		assertEquals(1, kunde.getAnzahlAenderungen());
	}

	private long anzahlKontenAus(CustomerAccountLinkEntity verknuepfung) {
		return verknuepfung.getKonten().stream().count();
	}

	private long anzahlGeloeschterKontenAus(CustomerAccountLinkEntity verknuepfung) {
		return verknuepfung.getKonten().stream().filter(AccountEntity::isGeloescht).count();
	}

	private CustomerAccountLinkEntity erzeugeVerknuepfung(CustomerAccountLinkEntityTestdata testdaten) {
		return testdaten.buildFor(getKundennummer());
	}
}