package com.softserve.user.security;

import com.softserve.commons.constant.RoleData;
import com.softserve.commons.exception.UserPermissionException;
import com.softserve.commons.security.UserPrincipal;
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
    private final JwtUtils jwtUtils;

    public JwtFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            if (request.getRequestURI().equals("/api/v1/jwt/parse")) {
                parseJwt(request.getHeader("Authorization"), response);
                return;
            }

            String id = request.getHeader("uid");
            String username = request.getHeader("uname");
            String roleHeader = request.getHeader("role");
            if (!StringUtils.isEmpty(id) && !StringUtils.isEmpty(username)
                    && !StringUtils.isEmpty(roleHeader)) {
                RoleData role = RoleData.valueOf(roleHeader);
                UserPrincipal userPrincipal = new UserPrincipal(Long.valueOf(id), username, role);
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

    private void parseJwt(String token, HttpServletResponse response) {
        log.info("Parsing token");

        if (StringUtils.isEmpty(token)) {
            response.setHeader("uid", "");
            response.setHeader("uname", "");
            response.setHeader("role", "");
        } else {
            token = token.substring(7);
            UserPrincipal principal = jwtUtils.getUserPrincipal(token);
            //UserPrincipal principal = new UserPrincipal(1L, "Name", RoleData.ADMIN);
            response.setHeader("uid", String.valueOf(principal.getId()));
            response.setHeader("uname", principal.getUsername());
            response.setHeader("role", String.valueOf(principal.getRole()));
        }
    }
}
