package com.softserve.user.controller;

import com.softserve.commons.constant.RoleData;
import com.softserve.user.dto.security.RefreshTokenRequest;
import com.softserve.user.dto.security.RefreshTokenResponse;
import com.softserve.user.service.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/jwt")
@Slf4j
public class TokenController {
    private final RefreshTokenService refreshTokenService;

    public TokenController(RefreshTokenService refreshTokenService) {
        this.refreshTokenService = refreshTokenService;
    }

    @GetMapping("/parse")
    public ResponseEntity<Void> parseJwtForNginx(
            @RequestHeader(value = "Authorization", defaultValue = "") String token) {
        log.info("Parsing token request");
        //todo
        System.out.println(token);
        HttpHeaders responseHeaders = new HttpHeaders();
        //todo
        if (token.isEmpty()) {
            responseHeaders.set("uid", "");
            responseHeaders.set("uname", "");
            responseHeaders.set("role", "");
        } else {
            responseHeaders.set("uid", "1");
            responseHeaders.set("uname", "testUsername");
            responseHeaders.set("role", String.valueOf(RoleData.ADMIN));
        }

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .build();
    }

    /**
     * The endpoint returns dto {@code RefreshTokenResponse} with access and refresh token.
     * Refresh token from request will be revoked.
     *
     * @param request {@code RefreshTokenRequest}.
     * @return new {@code SuccessLogin}.
     */
    @PreAuthorize("!isAuthenticated()")
    @PostMapping("/refresh")
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
    @PostMapping("/revoke")
    public void revokeRefreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        refreshTokenService.revokeRefreshToken(request.getRefreshToken());
    }
}
