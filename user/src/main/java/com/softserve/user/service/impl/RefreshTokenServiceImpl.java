package com.softserve.user.service.impl;

import com.softserve.user.dto.security.RefreshTokenResponse;
import com.softserve.user.exception.UserAuthenticationException;
import com.softserve.user.model.RefreshToken;
import com.softserve.user.model.User;
import com.softserve.user.repository.RefreshTokenRepository;
import com.softserve.user.security.JwtUtils;
import com.softserve.user.service.RefreshTokenService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {
    public static final String UNPROCESSED_REFRESH_TOKEN = "Refresh token is invalid or has been expired";
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository, JwtUtils jwtUtils,
                                   @Lazy PasswordEncoder passwordEncoder) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String assignRefreshToken(User user) {
        String rawRefreshToken = jwtUtils.generateRefreshToken(user.getId());
        String encodedRefreshToken = passwordEncoder.encode(rawRefreshToken);
        if (user.getRefreshToken() != null) {
            user.getRefreshToken().setToken(encodedRefreshToken);
        } else {
            user.setRefreshToken(new RefreshToken().withUser(user).withToken(encodedRefreshToken));
        }
        return rawRefreshToken;
    }

    @Override
    public void revokeRefreshToken(String refreshToken) {
        validateRefreshToken(refreshToken);
        RefreshToken currentRefreshToken = getRefreshToken(refreshToken);
        currentRefreshToken.revoke();
        refreshTokenRepository.delete(currentRefreshToken);
    }

    @Override
    public RefreshTokenResponse refreshAccessToken(String oldRefreshToken) {
        validateRefreshToken(oldRefreshToken);
        RefreshToken refreshToken = getRefreshToken(oldRefreshToken);
        String newRefreshToken = jwtUtils.generateRefreshToken(refreshToken.getId());
        refreshToken.setToken(passwordEncoder.encode(newRefreshToken));

        return RefreshTokenResponse.builder()
                .accessToken(jwtUtils.generateAccessToken(refreshToken.getUser().getEmail()))
                .refreshToken(newRefreshToken)
                .build();
    }

    private RefreshToken getRefreshToken(String rawRefreshToken) {
        Long userId = jwtUtils.getUserIdFromRefreshToken(rawRefreshToken);
        return refreshTokenRepository.findById(userId)
                .filter(refreshToken -> passwordEncoder.matches(rawRefreshToken, refreshToken.getToken()))
                .orElseThrow(() -> new UserAuthenticationException(UNPROCESSED_REFRESH_TOKEN));
    }

    private void validateRefreshToken(String rawRefreshToken) {
        if (!jwtUtils.isRefreshTokenValid(rawRefreshToken)) {
            throw new UserAuthenticationException(UNPROCESSED_REFRESH_TOKEN);
        }
    }
}
