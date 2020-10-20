package org.joht.livecoding.becomingfunctional.firstclass;

import org.joht.livecoding.becomingfunctional.firstclass.api.KundenService;

public class C_Customers_FunctionObjectTest extends AbstractCustomerServiceTest {

	@Override
	protected KundenService getKundenService() {
		return new C_Customers_FunctionObject(getTestkunden());
	}
}
