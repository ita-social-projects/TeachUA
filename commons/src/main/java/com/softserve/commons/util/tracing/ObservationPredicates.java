package com.softserve.commons.util.tracing;

import io.micrometer.observation.ObservationPredicate;

public final class ObservationPredicates {
    private ObservationPredicates() {
    }

    public static ObservationPredicate noSpringSecurity() {
        return (name, context) -> !name.startsWith("spring.security.");
    }
}
