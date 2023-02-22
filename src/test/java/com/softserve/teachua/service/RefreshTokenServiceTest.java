package com.softserve.teachua.service;

import static com.softserve.teachua.TestConstants.ACCESS_TOKEN;
import static com.softserve.teachua.TestConstants.ENCODED_REFRESH_TOKEN;
import static com.softserve.teachua.TestConstants.NEW_ENCODED_REFRESH_TOKEN;
import static com.softserve.teachua.TestConstants.NEW_REFRESH_TOKEN;
import static com.softserve.teachua.TestConstants.OLD_REFRESH_TOKEN;
import static com.softserve.teachua.TestUtils.getUser;
import com.softserve.teachua.dto.security.RefreshTokenResponse;
import com.softserve.teachua.exception.UserAuthenticationException;
import com.softserve.teachua.model.RefreshToken;
import com.softserve.teachua.model.User;
import com.softserve.teachua.repository.RefreshTokenRepository;
import com.softserve.teachua.security.JwtUtils;
import com.softserve.teachua.service.impl.RefreshTokenServiceImpl;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenService;
    private RefreshToken refreshToken;

    @BeforeEach
    void setUp() {
        User user = getUser();
        refreshToken = RefreshToken.builder()
                .id(user.getId())
                .user(user)
                .token(ENCODED_REFRESH_TOKEN)
                .build();
    }

    @Test
    void givenUserWithRefreshToken_whenAssignRefreshToken_thenSetNewTokenToUser() {
        User user = getUser().withRefreshToken(refreshToken);
        when(jwtUtils.generateRefreshToken(user.getId()))
                .thenReturn(NEW_REFRESH_TOKEN);
        when(passwordEncoder.encode(NEW_REFRESH_TOKEN))
                .thenReturn(ENCODED_REFRESH_TOKEN);

        String actual = refreshTokenService.assignRefreshToken(user);

        assertEquals(NEW_REFRESH_TOKEN, actual);
        assertEquals(ENCODED_REFRESH_TOKEN, user.getRefreshToken().getToken());
    }

    @Test
    void givenUserWithNullRefreshToken_whenAssignRefreshToken_thenSetTokenToUser() {
        User user = getUser().withRefreshToken(null);
        when(jwtUtils.generateRefreshToken(user.getId()))
                .thenReturn(NEW_REFRESH_TOKEN);
        when(passwordEncoder.encode(NEW_REFRESH_TOKEN))
                .thenReturn(ENCODED_REFRESH_TOKEN);

        String actual = refreshTokenService.assignRefreshToken(user);

        assertEquals(NEW_REFRESH_TOKEN, actual);
        assertNotNull(user.getRefreshToken());
        assertEquals(ENCODED_REFRESH_TOKEN, user.getRefreshToken().getToken());
    }

    @Test
    void givenInvalidRefreshToken_whenRevokeRefreshToken_thenThrow() {
        when(jwtUtils.isRefreshTokenValid(OLD_REFRESH_TOKEN))
                .thenReturn(false);

        assertThatThrownBy(() -> refreshTokenService.revokeRefreshToken(OLD_REFRESH_TOKEN))
                .isInstanceOf(UserAuthenticationException.class);
    }

    @Test
    void givenRefreshTokenNotInUse_whenRevokeRefreshToken_thenThrow() {
        when(jwtUtils.isRefreshTokenValid(OLD_REFRESH_TOKEN))
                .thenReturn(true);
        when(jwtUtils.getUserIdFromRefreshToken(OLD_REFRESH_TOKEN))
                .thenReturn(refreshToken.getId());
        when(refreshTokenRepository.findById(refreshToken.getId()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> refreshTokenService.revokeRefreshToken(OLD_REFRESH_TOKEN))
                .isInstanceOf(UserAuthenticationException.class);
    }

    @Test
    void givenValidRefreshToken_whenRevokeRefreshToken_thenRevoke() {
        User user = refreshToken.getUser();
        when(jwtUtils.isRefreshTokenValid(OLD_REFRESH_TOKEN))
                .thenReturn(true);
        when(jwtUtils.getUserIdFromRefreshToken(OLD_REFRESH_TOKEN))
                .thenReturn(user.getId());
        when(refreshTokenRepository.findById(user.getId()))
                .thenReturn(Optional.of(refreshToken));
        when(passwordEncoder.matches(OLD_REFRESH_TOKEN, ENCODED_REFRESH_TOKEN))
                .thenReturn(true);

        refreshTokenService.revokeRefreshToken(OLD_REFRESH_TOKEN);

        verify(refreshTokenRepository).delete(refreshToken);
        assertNull(user.getRefreshToken());
        assertNull(refreshToken.getUser());
    }

    @Test
    void givenValidRefreshToken_whenRefreshAccessToken_thenReturnRefreshTokenResponse() {
        RefreshTokenResponse expected = RefreshTokenResponse.builder()
                .refreshToken(NEW_REFRESH_TOKEN)
                .accessToken(ACCESS_TOKEN)
                .build();

        User user = refreshToken.getUser();
        when(jwtUtils.isRefreshTokenValid(OLD_REFRESH_TOKEN))
                .thenReturn(true);
        when(jwtUtils.getUserIdFromRefreshToken(OLD_REFRESH_TOKEN))
                .thenReturn(user.getId());
        when(refreshTokenRepository.findById(user.getId()))
                .thenReturn(Optional.of(refreshToken));
        when(passwordEncoder.matches(OLD_REFRESH_TOKEN, ENCODED_REFRESH_TOKEN))
                .thenReturn(true);
        when(passwordEncoder.encode(NEW_REFRESH_TOKEN))
                .thenReturn(NEW_ENCODED_REFRESH_TOKEN);
        when(jwtUtils.generateRefreshToken(user.getId()))
                .thenReturn(NEW_REFRESH_TOKEN);
        when(jwtUtils.generateAccessToken(user.getEmail()))
                .thenReturn(ACCESS_TOKEN);

        RefreshTokenResponse actual = refreshTokenService.refreshAccessToken(OLD_REFRESH_TOKEN);

        assertEquals(expected, actual);
        assertEquals(NEW_ENCODED_REFRESH_TOKEN, refreshToken.getToken());
    }
}
