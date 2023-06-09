package com.softserve.club.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.commons.constant.RoleData;
import com.softserve.commons.exception.UserPermissionException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;

    public JwtFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String username = request.getHeader("username");
            String roles = request.getHeader("roles");
            System.out.println(username);
            System.out.println(roles);
            if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(roles)) {
                RoleData rolesSet = objectMapper.readValue(roles,RoleData.class);

                UserPrincipal userPrincipal = new UserPrincipal(username,rolesSet);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userPrincipal, null, userPrincipal.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception ex) {
            log.error("Error user authentication, {}", ex.getMessage());
            throw new UserPermissionException();
        }
        filterChain.doFilter(request, response);
    }
}
