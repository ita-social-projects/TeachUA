package com.softserve.teachua.security;

import static com.softserve.teachua.TestConstants.ACCESS_TOKEN;
import static com.softserve.teachua.TestConstants.EMPTY_STRING;
import static com.softserve.teachua.TestConstants.NOT_EMPTY_STRING;
import static com.softserve.teachua.TestConstants.USER_EMAIL;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.servlet.http.HttpServletRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JwtUtilsTest {

    public final String ACCESS_TOKEN_SECRET = "accessTokenSecret";
    public final String REFRESH_TOKEN_SECRET = "refreshTokenSecret";
    public final Integer ACCESS_EXPIRATION_TIME_IN_MINUTES = 30;
    public final Integer REFRESH_EXPIRATION_TIME_IN_DAYS = 7;
    public static final String AUTHORIZATION = "Authorization";
    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils(
                ACCESS_TOKEN_SECRET,
                REFRESH_TOKEN_SECRET,
                ACCESS_EXPIRATION_TIME_IN_MINUTES,
                REFRESH_EXPIRATION_TIME_IN_DAYS
        );
    }

    @Test
    void givenInvalidToken_whenIsAccessTokenValid_shouldReturnFalse() {
        boolean actual = jwtUtils.isAccessTokenValid(EMPTY_STRING);
        assertFalse(actual);
    }

    @Test
    void givenInvalidToken_whenIsRefreshTokenValid_shouldReturnFalse() {
        boolean actual = jwtUtils.isRefreshTokenValid(EMPTY_STRING);
        assertFalse(actual);
    }

    @Test
    void givenValidToken_whenGetEmailFromAccessToken_shouldReturnEmail() {
        String actual = jwtUtils.getEmailFromAccessToken(getAccessToken());
        assertEquals(USER_EMAIL, actual);
    }

    @Test
    void givenValidToken_whenGetEmailFromRefreshToken_shouldReturnEmail() {
        String actual = jwtUtils.getEmailFromRefreshToken(getRefreshToken());
        assertEquals(USER_EMAIL, actual);
    }

    @Test
    void givenValidToken_whenIsAccessTokenValid_shouldReturnTrue() {
        boolean actual = jwtUtils.isAccessTokenValid(getAccessToken());
        assertTrue(actual);
    }

    @Test
    void givenValidToken_whenIsRefreshTokenValid_shouldReturnTrue() {
        boolean actual = jwtUtils.isRefreshTokenValid(getRefreshToken());
        assertTrue(actual);
    }

    @Test
    void givenRequestWithBearerToken_whenGetJwtFromRequest_shouldReturnToken() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(AUTHORIZATION)).thenReturn("Bearer " + ACCESS_TOKEN);

        String actual = jwtUtils.getJwtFromRequest(request);
        assertEquals(ACCESS_TOKEN, actual);
    }

    @Test
    void givenRequestWithoutToken_whenGetJwtFromRequest_shouldNull() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(AUTHORIZATION)).thenReturn(null);

        String actual = jwtUtils.getJwtFromRequest(request);
        assertNull(actual);
    }

    @Test
    void givenRequestWithInvalidToken_whenGetJwtFromRequest_shouldNull() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(AUTHORIZATION)).thenReturn(NOT_EMPTY_STRING);

        String actual = jwtUtils.getJwtFromRequest(request);
        assertNull(actual);
    }

    private String getAccessToken() {
        return Jwts.builder()
                .setSubject(USER_EMAIL)
                .signWith(SignatureAlgorithm.HS512, ACCESS_TOKEN_SECRET)
                .compact();
    }

    private String getRefreshToken() {
        return Jwts.builder()
                .setSubject(USER_EMAIL)
                .signWith(SignatureAlgorithm.HS512, REFRESH_TOKEN_SECRET)
                .compact();
    }
}
