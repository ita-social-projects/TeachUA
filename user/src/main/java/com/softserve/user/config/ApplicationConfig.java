package com.softserve.user.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.softserve.commons.util.tracing.ObservationPredicates;
import com.softserve.user.util.CustomRequestInterceptor;
import feign.RequestInterceptor;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.observation.ServerRequestObservationContext;

@Configuration
public class ApplicationConfig {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    @Bean
    public RequestInterceptor customRequestInterceptor() {
        return new CustomRequestInterceptor();
    }

    @Bean
    public ObservationRegistryCustomizer<ObservationRegistry> observationRegistryCustomizer() {
        return registry -> registry.observationConfig()
                .observationPredicate(ObservationPredicates.noSpringSecurity())
                .observationPredicate((name, context) -> {
                    if (context instanceof ServerRequestObservationContext srCtx) {
                        return !srCtx.getCarrier().getRequestURI().startsWith("/api/v1/jwt");
                    }
                    return true;
                });
    }
}
