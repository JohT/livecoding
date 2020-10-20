package org.joht.livecoding.becomingfunctional.pure;

import java.util.Collections;
import java.util.List;

import org.joht.livecoding.becomingfunctional.pure.model.AccountEntity;
import org.joht.livecoding.becomingfunctional.pure.model.AccountRepository;
import org.joht.livecoding.becomingfunctional.pure.model.CustomerAccountLinkEntity;
import org.joht.livecoding.becomingfunctional.pure.model.Customernumber;
import org.junit.Ignore;

@Ignore
public class AccountTestRespository implements AccountRepository {

	private final CustomerAccountLinkEntity verknuepfung;

	public AccountTestRespository(CustomerAccountLinkEntity verknuepfung) {
		this.verknuepfung = verknuepfung;
	}

	@Override
	public List<AccountEntity> getKundenKonten(Customernumber customernumber) {
		return verknuepfung.getKundennummer().equals(customernumber) ? verknuepfung.getKonten() : Collections.emptyList();
	}

	@Override
	public String toString() {
		return "AccountTestRespository [verknuepfung=" + verknuepfung + "]";
	}
}
