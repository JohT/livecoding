package org.joht.livecoding.eventsourcing.genericproducer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.joht.livecoding.eventsourcing.genericproducer.CdiProducerWithTypeParameter.TypedInterface;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

@EnableAutoWeld
@AddBeanClasses(CdiProducerWithTypeParameter.class)
class CdiProducerWithTypeParameterIT {
	
	@Inject
	TypedInterface<String> typedInterfaceOfString;
	
	@Test
	void typedInterfaceOfStringShouldBePresent() {
		assertNotNull(typedInterfaceOfString);
		assertEquals("String", typedInterfaceOfString.getName());
		assertEquals(String.class, typedInterfaceOfString.getType());
	}	
}