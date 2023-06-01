package com.softserve.teachua.config;

import com.softserve.teachua.constants.RoleData;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

/**
 * Multiple Entry Points in Spring Security.
 *
 * @see <a href="https://www.baeldung.com/spring-security-multiple-entry-points">Link for spring-security-multiple-entry-points</a>
 */
//@Configuration
//@EnableWebSecurity
//@Order(2)
public class SecurityBasicAuthConfig {
    //@Bean
    public SecurityFilterChain filterChainAppForBasicAuth(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests().requestMatchers("/api/prometheus").hasRole(RoleData.ADMIN.getRoleName())
                .and().httpBasic().authenticationEntryPoint(authenticationEntryPoint());
        return http.build();
    }

    //@Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        BasicAuthenticationEntryPoint entryPoint =
                new BasicAuthenticationEntryPoint();
        entryPoint.setRealmName("basic auth realm");
        return entryPoint;
    }
}
