package org.joht.livecoding.becomingfunctional.pure;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.joht.livecoding.becomingfunctional.pure.model.AccountEntity;
import org.junit.Ignore;

/**
 * Object-Mother zum Erzeugen von Test-Objekte vom Typ {@link AccountEntity}.
 * 
 * @author JohT
 */
@Ignore
enum AccountEntityTestdata {

	AKTIVES_KONTO_1 {
		@Override
		public AccountEntity build() {
			AccountEntity konto = new AccountEntity(1);
			konto.setExtern(false);
			return konto;
		}
	},
	AKTIVES_EXTERNES_KONTO_2 {
		@Override
		public AccountEntity build() {
			AccountEntity konto = new AccountEntity(2);
			konto.setExtern(true);
			return konto;
		}
	},
	GELOESCHTES_KONTO_3 {
		@Override
		public AccountEntity build() {
			AccountEntity konto = new AccountEntity(3);
			konto.setExtern(false);
			konto.loeschen();
			return konto;
		}
	},
	GELOESCHTES_EXTERNES_KONTO_4 {
		@Override
		public AccountEntity build() {
			AccountEntity konto = new AccountEntity(4);
			konto.setExtern(true);
			konto.loeschen();
			return konto;
		}
	},

	;
	public abstract AccountEntity build();

	public static final Collection<AccountEntity> alle() {
		return of(values());
	}

	public static final Collection<AccountEntity> of(AccountEntityTestdata... testdaten) {
		return Stream.of(testdaten).map(AccountEntityTestdata::build).collect(Collectors.toList());
	}
}