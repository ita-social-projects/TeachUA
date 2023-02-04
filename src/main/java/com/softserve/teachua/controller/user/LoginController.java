package com.softserve.teachua.controller.user;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.security.RefreshTokenRequest;
import com.softserve.teachua.dto.security.RefreshTokenResponse;
import com.softserve.teachua.dto.user.SuccessLogin;
import com.softserve.teachua.dto.user.UserLogin;
import com.softserve.teachua.service.RefreshTokenService;
import com.softserve.teachua.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is for managing the login process.
 */

@RestController
@Tag(name = "login", description = "the Login API")
@SecurityRequirement(name = "api")
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
     * The controller returns dto {@code SuccessLogin} of sign-inned user.
     *
     * @param userLogin
     *            - dto with all params.
     *
     * @return new {@code SuccessLogin}.
     */
    @PreAuthorize("!isAuthenticated()")
    @PostMapping("/signin")
    public SuccessLogin signIn(@Valid @RequestBody UserLogin userLogin) {
        return userService.loginUser(userLogin);
    }

    @PreAuthorize("!isAuthenticated()")
    @PostMapping("/token/refresh")
    public RefreshTokenResponse refreshAccessToken(@Valid @RequestBody RefreshTokenRequest request) {
        return refreshTokenService.refreshAccessToken(request.getRefreshToken());
    }

    @PostMapping("/token/revoke")
    public ResponseEntity<?> revokeRefreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        refreshTokenService.revokeRefreshToken(request.getRefreshToken());
        return ResponseEntity.ok().build();
    }
}
