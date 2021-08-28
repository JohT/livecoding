package org.joht.livecoding.eventsourcing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.joht.livecoding.eventsourcing.CdiProducerTypedInterfaceProducer.TypedInterface;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

// TODO delete, if generic type producer for aggregate repositories works
@Disabled
@EnableAutoWeld
@AddBeanClasses(CdiProducerTypedInterfaceProducer.class)
public class CdiProducerWithTypeParameterTest {
	
	@Inject
	TypedInterface<String> typedInterfaceOfString;
	
	@Test
	void typedInterfaceOfStringShouldBePresent() {
		assertNotNull(typedInterfaceOfString);
		assertEquals("String", typedInterfaceOfString.getName());
		assertEquals(String.class, typedInterfaceOfString.getType());
	}	
}