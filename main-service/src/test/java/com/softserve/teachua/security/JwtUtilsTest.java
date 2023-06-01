package com.softserve.teachua.security;

import static com.softserve.teachua.TestConstants.ACCESS_TOKEN;
import static com.softserve.teachua.TestConstants.EMPTY_STRING;
import static com.softserve.teachua.TestConstants.NOT_EMPTY_STRING;
import static com.softserve.teachua.TestConstants.USER_EMAIL;
import static com.softserve.teachua.TestConstants.USER_ID;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JwtUtilsTest {
    public final String ACCESS_TOKEN_SECRET = "655368566D597133743677397A244326462948404D635166546A576E5A723475";
    public final String REFRESH_TOKEN_SECRET = "4B6150645367566B59703373367639792442264529482B4D6251655468576D5A";
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
    void givenValidToken_whenGetUserIdFromRefreshToken_shouldReturnEmail() {
        Long actual = jwtUtils.getUserIdFromRefreshToken(getRefreshToken());
        assertEquals(USER_ID, actual);
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
        Calendar calendar = getCalendar();
        calendar.add(Calendar.MINUTE, 30);
        return Jwts.builder()
                .setSubject(USER_EMAIL)
                .setExpiration(calendar.getTime())
                .signWith(getSignInKey(ACCESS_TOKEN_SECRET), SignatureAlgorithm.HS256)
                .compact();
    }

    private String getRefreshToken() {
        Calendar calendar = getCalendar();
        calendar.add(Calendar.DATE, 3);
        Claims claims = Jwts.claims();
        claims.put("user_id", USER_ID);
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(calendar.getTime())
                .signWith(getSignInKey(REFRESH_TOKEN_SECRET), SignatureAlgorithm.HS256)
                .compact();
    }

    private Calendar getCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar;
    }

    private Key getSignInKey(String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
