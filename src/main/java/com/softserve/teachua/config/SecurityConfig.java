package com.softserve.teachua.config;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.security.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtFilter jwtFilter;

    @Autowired
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                //.antMatchers("/admin/*").hasRole(RoleData.ADMIN.getRoleName())
                .antMatchers("/roles").hasRole(RoleData.ADMIN.getRoleName())
                .antMatchers("/hello").hasAnyRole(RoleData.ADMIN.getRoleName(), RoleData.USER.getRoleName())
                .antMatchers("/index", "/signup", "/signin", "/signout").permitAll()
                .antMatchers("/user{id}", "/user{email}", "/users").permitAll()
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/signout")).logoutSuccessUrl("/signin");
    }

}
