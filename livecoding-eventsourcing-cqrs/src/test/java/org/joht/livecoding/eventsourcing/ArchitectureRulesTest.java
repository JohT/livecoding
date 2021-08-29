package org.joht.livecoding.eventsourcing;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.codeUnits;
import static nl.jqno.equalsverifier.Warning.STRICT_HASHCODE;
import static nl.jqno.equalsverifier.Warning.SURROGATE_KEY;

import org.axonframework.modelling.command.AggregateRoot;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaCodeUnit;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

import nl.jqno.equalsverifier.ConfiguredEqualsVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.EqualsVerifierReport;
import nl.jqno.equalsverifier.Warning;

/**
 * Since this demo module contains all elements of an event-sourced system only separated by packages, <br>
 * this tests assures, that there are no unwanted dependencies e.g. between command and query side.
 * <p>
 * Additionally, this demo also shows how to separate the business (domain/command and query) models, <br>
 * that will contain the most important parts of a real system including all the business logic,<br>
 * from the framework used for messaging (axon). <br>
 * It should always be possible, to easily update to any newer version of a library, without changing business code.<br>
 * If the heart of the application directly depends on these libraries, every change may be a risk,<br>
 * especially if the library introduces "breaking changes". <br>
 * These may be important to get the library a step forward and should not need to be considered as "dangerous" or lead to great afford.
 */
class ArchitectureRulesTest {

    private static JavaClasses classes;

    @BeforeAll
    public static void setup() {
        classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(Product.class.getPackage().getName());
    }

    @Test
    @DisplayName("equals and hashCode methods in classes except aggregates fulfill the contract")
    void testEqualsAndHashcodeMethodsFulfillTheContract() {
        // TODO Aggregates need a special treatment, since their key does not consist of every field
        codeUnits().that()
                .haveName("hashCode").or().haveName("equals")
                .and().areDeclaredInClassesThat().areNotMetaAnnotatedWith(AggregateRoot.class)
                .should(FulfillEqualsHashcodeContract.suppress(STRICT_HASHCODE, SURROGATE_KEY)).check(classes);
    }

    private static class FulfillEqualsHashcodeContract extends ArchCondition<JavaCodeUnit> {

        private final ConfiguredEqualsVerifier verifier;

        public static final ArchCondition<JavaCodeUnit> suppress(Warning... warnings) {
            ConfiguredEqualsVerifier verifier = EqualsVerifier.configure().usingGetClass().suppress(warnings);
            return FulfillEqualsHashcodeContract.using(verifier);
        }

        public static final ArchCondition<JavaCodeUnit> using(ConfiguredEqualsVerifier verifier) {
            return new FulfillEqualsHashcodeContract(verifier);
        }

        private FulfillEqualsHashcodeContract(ConfiguredEqualsVerifier verifier) {
            super("fulfill the equals and hashCode contract");
            this.verifier = verifier;
        }

        @Override
        public void check(JavaCodeUnit codeUnit, ConditionEvents events) {
            Class<?> type = getClassOfMethodOwner(codeUnit);
            EqualsVerifierReport report = verifier.forClass(type).report();
            events.add(new SimpleConditionEvent(codeUnit.getOwner(), report.isSuccessful(), report.getMessage()));
        }

        private Class<?> getClassOfMethodOwner(JavaCodeUnit codeUnit) {
            try {
                return Class.forName(codeUnit.getOwner().getName());
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}