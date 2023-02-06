package com.softserve.teachua.service.impl;

import com.softserve.teachua.dto.security.RefreshTokenResponse;
import com.softserve.teachua.exception.UserAuthenticationException;
import com.softserve.teachua.model.RefreshToken;
import com.softserve.teachua.model.User;
import com.softserve.teachua.repository.RefreshTokenRepository;
import com.softserve.teachua.security.JwtProvider;
import com.softserve.teachua.service.RefreshTokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    public static final String UNPROCESSED_REFRESH_TOKEN = "Refresh token is invalid or has been expired";
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository, JwtProvider jwtProvider) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    @Transactional
    public String assignRefreshToken(User user) {
        String refreshToken = jwtProvider.generateRefreshToken(user.getEmail());
        if (user.getRefreshToken() != null) {
            user.getRefreshToken().setToken(refreshToken);
        } else {
            user.setRefreshToken(new RefreshToken().withUser(user).withToken(refreshToken));
        }
        return user.getRefreshToken().getToken();
    }

    @Override
    @Transactional
    public void revokeRefreshToken(String token) {
        validateRefreshToken(token);
        RefreshToken refreshToken = getRefreshToken(token);
        refreshToken.revoke();
        refreshTokenRepository.delete(refreshToken);
    }

    @Override
    @Transactional
    public RefreshTokenResponse refreshAccessToken(String refreshToken) {
        validateRefreshToken(refreshToken);
        String email = jwtProvider.getEmailFromRefreshToken(refreshToken);
        String newRefreshToken = jwtProvider.generateRefreshToken(email);
        getRefreshToken(refreshToken).setToken(newRefreshToken);

        return RefreshTokenResponse.builder()
                .accessToken(jwtProvider.generateAccessToken(email))
                .refreshToken(newRefreshToken)
                .build();
    }

    private void validateRefreshToken(String refreshToken) {
        if (!jwtProvider.isRefreshTokenValid(refreshToken)) {
            throw new UserAuthenticationException(UNPROCESSED_REFRESH_TOKEN);
        }
    }

    private RefreshToken getRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new UserAuthenticationException(UNPROCESSED_REFRESH_TOKEN));
    }
}
