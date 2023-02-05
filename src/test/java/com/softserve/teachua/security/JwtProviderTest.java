package com.softserve.teachua.security;

import static com.softserve.teachua.TestConstants.ACCESS_TOKEN;
import javax.servlet.http.HttpServletRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JwtProviderTest {
    public static final String AUTHORIZATION = "Authorization";
    private JwtProvider jwtProvider;

    @BeforeEach
    void setUp() {
        jwtProvider = new JwtProvider();
    }

    @Test
    void givenRequestWithBearerToken_whenGetJwtFromRequest_shouldReturnToken() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(AUTHORIZATION)).thenReturn("Bearer " + ACCESS_TOKEN);

        String actual = jwtProvider.getJwtFromRequest(request);
        assertEquals(ACCESS_TOKEN, actual);
    }

    @Test
    void givenRequestWithoutToken_whenGetJwtFromRequest_shouldNull() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(AUTHORIZATION)).thenReturn(null);

        String actual = jwtProvider.getJwtFromRequest(request);
        assertNull(actual);
    }
}
