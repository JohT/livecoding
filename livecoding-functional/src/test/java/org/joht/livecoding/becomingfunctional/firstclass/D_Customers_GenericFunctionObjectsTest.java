package org.joht.livecoding.becomingfunctional.firstclass;

import org.joht.livecoding.becomingfunctional.firstclass.api.KundenService;

public class D_Customers_GenericFunctionObjectsTest extends AbstractCustomerServiceTest {

	@Override
	protected KundenService getKundenService() {
		return new D_Customers_GenericFunctionObjects(getTestkunden());
	}
}
