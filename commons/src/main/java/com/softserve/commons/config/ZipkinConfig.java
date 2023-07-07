package com.softserve.commons.config;

import com.softserve.commons.util.tracing.ObservationPredicates;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationRegistryCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "management.tracing.sampling.probability", havingValue = "1.0")
public class ZipkinConfig {
    @Bean
    @ConditionalOnMissingBean(ObservationRegistryCustomizer.class)
    public ObservationRegistryCustomizer<ObservationRegistry> observationRegistryCustomizer() {
        return registry -> registry.observationConfig()
                .observationPredicate(ObservationPredicates.noSpringSecurity());
    }
}
