package org.joht.livecoding.eventsourcing.infrastructure.axon.serializer.jsonb;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import jakarta.json.bind.config.PropertyVisibilityStrategy;

enum VisibilityStrategies implements PropertyVisibilityStrategy {
    FIELD_VISIBILITY {
        @Override
        public boolean isVisible(Field field) {
            return true;
        }

        @Override
        public boolean isVisible(Method method) {
            return false;
        }
    },

    ;
}
