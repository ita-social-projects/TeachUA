package com.softserve.teachua.config;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.security.JwtFilter;
import com.softserve.teachua.security.RestAuthenticationEntryPoint;
import com.softserve.teachua.security.oauth2.CustomOAuth2UserService;
import com.softserve.teachua.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.softserve.teachua.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.softserve.teachua.security.oauth2.OAuth2AuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@Order(1)
@EnableMethodSecurity
public class SecurityConfig {
    private static final String ADMIN = RoleData.ADMIN.getRoleName();
    private static final String USER = RoleData.USER.getRoleName();
    private static final String MANAGER = RoleData.MANAGER.getRoleName();
    private final JwtFilter jwtFilter;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Autowired
    public SecurityConfig(JwtFilter jwtFilter, CustomOAuth2UserService customOAuth2UserService,
                          OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler,
                          OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler) {
        this.jwtFilter = jwtFilter;
        this.customOAuth2UserService = customOAuth2UserService;
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
        this.oAuth2AuthenticationFailureHandler = oAuth2AuthenticationFailureHandler;
    }

    @Autowired
    public void setHttpCookieOAuth2Authorization(HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth) {
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth;
    }

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/main", "/index.html").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**",
                                "/swagger", "/swagger-resources/**", "/swagger-resources").permitAll()
                        .requestMatchers("/static/**").permitAll()
                        .requestMatchers("/manifest.json").permitAll()
                        .requestMatchers("/favicon**").permitAll()
                        .requestMatchers("/upload/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/club/*", "/clubs", "/challenge",
                                "/challenge/*", "/challenge/**", "/challenges/task/**", "/challenge/task/**",
                                "/marathon", "/marathon/*", "/marathon/task/*", "/about", "/banners",
                                "/banner/*", "/centers", "/center/*", "/service").permitAll()
                        .requestMatchers("/api/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/challengeUA",
                                "/challengeUA/registration", "/challengeUA/task/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/user/*").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/user/**").hasAnyRole(USER, ADMIN, MANAGER)
                        .requestMatchers("/verify", "/verifyreset").permitAll()
                        .requestMatchers("/roles").hasRole(ADMIN)
                        .requestMatchers("/index").permitAll()
                        .requestMatchers("/oauth2/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/logs").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/logs")
                        .permitAll()
                )
                .oauth2Login(oauth -> oauth
                        .authorizationEndpoint(authEndpoint -> authEndpoint
                                .baseUri("/oauth2/authorize")
                                .authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository))
                        .redirectionEndpoint(redirectionEndpoint -> redirectionEndpoint
                                .baseUri("/oauth2/callback/*"))
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                                .userService(customOAuth2UserService))
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                        .failureHandler(oAuth2AuthenticationFailureHandler)
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(new RestAuthenticationEntryPoint()))
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/signout"))
                        .logoutSuccessUrl("/signin"));
        return http.build();
    }
}
