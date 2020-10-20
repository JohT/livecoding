package org.joht.livecoding.becomingfunctional.firstclass;

import org.joht.livecoding.becomingfunctional.firstclass.api.KundenService;

public class E_Customers_DuplicatedLamndaRegressionTest extends AbstractCustomerServiceTest {

	@Override
	protected KundenService getKundenService() {
		return new E_Customers_DuplicatedLamndaRegression(getTestkunden());
	}
}
