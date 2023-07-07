package com.softserve.commons.config;

import com.softserve.commons.security.UserPrincipal;
import feign.RequestInterceptor;
import java.util.Iterator;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableFeignClients(
        basePackages = "com.softserve.commons.client"
)
public class FeignConfig {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if (principal instanceof UserPrincipal userPrincipal) {
                requestTemplate.header("uid", String.valueOf(userPrincipal.getId()));
                requestTemplate.header("uname", userPrincipal.getUsername());

                Iterator<? extends GrantedAuthority> iterator = userPrincipal.getAuthorities().iterator();
                if (iterator.hasNext()) {
                    String authority = iterator.next().getAuthority();
                    requestTemplate.header("role", authority);
                }
            }
        };
    }
}
