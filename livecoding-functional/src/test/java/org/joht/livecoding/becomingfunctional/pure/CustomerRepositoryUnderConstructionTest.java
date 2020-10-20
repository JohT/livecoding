package org.joht.livecoding.becomingfunctional.pure;

import org.joht.livecoding.becomingfunctional.pure.model.AccountDatabaseRepository;
import org.joht.livecoding.becomingfunctional.pure.model.CustomerAccountLinkEntity;
import org.joht.livecoding.becomingfunctional.pure.model.CustomerRepository;
import org.joht.livecoding.becomingfunctional.pure.model.Customernumber;

public class CustomerRepositoryUnderConstructionTest extends AbstractCustomerRepositoryTest {

	private static final Customernumber KUNDENNUMMER = Customernumber.of(123_456_789_01L);

	@Override
	protected CustomerRepository getKundeRepository(CustomerAccountLinkEntity verknuepfung) {
		AccountTestRespository repository = new AccountTestRespository(verknuepfung);
		CustomerRepository repositoryToTest = new CustomerRepositoryUnderConstruction();
		AccountDatabaseRepository.setTestRepository(repository);
		return repositoryToTest;
	}

	@Override
	protected Customernumber getKundennummer() {
		return KUNDENNUMMER;
	}
}
