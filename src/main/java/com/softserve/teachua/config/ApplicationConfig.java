package com.softserve.teachua.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.Filter;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
@EnableScheduling
@ComponentScan(basePackages = {"com.softserve.teachua.controller"})
public class ApplicationConfig {
    private static final String UPLOAD_LOCATION = "/upload/";
    private static final String STATIC_LOCATION = "/static/";
    private static final String API_LOCATION = "/api/";
    private static final String SLASH = "/";
    private static final String SWAGGER_UI = "/swagger";
    private static final String SWAGGER_RESOURCE = "api-docs";

    @Value("${server.servlet.context-path}")
    private String rootUri;

    private String removeSecondSlash(String uri) {
        return uri.replace("//", "/").replace(":/", "://");
    }

    @Bean
    public FilterRegistrationBean<Filter> customFilterBean() {
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter((request, response, chain) -> {
            HttpServletRequest req = (HttpServletRequest) request;
            if (!req.getRequestURI().startsWith(removeSecondSlash(rootUri + UPLOAD_LOCATION))
                    && !req.getRequestURI().startsWith(removeSecondSlash(rootUri + STATIC_LOCATION))
                    && !req.getRequestURI().startsWith(removeSecondSlash(rootUri + API_LOCATION))
                    && !req.getRequestURI().equals(removeSecondSlash(rootUri + SLASH))
                    && !req.getRequestURI().startsWith(removeSecondSlash(rootUri + SWAGGER_UI))
                    && !req.getRequestURI().contains(SWAGGER_RESOURCE)) {
                RequestDispatcher requestDispatcher = request.getRequestDispatcher(SLASH);
                requestDispatcher.forward(request, response);
                return;
            }

            chain.doFilter(request, response);
        });
        return filterFilterRegistrationBean;
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        Converter<LocalDateTime, String> dateTimeConverter = new AbstractConverter<>() {
            @Override
            protected String convert(LocalDateTime source) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                return source.format(formatter);
            }
        };
        modelMapper.addConverter(dateTimeConverter);

        return modelMapper;
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
}
