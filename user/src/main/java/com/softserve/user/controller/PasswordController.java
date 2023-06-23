package com.softserve.user.controller;

import com.softserve.user.controller.marker.Api;
import com.softserve.user.dto.SuccessUserPasswordReset;
import com.softserve.user.dto.UserResetPassword;
import com.softserve.user.dto.UserVerifyPassword;
import com.softserve.user.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is for managing the password.
 */

@Slf4j
@RestController
//@Tag(name = "password", description = "the Password API")
//@SecurityRequirement(name = "api")
@RequestMapping("/api/v1/auth/password")
public class PasswordController implements Api {
    private final UserService userService;

    public PasswordController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Use this endpoint to create new password for signed-in users. The controller returns {@code UserVerifyPassword}.
     *
     * @param userLogin - dto with all params.
     * @return new {@code UserVerifyPassword}.
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/verify")
    public UserVerifyPassword verify(@Valid @RequestBody UserVerifyPassword userLogin) {
        return userService.validateUser(userLogin);
    }

    /**
     * The controller returns dto {@code SuccessUserPasswordReset} of user.
     *
     * @param userProfile - dto with all params.
     * @return new {@code SuccessUserPasswordReset}.
     */
    @PreAuthorize("!isAuthenticated()")
    @PostMapping("/reset")
    public SuccessUserPasswordReset resetPassword(@Valid @RequestBody UserResetPassword userProfile) {
        log.debug("Controller \"resetpassword\", userProfile = " + userProfile.toString());
        return userService.resetPassword(userProfile);
    }

    /**
     * The controller returns dto {@code SuccessUserPasswordReset} of user.
     *
     * @param userProfile - dto with all params.
     * @return new {@code SuccessUserPasswordReset}.
     */
    @PreAuthorize("!isAuthenticated()")
    @PostMapping("/reset/verify")
    public SuccessUserPasswordReset changePassword(@Valid @RequestBody SuccessUserPasswordReset userProfile) {
        return userService.verifyChangePassword(userProfile);
    }
}
