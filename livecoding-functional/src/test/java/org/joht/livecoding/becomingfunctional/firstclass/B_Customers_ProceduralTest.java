package org.joht.livecoding.becomingfunctional.firstclass;

import org.joht.livecoding.becomingfunctional.firstclass.api.KundenService;

public class B_Customers_ProceduralTest extends AbstractCustomerServiceTest {

	@Override
	protected KundenService getKundenService() {
		return new B_Customers_Procedural(getTestkunden());
	}
}
