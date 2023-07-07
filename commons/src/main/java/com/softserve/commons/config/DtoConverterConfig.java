package com.softserve.commons.config;

import com.softserve.commons.util.converter.DtoConverter;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DtoConverterConfig {
    @Bean
    @ConditionalOnMissingBean(ModelMapper.class)
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public DtoConverter dtoConverter(ModelMapper modelMapper) {
        return new DtoConverter(modelMapper);
    }
}
