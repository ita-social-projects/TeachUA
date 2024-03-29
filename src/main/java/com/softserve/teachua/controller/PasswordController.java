package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.user.UserVerifyPassword;
import com.softserve.teachua.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is for managing the password.
 */

@RestController
@Tag(name = "password", description = "the Password API")
@SecurityRequirement(name = "api")
public class PasswordController implements Api {
    private final UserService userService;

    @Autowired
    public PasswordController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Use this endpoint to create new password for signed-in users. The controller returns {@code UserVerifyPassword}.
     *
     * @param userLogin
     *            - dto with all params.
     *
     * @return new {@code UserVerifyPassword}.
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/verify")
    public UserVerifyPassword verify(@Valid @RequestBody UserVerifyPassword userLogin) {
        return userService.validateUser(userLogin);
    }
}
