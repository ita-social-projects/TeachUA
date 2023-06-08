package com.softserve.user.controller;

import com.softserve.user.controller.marker.Api;
import com.softserve.user.dto.SuccessUserPasswordReset;
import com.softserve.user.dto.SuccessVerification;
import com.softserve.user.dto.UserResetPassword;
import com.softserve.user.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is for managing the reset password process.
 */

@Slf4j
@RestController
//@Tag(name = "reset password", description = "the Password Reset API")
//@SecurityRequirement(name = "api")
public class ResetPasswordController implements Api {
    private final UserService userService;

    @Autowired
    public ResetPasswordController(UserService userService) {
        this.userService = userService;
    }

    /**
     * The controller returns dto {@code SuccessUserPasswordReset} of user.
     *
     * @param userProfile
     *            - dto with all params.
     *
     * @return new {@code SuccessUserPasswordReset}.
     */
    @PreAuthorize("!isAuthenticated()")
    @PostMapping("/resetpassword")
    public SuccessUserPasswordReset resetPassword(@Valid @RequestBody UserResetPassword userProfile) {
        log.debug("Controller \"resetpassword\", userProfile = " + userProfile.toString());
        return userService.resetPassword(userProfile);
    }

    /**
     * The controller returns dto {@code SuccessRegistration} of sign-upped user.
     *
     * @param code
     *            - code of user verification
     *
     * @return new {@code SuccessRegistration}.
     */
    @PreAuthorize("!isAuthenticated()")
    @GetMapping("/verifyreset")
    public SuccessVerification verifyUser(@Param("code") String code) {
        log.info("Controller \"verifyreset\",  code = " + code);
        return userService.verifyChange(code);
    }

    /**
     * The controller returns dto {@code SuccessUserPasswordReset} of user.
     *
     * @param userProfile
     *            - dto with all params.
     *
     * @return new {@code SuccessUserPasswordReset}.
     */
    @PreAuthorize("!isAuthenticated()")
    @PostMapping("/verifyreset")
    public SuccessUserPasswordReset changePassword(@Valid @RequestBody SuccessUserPasswordReset userProfile) {
        return userService.verifyChangePassword(userProfile);
    }
}
