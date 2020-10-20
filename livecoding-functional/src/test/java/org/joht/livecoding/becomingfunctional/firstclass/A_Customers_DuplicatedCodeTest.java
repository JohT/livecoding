package org.joht.livecoding.becomingfunctional.firstclass;

import org.joht.livecoding.becomingfunctional.firstclass.api.KundenService;

public class A_Customers_DuplicatedCodeTest extends AbstractCustomerServiceTest {

	@Override
	protected KundenService getKundenService() {
		return new A_Customers_DuplicatedCode(getTestkunden());
	}
}