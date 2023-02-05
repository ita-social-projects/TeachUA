package com.softserve.teachua.config;

import com.softserve.teachua.constants.RoleData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@Order(2)
// Multiple Entry Points in Spring Security
//https://www.baeldung.com/spring-security-multiple-entry-points
public class SecurityBasicAuthConfig {
    @Bean
    public SecurityFilterChain filterChainAppForBasicAuth(HttpSecurity http) throws Exception {
        http.antMatcher("/api/prometheus")
            .authorizeRequests().anyRequest().hasRole(RoleData.ADMIN.getRoleName())
            .and().httpBasic().authenticationEntryPoint(authenticationEntryPoint());
        return http.build();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(){
        BasicAuthenticationEntryPoint entryPoint =
            new BasicAuthenticationEntryPoint();
        entryPoint.setRealmName("basic auth realm");
        return entryPoint;
    }
}
