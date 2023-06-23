package com.softserve.user.controller;

import com.softserve.user.controller.marker.Api;
import com.softserve.user.dto.SuccessLogin;
import com.softserve.user.dto.SuccessRegistration;
import com.softserve.user.dto.SuccessVerification;
import com.softserve.user.dto.UserLogin;
import com.softserve.user.dto.UserProfile;
import com.softserve.user.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is for managing the login process.
 */

@Slf4j
@RestController
//@Tag(name = "login", description = "the Login API")
//@SecurityRequirement(name = "api")
@RequestMapping("/api/v1/auth")
public class AuthenticationController implements Api {
    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
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
     * The controller returns dto {@code SuccessRegistration} of sign-upped user.
     *
     * @param code - code of user verification
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
     * @param userProfile - dto with all params.
     * @return new {@code SuccessRegistration}.
     */
    @PreAuthorize("!isAuthenticated()")
    @PostMapping("/signup")
    public SuccessRegistration signUp(@Valid @RequestBody UserProfile userProfile) {
        log.debug("Controller \"signup\", userProfile = " + userProfile.toString());
        return userService.registerUser(userProfile);
    }
}
