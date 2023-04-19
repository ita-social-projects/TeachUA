package com.softserve.teachua.security;

import io.jsonwebtoken.ExpiredJwtException;
import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private static final String ABSENT_TOKEN = "Token is empty [%s]";
    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public JwtFilter(JwtUtils jwtUtils, CustomUserDetailsService customUserDetailsService) {
        this.jwtUtils = jwtUtils;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String accessToken = jwtUtils.getJwtFromRequest(request);

            if (StringUtils.hasText(accessToken) && jwtUtils.isAccessTokenValid(accessToken)) {
                String userEmail = jwtUtils.getEmailFromAccessToken(accessToken);
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("User {} successfully authenticate with token {}", userDetails.getUsername(), accessToken);
            } else {
                log.debug(String.format(ABSENT_TOKEN, request.getRequestURI()));
            }
        } catch (ExpiredJwtException ex) {
            log.error("Detected expired jwt token");
        } catch (Exception ex) {
            log.error("Could not authenticate user", ex);
        }
        filterChain.doFilter(request, response);
    }
}
