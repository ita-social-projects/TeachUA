package com.softserve.teachua.controller.user;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.user.SuccessRegistration;
import com.softserve.teachua.dto.user.UserProfile;
import com.softserve.teachua.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class RegistrationController implements Api {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    /**
     * The controller returns sign-up page.
     *
     * @return signup.
     */
    @GetMapping("/signup")
    public String signUp() {
        return "signup";
    }

    /**
     * The controller returns dto {@code SuccessRegistration} of sign-upped user.
     *
     * @param userProfile - dto with all params.
     * @return new {@code SuccessRegistration}.
     */
    @PostMapping("/signup")
    public SuccessRegistration signUp(
            @Valid
            @RequestBody
                    UserProfile userProfile) {
        return userService.registerUser(userProfile);
    }
}
