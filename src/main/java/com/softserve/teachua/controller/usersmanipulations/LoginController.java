package com.softserve.teachua.controller.usersmanipulations;


import com.softserve.teachua.dto.controller.SuccessLogin;
import com.softserve.teachua.dto.service.UserLogin;
import com.softserve.teachua.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Slf4j
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping({"/signin", "/login"})
    public String registration() {
        return "login";
    }

    @PostMapping({"/signin", "/login"})
    public SuccessLogin signIn(
            @Valid
            @RequestBody
                    UserLogin userLogin) {
        log.info("**/signin userLogin by email = " + userLogin.getEmail());
        return userService.validateUser(userLogin);
    }
}
