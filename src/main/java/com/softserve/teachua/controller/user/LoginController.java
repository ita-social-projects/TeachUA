package com.softserve.teachua.controller.user;


import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.user.SuccessLogin;
import com.softserve.teachua.dto.user.UserLogin;
import com.softserve.teachua.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class LoginController implements Api {

    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    /**
     * The controller returns sign-in page.
     *
     * @return /signin.
     */
    @GetMapping("/signin")
    public String signIn() {
        return "signin";
    }

    /**
     * The controller returns dto {@code SuccessLogin} of sign-inned user.
     *
     * @param userLogin - dto with all params.
     * @return new {@code SuccessLogin}.
     */
    @PostMapping("/signin")
    public SuccessLogin signIn(
            @Valid
            @RequestBody
                    UserLogin userLogin) {
        return userService.validateUser(userLogin);
    }
}
