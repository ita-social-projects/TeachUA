package com.softserve.user.controller;

import com.softserve.user.controller.marker.Api;
import com.softserve.user.dto.SuccessRegistration;
import com.softserve.user.dto.SuccessVerification;
import com.softserve.user.dto.UserProfile;
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
 * This controller is for managing the registration process.
 */

@RestController
@Slf4j
//@Tag(name = "registration", description = "the Registration API")
//@SecurityRequirement(name = "api")
public class RegistrationController implements Api {
    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
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
    @GetMapping("/verify")
    public SuccessVerification verifyUser(@Param("code") String code) {
        log.debug("Controller \"verify\",  code = " + code);
        return userService.verify(code);
    }

    /**
     * The controller returns dto {@code SuccessRegistration} of sign-upped user.
     *
     * @param userProfile
     *            - dto with all params.
     *
     * @return new {@code SuccessRegistration}.
     */
    @PreAuthorize("!isAuthenticated()")
    @PostMapping("/signup")
    public SuccessRegistration signUp(@Valid @RequestBody UserProfile userProfile) {
        log.debug("Controller \"signup\", userProfile = " + userProfile.toString());
        return userService.registerUser(userProfile);
    }
}
