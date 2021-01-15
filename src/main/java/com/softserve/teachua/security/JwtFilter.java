package com.softserve.teachua.security;

import static org.springframework.util.StringUtils.hasText;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtFilter extends GenericFilterBean {

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
    	// TODO
        log.info("**doFilter Start");
        String token = getTokenFromRequest((HttpServletRequest) servletRequest);
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
        filterChain.doFilter(servletRequest, servletResponse);
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
