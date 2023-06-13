package com.softserve.club.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.softserve.commons.util.converter.DtoConverter;
import com.softserve.commons.util.tracing.ObservationPredicates;
import io.micrometer.observation.ObservationRegistry;
import org.modelmapper.ModelMapper;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    ObservationRegistryCustomizer<ObservationRegistry> observationRegistryCustomizer() {
        return registry -> registry.observationConfig()
                .observationPredicate(ObservationPredicates.noSpringSecurity());
    }
}
