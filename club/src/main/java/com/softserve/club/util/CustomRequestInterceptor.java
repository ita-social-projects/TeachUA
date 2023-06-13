package com.softserve.club.util;

import com.softserve.club.security.UserPrincipal;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import java.util.Iterator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class CustomRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        UserPrincipal userPrincipal =
                (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        requestTemplate.header("username", userPrincipal.getUsername());

        Iterator<? extends GrantedAuthority> iterator = userPrincipal.getAuthorities().iterator();
        if (iterator.hasNext()) {
            String authority = iterator.next().getAuthority();
            requestTemplate.header("role", authority);
        }
    }
}
