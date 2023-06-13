package com.softserve.user.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.softserve.commons.util.converter.DtoConverter;
import com.softserve.commons.util.tracing.ObservationPredicates;
import com.softserve.user.util.CustomRequestInterceptor;
import feign.RequestInterceptor;
import io.micrometer.observation.ObservationRegistry;
import org.modelmapper.ModelMapper;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.observation.ServerRequestObservationContext;

@Configuration
public class ApplicationConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    @Bean
    public DtoConverter dtoConverter() {
        return new DtoConverter(modelMapper());
    }

    @Bean
    public RequestInterceptor customRequestInterceptor() {
        return new CustomRequestInterceptor();
    }

    @Bean
    ObservationRegistryCustomizer<ObservationRegistry> observationRegistryCustomizer() {
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
