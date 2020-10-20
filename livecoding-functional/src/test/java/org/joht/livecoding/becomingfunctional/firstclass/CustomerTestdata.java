package org.joht.livecoding.becomingfunctional.firstclass;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.joht.livecoding.becomingfunctional.firstclass.api.Kunde;

/**
 * Object-Mother zum Erzeugen von Test-{@link Kunde}n.
 * 
 * @author JohT
 */
enum CustomerTestdata {

	JOHN_DOE {
		@Override
		public Kunde build() {
			return Kunde.aktivMitNamen("John", "Doe");
		}
	},
	RICHARD_MILES {
		@Override
		public Kunde build() {
			return Kunde.inaktivMitNamen("Richard", "Miles");
		}
	},

	;
	public abstract Kunde build();

	public static final Collection<Kunde> alle() {
		return of(values());
	}

	public static final Collection<Kunde> of(CustomerTestdata... testdaten) {
		return Stream.of(testdaten).map(CustomerTestdata::build).collect(Collectors.toList());
	}
}
