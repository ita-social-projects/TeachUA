package com.softserve.teachua.controller.user;

import com.softserve.teachua.dto.controller.SuccessRegistration;
import com.softserve.teachua.dto.service.UserProfile;
import com.softserve.teachua.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Slf4j
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping({"/signup", "/registration"})
    public ResponseEntity<Object> registration() {
        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .build();
    }

    @PostMapping({"/signup", "/registration"})
    public ResponseEntity<SuccessRegistration> signUp(
            @Valid
            @RequestBody
                    UserProfile userProfile) {
        log.info("**/signup userLogin = " + userProfile.getEmail());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.registerUser(userProfile));
    }
}
