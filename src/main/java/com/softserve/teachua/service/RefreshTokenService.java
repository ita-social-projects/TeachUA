package com.softserve.teachua.service;

import com.softserve.teachua.dto.security.RefreshTokenResponse;
import com.softserve.teachua.model.User;

/**
 * This interface contains all needed methods to manage refresh tokens.
 */
public interface RefreshTokenService {
    String assignRefreshToken(User user);

    void revokeRefreshToken(String refreshToken);

    RefreshTokenResponse refreshAccessToken(String refreshToken);
}
