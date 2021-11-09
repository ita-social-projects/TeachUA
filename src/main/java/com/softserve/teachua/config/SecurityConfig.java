package com.softserve.teachua.config;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.security.CustomUserDetailsService;
import com.softserve.teachua.security.JwtFilter;
import com.softserve.teachua.security.RestAuthenticationEntryPoint;
import com.softserve.teachua.security.oauth2.CustomOAuth2UserService;
import com.softserve.teachua.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.softserve.teachua.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.softserve.teachua.security.oauth2.OAuth2AuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static com.softserve.teachua.constants.RoleData.ADMIN;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Autowired
    private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Autowired
    private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    private final JwtFilter jwtFilter;

    @Autowired
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web
//            .ignoring()
//            .antMatchers(
//                    "/v3/api-docs/**",
//                    "/swagger-ui/**",
//                    "/swagger-ui.html",
//                    "/swagger-resources/**",
//                    "/swagger-resources"
//            );
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .cors()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .and()
//                .authorizeRequests()
//                .antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
//                .and()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/static/**").permitAll()
                .antMatchers("/manifest.json").permitAll()
                .antMatchers("/favicon**").permitAll()
                .antMatchers("/upload/**").permitAll()
                .antMatchers(HttpMethod.GET,
                        "/club/*",
                        "/clubs",
                        "/challenge",
                        "/challenge/*",
                        "/marathon",
                        "/marathon/*",
                        "/marathon/task/*",
                        "/about",
                        "/centers",
                        "/center/*",
                        "/service").permitAll()
                .antMatchers(HttpMethod.GET, "/challengeUA", "/challengeUA/registration", "/challengeUA/task/*").permitAll()
                .antMatchers(HttpMethod.GET,"/user/*").permitAll()
                .antMatchers(HttpMethod.GET,"/manager/**" ).hasAnyRole("MANAGER", "ADMIN")
                .antMatchers(HttpMethod.GET,"/admin/**").hasRole("ADMIN")
                .antMatchers("/verify", "/verifyreset").permitAll()
                .antMatchers("/roles").hasRole("ADMIN")
                .antMatchers("/index", "/api/signup", "/api/signin", "/api/signout", "/api/verify", "/api/resetpassword", "/api/verifyreset").permitAll()
                .antMatchers(HttpMethod.GET, "/api/user/**","/api/verify","/api/verifyreset").hasAnyRole("USER", "ADMIN", "MANAGER")
                .antMatchers(HttpMethod.PUT, "/api/user/**").hasAnyRole("USER", "ADMIN", "MANAGER")
                .antMatchers(HttpMethod.GET, "/api/cities", "/api/city/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/city").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/city/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/city/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/categories/**", "/api/category/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/centers/search/advanced").permitAll()
                .antMatchers(HttpMethod.POST, "/api/category").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/category/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/category/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/newslist", "/api/newslist/search").permitAll()
                .antMatchers(HttpMethod.GET, "/api/club/**", "/api/clubs/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/club").hasAnyRole("MANAGER", "ADMIN")
                .antMatchers(HttpMethod.POST, "/api/upload-excel").permitAll() // TODO: only for admins
                .antMatchers(HttpMethod.POST, "/api/load-excel-to-db").permitAll() // TODO: only for admins
                .antMatchers(HttpMethod.GET, "/api/download-database-sql").permitAll() // TODO: only for admins
                .antMatchers(HttpMethod.PUT, "/api/club").hasAnyRole("MANAGER", "ADMIN")
                .antMatchers(HttpMethod.POST, "/api/complaint", "/api/feedback").hasAnyRole("USER", "MANAGER", "ADMIN")
                .antMatchers(HttpMethod.GET, "/api/center/**", "/api/centers/**", "/api/feedbacks/**").permitAll()
                .antMatchers(HttpMethod.DELETE, "/api/center/**").hasAnyRole("MANAGER", "ADMIN")
                .antMatchers(HttpMethod.GET, "/api/search").permitAll()
                .antMatchers(HttpMethod.GET, "/api/questions", "/api/question/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/question").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/question/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/question/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/contact-types", "/api/districts/**").permitAll()
                .antMatchers(HttpMethod.DELETE, "/api/club/**").hasAnyRole("MANAGER", "ADMIN")
                .antMatchers(HttpMethod.GET, "/api/challenge/{\\d+}/tasks").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/challenges", "/api/challenge/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/challenge/**", "/api/challenge").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/challenge/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/challenge/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/challenge/{\\d+}/task").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/tasks").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/challenge/task/{\\d+}").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/challenge/task/{\\d+}").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/challenge/task/{\\d+}").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/about", "/api/about/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/about", "/api/about/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/about/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/about/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/api/clubs/rating").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/api/centers/rating").hasRole("ADMIN")

                //TODO: only for admin
                .antMatchers(HttpMethod.GET, "/api/logs").permitAll()
                .antMatchers(HttpMethod.GET, "/logs").permitAll()
                .antMatchers(HttpMethod.DELETE, "/api/logs").permitAll()
                .antMatchers(HttpMethod.DELETE, "/logs").permitAll()
                .antMatchers(HttpMethod.GET, "/api/log/**").permitAll()
                .antMatchers(HttpMethod.GET, "/log/**").permitAll()

                .antMatchers("/oauth2/**").permitAll()
                .antMatchers("/api/upload-image/**").permitAll()
                .antMatchers("/api/users","/api/user/update").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorize")
                .authorizationRequestRepository(cookieAuthorizationRequestRepository())
                .and()
                .redirectionEndpoint()
                .baseUri("/oauth2/callback/*")
                .and()
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler)
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/signout")).logoutSuccessUrl("/signin");
    }
}
