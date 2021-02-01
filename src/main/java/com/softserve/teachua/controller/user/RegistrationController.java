package com.softserve.teachua.controller.user;

import com.softserve.teachua.dto.controller.SuccessRegistration;
import com.softserve.teachua.dto.service.UserProfile;
import com.softserve.teachua.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    /**
     * The controller returns sign-up page.
     *
     * @return /signup.
     */
    @GetMapping("/signup")
    public String signup() {
        return "/signup";
    }

    /**
     * The controller returns dto {@code SuccessLogin} of sign-upped user.
     *
     * @param userProfile - dto with all params.
     * @return new {@code SuccessRegistration}.
     */
    @PostMapping("/signup")
    public ResponseEntity<SuccessRegistration> signUp(
            @Valid
            @RequestBody
                    UserProfile userProfile) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.registerUser(userProfile));
    }
}
