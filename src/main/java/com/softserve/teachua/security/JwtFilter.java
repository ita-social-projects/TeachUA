package com.softserve.teachua.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public JwtFilter(JwtProvider jwtProvider, CustomUserDetailsService customUserDetailsService) {
        this.jwtProvider = jwtProvider;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String accessToken = jwtProvider.getJwtFromRequest(request);

            if (StringUtils.hasText(accessToken) && jwtProvider.isAccessTokenValid(accessToken)) {
                String userEmail = jwtProvider.getEmailFromAccessToken(accessToken);
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("User {} successfully authenticate with token {}", userDetails.getUsername(), accessToken);
            } else {
                log.debug(String.format(ABSENT_TOKEN, request.getRequestURI()));
            }
        } catch (Exception ex) {
            logger.error("Could not authenticate user", ex);
        }
        filterChain.doFilter(request, response);
    }
}
