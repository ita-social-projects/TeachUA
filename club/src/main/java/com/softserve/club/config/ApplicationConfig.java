package com.softserve.club.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.softserve.club.util.CustomRequestInterceptor;
import com.softserve.commons.util.converter.DtoConverter;
import feign.RequestInterceptor;
import org.modelmapper.ModelMapper;
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
    public RequestInterceptor customRequestInterceptor() {
        return new CustomRequestInterceptor();
    }
}
