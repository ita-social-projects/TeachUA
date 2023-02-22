package com.softserve.teachua.security;

import static com.softserve.teachua.TestConstants.EMPTY_STRING;
import static com.softserve.teachua.TestConstants.NOT_EMPTY_STRING;
import static com.softserve.teachua.TestConstants.USER_EMAIL;
import static com.softserve.teachua.TestUtils.getUser;
import static com.softserve.teachua.TestUtils.getUserPrincipal;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JwtFilterTest {
    @Mock
    private FilterChain chain;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private CustomUserDetailsService customUserDetailsService;
    @InjectMocks
    private JwtFilter jwtFilter;

    @BeforeEach
    void setUp() {
    }

    @Test
    void givenEmptyToken_whenDoFilterInternal_shouldSkip() throws ServletException, IOException {
        when(jwtUtils.getJwtFromRequest(request)).thenReturn(EMPTY_STRING);

        jwtFilter.doFilterInternal(request, response, chain);
        verify(jwtUtils, never()).getEmailFromAccessToken(anyString());
        verify(customUserDetailsService, never()).loadUserByUsername(anyString());
    }

    @Test
    void givenInvalidToken_whenDoFilterInternal_shouldSkip() throws ServletException, IOException {
        when(jwtUtils.getJwtFromRequest(request)).thenReturn(NOT_EMPTY_STRING);
        when(jwtUtils.isAccessTokenValid(NOT_EMPTY_STRING)).thenReturn(false);

        jwtFilter.doFilterInternal(request, response, chain);
        verify(jwtUtils, never()).getEmailFromAccessToken(anyString());
        verify(customUserDetailsService, never()).loadUserByUsername(anyString());
    }

    @Test
    void shouldSkip_whenDoFilterInternal_throwException() throws ServletException, IOException {
        when(jwtUtils.getJwtFromRequest(request)).thenReturn(NOT_EMPTY_STRING);
        when(jwtUtils.isAccessTokenValid(NOT_EMPTY_STRING)).thenReturn(false);

        jwtFilter.doFilterInternal(request, response, chain);
        verify(jwtUtils, never()).getEmailFromAccessToken(anyString());
        verify(customUserDetailsService, never()).loadUserByUsername(anyString());
    }

    @Test
    void givenValidToken_whenDoFilterInternal_shouldLoadUser() {
        when(jwtUtils.getJwtFromRequest(request)).thenReturn(NOT_EMPTY_STRING);
        when(jwtUtils.isAccessTokenValid(NOT_EMPTY_STRING)).thenReturn(true);
        when(jwtUtils.getEmailFromAccessToken(NOT_EMPTY_STRING)).thenReturn(USER_EMAIL);
        when(customUserDetailsService.loadUserByUsername(USER_EMAIL))
                .thenReturn(getUserPrincipal(getUser()));

        assertDoesNotThrow(() -> jwtFilter.doFilterInternal(request, response, chain));
    }
}
