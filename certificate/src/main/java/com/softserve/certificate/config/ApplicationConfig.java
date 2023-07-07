package com.softserve.certificate.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.softserve.commons.util.converter.DtoConverter;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ComponentScan(basePackages = {"com.softserve.certificate.controller"})
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
    public DataFormatter dataFormatter() {
        return new DataFormatter();
    }

    @Bean
    public DtoConverter dtoConverter() {
        return new DtoConverter(modelMapper());
    }
}
