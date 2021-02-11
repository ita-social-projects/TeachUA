package com.softserve.teachua.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.util.StringUtils.hasText;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public JwtFilter(JwtProvider jwtProvider, CustomUserDetailsService customUserDetailsService) {
        this.jwtProvider = jwtProvider;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.info("**doFilter start, requestURI {}", httpServletRequest.getRequestURI());
        String token = getTokenFromRequest(httpServletRequest);
        // TODO check ExpirationDate
        if ((token != null)
                && jwtProvider.validateToken(token)) {
            String email = jwtProvider.getEmailFromToken(token);
            CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(email);
            // TODO
            customUserDetails.setExpirationDate(jwtProvider.getExpirationDate(token));
            //
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(customUserDetails,
                    null, customUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
//        else {
//        	// TODO
//        	throw new RuntimeException("error token");
//        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
        //
        log.info("**doFilter done");
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(AUTHORIZATION);
        if (hasText(bearer) && bearer.startsWith(BEARER)) {
            return bearer.substring(BEARER.length());
        }
        return null;
    }
}
