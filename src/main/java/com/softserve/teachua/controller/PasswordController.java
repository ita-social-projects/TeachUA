package com.softserve.teachua.controller;


import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.user.UserVerifyPassword;
import com.softserve.teachua.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;


@RestController
public class PasswordController implements Api {

    private final UserService userService;

    @Autowired
    public PasswordController(UserService userService) {
        this.userService = userService;
    }

    /**
     * The controller returns dto {@code UserVerifyPassword} of sign-inned user.
     *
     * @param userLogin - dto with all params.
     * @return new {@code UserVerifyPassword}.
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/verify")
    public UserVerifyPassword verify(
            @Valid
            @RequestBody
                    UserVerifyPassword userLogin) {
        return userService.validateUser(userLogin);
    }
}
