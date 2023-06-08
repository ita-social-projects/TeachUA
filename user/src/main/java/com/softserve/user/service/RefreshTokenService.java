package com.softserve.user.service;

import com.softserve.user.dto.security.RefreshTokenResponse;
import com.softserve.user.model.User;

/**
 * This interface contains all needed methods to manage refresh tokens.
 */
public interface RefreshTokenService {
    /**
     * Generates new refresh token and assigns it to user.
     *
     * @param user {@code User}
     * @return generated refresh token
     */
    String assignRefreshToken(User user);

    /**
     * Revokes refresh token. Will validate token and get it from database.
     *
     * @param refreshToken {@code String}
     * @throws UserAuthenticationException if refresh token is not in use
     */
    void revokeRefreshToken(String refreshToken);

    /**
     * Generates new refresh token and assigns it to user. Will validate token
     * and get it from database. Previous refresh token will be revoked
     *
     * @param refreshToken {@code String}
     * @return {@code RefreshTokenResponse} with new access and refresh token
     * @throws UserAuthenticationException if refresh token is not in use
     */
    RefreshTokenResponse refreshAccessToken(String refreshToken);
}
