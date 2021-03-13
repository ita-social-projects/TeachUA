package com.softserve.teachua.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.Filter;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Configuration
public class ApplicationConfig {
    private static final String UPLOAD_LOCATION = "/upload/";
    private static final String STATIC_LOCATION = "/static/";
    private static final String API_LOCATION = "/api/";
    private static final String SLASH = "/";

    @Value("${server.servlet.context-path}")
    private String rootUri;

    @Bean
    public FilterRegistrationBean customFilterBean() {
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter((request, response, chain) -> {
            HttpServletRequest req = (HttpServletRequest) request;
            if (!req.getRequestURI().startsWith(rootUri + UPLOAD_LOCATION) &&
                    !req.getRequestURI().startsWith(rootUri + STATIC_LOCATION) &&
                    !req.getRequestURI().startsWith(rootUri + API_LOCATION) &&
                    !req.getRequestURI().equals(rootUri + SLASH)) {
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
        return new ModelMapper();
    }

    @Bean
    public ObjectMapper objectMapper() { return new ObjectMapper(); }
}
