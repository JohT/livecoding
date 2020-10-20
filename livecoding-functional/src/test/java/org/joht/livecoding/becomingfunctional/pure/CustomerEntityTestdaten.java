package org.joht.livecoding.becomingfunctional.pure;

import org.joht.livecoding.becomingfunctional.pure.model.CustomerEntity;
import org.joht.livecoding.becomingfunctional.pure.model.CustomerAccountLinkEntity;
import org.joht.livecoding.becomingfunctional.pure.model.Customernumber;
import org.junit.Ignore;

/**
 * Object-Mother zum Erzeugen von Test-Objekte vom Typ {@link CustomerEntity} und
 * {@link CustomerAccountLinkEntity}.
 * 
 * @author JohT
 */
@Ignore
enum CustomerEntityTestdaten {

	AKTIVER_KUNDE {
		@Override
		public CustomerEntity buildFor(Customernumber customernumber) {
			return new CustomerEntity(customernumber);
		}
	},
	GELOESCHTER_KUNDE_2 {
		@Override
		public CustomerEntity buildFor(Customernumber customernumber) {
			CustomerEntity customerEntity = new CustomerEntity(customernumber);
			customerEntity.loeschen();
			return customerEntity;
		}
	},

	;
	public abstract CustomerEntity buildFor(Customernumber customernumber);
}