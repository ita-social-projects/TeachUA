package com.softserve.user.controller;

import com.softserve.user.controller.marker.Api;
import com.softserve.user.dto.SuccessLogin;
import com.softserve.user.dto.UserLogin;
import com.softserve.user.dto.security.RefreshTokenRequest;
import com.softserve.user.dto.security.RefreshTokenResponse;
import com.softserve.user.service.RefreshTokenService;
import com.softserve.user.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is for managing the login process.
 */

@RestController
//@Tag(name = "login", description = "the Login API")
//@SecurityRequirement(name = "api")
@Slf4j
public class LoginController implements Api {
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public LoginController(UserService userService,
                           RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
    }

    /**
     * The endpoint returns dto {@code SuccessLogin} of sign-inned user.
     *
     * @param userLogin - dto with all params.
     * @return new {@code SuccessLogin}.
     */
    @PreAuthorize("!isAuthenticated()")
    @PostMapping("/signin")
    public SuccessLogin signIn(@Valid @RequestBody UserLogin userLogin) {
        return userService.loginUser(userLogin);
    }

    /**
     * The endpoint returns dto {@code RefreshTokenResponse} with access and refresh token.
     * Refresh token from request will be revoked.
     *
     * @param request {@code RefreshTokenRequest}.
     * @return new {@code SuccessLogin}.
     */
    @PreAuthorize("!isAuthenticated()")
    @PostMapping("/token/refresh")
    public RefreshTokenResponse refreshAccessToken(@Valid @RequestBody RefreshTokenRequest request) {
        log.info("Refresh token request");
        return refreshTokenService.refreshAccessToken(request.getRefreshToken());
    }

    /**
     * The endpoint revokes requested refresh token.
     *
     * @param request {@code RefreshTokenRequest}.
     */
    @PreAuthorize("!isAuthenticated()")
    @PostMapping("/token/revoke")
    public void revokeRefreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        refreshTokenService.revokeRefreshToken(request.getRefreshToken());
    }
}
