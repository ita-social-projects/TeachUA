package com.softserve.teachua.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final String ABSENT_TOKEN = "Token is empty [%s]";
    private final JwtProvider jwtProvider;

    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    public JwtFilter(JwtProvider jwtProvider, CustomUserDetailsService customUserDetailsService) {
        this.jwtProvider = jwtProvider;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.debug("**doFilter start, requestURI {}", httpServletRequest.getRequestURI());
        try {
            String jwt = jwtProvider.getJwtFromRequest(httpServletRequest);

            if (StringUtils.hasText(jwt) && jwtProvider.validateToken(jwt)) {
                Long userId = jwtProvider.getUserIdFromToken(jwt);
                String currentEmail = jwtProvider.getEmailFromToken(jwt);
                log.debug("email user = " + currentEmail);
                UserDetails userDetails = customUserDetailsService.loadUserById(userId);
                if (!userDetails.getUsername().equals(currentEmail)) {
                    throw new RuntimeException("Invalid email");
                }
                log.debug("USER AUTHORITIES");
                log.debug(userDetails.getAuthorities().toString());
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails,
                                null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("User " + userDetails.getUsername() + "successfully authenticate with token" + jwt);
            } else {
                log.debug(String.format(ABSENT_TOKEN, httpServletRequest.getRequestURI()));
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
        log.debug("**doFilter done");
    }
}