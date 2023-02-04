package com.softserve.teachua.service;

import static com.softserve.teachua.TestUtils.getUser;
import com.softserve.teachua.dto.security.RefreshTokenResponse;
import com.softserve.teachua.exception.UserAuthenticationException;
import com.softserve.teachua.model.RefreshToken;
import com.softserve.teachua.model.User;
import com.softserve.teachua.repository.RefreshTokenRepository;
import com.softserve.teachua.security.JwtProvider;
import com.softserve.teachua.service.impl.RefreshTokenServiceImpl;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {
    private static final String OLD_REFRESH_TOKEN = "oldRefreshToken";
    private static final String NEW_REFRESH_TOKEN = "newRefreshToken";
    private static final String ACCESS_TOKEN = "accessToken";
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private JwtProvider jwtProvider;
    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenService;
    private RefreshToken refreshToken;

    @BeforeEach
    void setUp() {
        User user = getUser();
        refreshToken = RefreshToken.builder()
                .id(user.getId())
                .user(user)
                .token(OLD_REFRESH_TOKEN)
                .build();
    }

    @Test
    void givenUserWithRefreshToken_whenAssignRefreshToken_thenSetNewTokenToUser() {
        User user = getUser().withRefreshToken(refreshToken);
        when(jwtProvider.generateRefreshToken(user.getEmail()))
                .thenReturn(NEW_REFRESH_TOKEN);

        String actual = refreshTokenService.assignRefreshToken(user);

        assertEquals(NEW_REFRESH_TOKEN, actual);
        assertEquals(NEW_REFRESH_TOKEN, user.getRefreshToken().getToken());
    }

    @Test
    void givenUserWithNullRefreshToken_whenAssignRefreshToken_thenSetTokenToUser() {
        User user = getUser().withRefreshToken(null);
        when(jwtProvider.generateRefreshToken(user.getEmail()))
                .thenReturn(NEW_REFRESH_TOKEN);

        String actual = refreshTokenService.assignRefreshToken(user);

        assertEquals(NEW_REFRESH_TOKEN, actual);
        assertNotNull(user.getRefreshToken());
        assertEquals(NEW_REFRESH_TOKEN, user.getRefreshToken().getToken());
    }

    @Test
    void givenInvalidRefreshToken_whenRevokeRefreshToken_thenThrow() {
        when(jwtProvider.isRefreshTokenValid(OLD_REFRESH_TOKEN))
                .thenReturn(false);

        assertThatThrownBy(() -> refreshTokenService.revokeRefreshToken(OLD_REFRESH_TOKEN))
                .isInstanceOf(UserAuthenticationException.class);
    }

    @Test
    void givenRefreshTokenNotInUse_whenRevokeRefreshToken_thenThrow() {
        when(jwtProvider.isRefreshTokenValid(OLD_REFRESH_TOKEN))
                .thenReturn(true);
        when(refreshTokenRepository.findByToken(OLD_REFRESH_TOKEN))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> refreshTokenService.revokeRefreshToken(OLD_REFRESH_TOKEN))
                .isInstanceOf(UserAuthenticationException.class);
    }

    @Test
    void givenValidRefreshToken_whenRevokeRefreshToken_thenRevoke() {
        User user = refreshToken.getUser();
        when(jwtProvider.isRefreshTokenValid(OLD_REFRESH_TOKEN))
                .thenReturn(true);
        when(refreshTokenRepository.findByToken(OLD_REFRESH_TOKEN))
                .thenReturn(Optional.of(refreshToken));

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
        when(jwtProvider.isRefreshTokenValid(OLD_REFRESH_TOKEN))
                .thenReturn(true);
        when(jwtProvider.getEmailFromRefreshToken(OLD_REFRESH_TOKEN))
                .thenReturn(user.getEmail());
        when(jwtProvider.generateRefreshToken(user.getEmail()))
                .thenReturn(NEW_REFRESH_TOKEN);
        when(jwtProvider.generateAccessToken(user.getEmail()))
                .thenReturn(ACCESS_TOKEN);
        when(refreshTokenRepository.findByToken(OLD_REFRESH_TOKEN))
                .thenReturn(Optional.of(refreshToken));

        RefreshTokenResponse actual = refreshTokenService.refreshAccessToken(OLD_REFRESH_TOKEN);

        assertEquals(expected, actual);
        assertEquals(NEW_REFRESH_TOKEN, refreshToken.getToken());
    }
}
