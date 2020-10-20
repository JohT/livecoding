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
enum CustomerAccountLinkEntityTestdata {

	ALLE_KONTEN {
		@Override
		public CustomerAccountLinkEntity buildFor(Customernumber customernumber) {
			CustomerAccountLinkEntity verknuepfung = new CustomerAccountLinkEntity(customernumber);
			verknuepfung.verknuepfeKonten(AccountEntityTestdata.alle());
			return verknuepfung;
		}
	},
	LOESCHBARE_KONTEN {
		@Override
		public CustomerAccountLinkEntity buildFor(Customernumber customernumber) {
			CustomerAccountLinkEntity verknuepfung = new CustomerAccountLinkEntity(customernumber);
			verknuepfung.verknuepfeKonto(AccountEntityTestdata.AKTIVES_KONTO_1.build());
			return verknuepfung;
		}
	},
	EXTERNE_KONTEN {
		@Override
		public CustomerAccountLinkEntity buildFor(Customernumber customernumber) {
			CustomerAccountLinkEntity verknuepfung = new CustomerAccountLinkEntity(customernumber);
			verknuepfung.verknuepfeKonto(AccountEntityTestdata.AKTIVES_EXTERNES_KONTO_2.build());
			verknuepfung.verknuepfeKonto(AccountEntityTestdata.GELOESCHTES_EXTERNES_KONTO_4.build());
			return verknuepfung;
		}
	},
	NICHT_LOESCHBARE_KONTEN {
		@Override
		public CustomerAccountLinkEntity buildFor(Customernumber customernumber) {
			CustomerAccountLinkEntity verknuepfung = new CustomerAccountLinkEntity(customernumber);
			verknuepfung.verknuepfeKonto(AccountEntityTestdata.AKTIVES_EXTERNES_KONTO_2.build());
			verknuepfung.verknuepfeKonto(AccountEntityTestdata.GELOESCHTES_KONTO_3.build());
			verknuepfung.verknuepfeKonto(AccountEntityTestdata.GELOESCHTES_EXTERNES_KONTO_4.build());
			return verknuepfung;
		}
	},

	;
	public abstract CustomerAccountLinkEntity buildFor(Customernumber customernumber);
}