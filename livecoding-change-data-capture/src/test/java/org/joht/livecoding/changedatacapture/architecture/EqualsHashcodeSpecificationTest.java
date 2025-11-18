package org.joht.livecoding.changedatacapture.architecture;

import org.joht.livecoding.changedatacapture.Change;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

import nl.jqno.equalsverifier.api.ConfiguredEqualsVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

/**
 * This test verifies equal- and hashCode-Method implementations.
 */
public class EqualsHashcodeSpecificationTest {

	private static JavaClasses classes = new ClassFileImporter()
			.withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
			.importPackages(Change.class.getPackage().getName());

	@Test
	@DisplayName("value objects are only considered equal, if all of their fields are equal")
	public void valueObjectsAreOnlyConsideredEqualIfAllOfTheirFieldsAreEqual() {
		ConfiguredEqualsVerifier verifier = EqualsVerifier.configure()
				.usingGetClass()
				.suppress(Warning.STRICT_HASHCODE);
		ArchRuleDefinition.methods().that()
				.haveName("hashCode").or().haveName("equals")
				.should(FulfillEqualsAndHashcodeContract.configuredBy(verifier))
				.check(classes);
	}
}