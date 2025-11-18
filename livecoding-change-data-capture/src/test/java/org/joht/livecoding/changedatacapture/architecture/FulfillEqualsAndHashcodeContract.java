package org.joht.livecoding.changedatacapture.architecture;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaCodeUnit;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

import nl.jqno.equalsverifier.api.ConfiguredEqualsVerifier;
import nl.jqno.equalsverifier.EqualsVerifierReport;

/**
 * Link between "ArchUnit" and "EqualsVerifier".
 */
class FulfillEqualsAndHashcodeContract extends ArchCondition<JavaCodeUnit> {

    private final ConfiguredEqualsVerifier config;

    public static final ArchCondition<JavaCodeUnit> configuredBy(ConfiguredEqualsVerifier config) {
        return new FulfillEqualsAndHashcodeContract(config);
    }

    private FulfillEqualsAndHashcodeContract(ConfiguredEqualsVerifier config) {
        super("fulfills the equals and hashCode contract");
        this.config = config;
    }

    @Override
    public void check(JavaCodeUnit codeUnit, ConditionEvents events) {
        Class<?> classToTest = classForName(codeUnit.getOwner().getName());
        EqualsVerifierReport report = config.forClass(classToTest).report();
        events.add(eventFor(report, codeUnit.getOwner()));
    }

    private static SimpleConditionEvent eventFor(EqualsVerifierReport report, JavaClass owner) {
        return new SimpleConditionEvent(owner, report.isSuccessful(), report.getMessage());
    }

    private static Class<?> classForName(String classname) {
        try {
            return Class.forName(classname);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String toString() {
        return "FulfillEqualsAndHashcodeContract [config=" + config + "]";
    }
}