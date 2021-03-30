package com.softserve.teachua.controller.user;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.user.SuccessRegistration;
import com.softserve.teachua.dto.user.SuccessVerification;
import com.softserve.teachua.dto.user.UserProfile;
import com.softserve.teachua.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class RegistrationController implements Api {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }


    /**
     * The controller returns dto {@code SuccessRegistration} of sign-upped user.
     *
     * @param code - code of user verification
     * @return new {@code SuccessRegistration}.
     */
    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code) {
        userService.verify(code);
        return "Користувач успішно зареєстрований";
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
                    UserProfile userProfile, HttpServletRequest request) {
        return userService.registerUser(userProfile, request);
    }
}
