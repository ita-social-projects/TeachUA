package com.softserve.teachua.controller.test;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.test.test.CreateTest;
import com.softserve.teachua.dto.test.test.SuccessCreatedTest;
import com.softserve.teachua.model.User;
import com.softserve.teachua.service.UserService;
import com.softserve.teachua.service.test.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TestController implements Api {
    private final TestService testService;
    private final UserService userService;

    @GetMapping("/super_test")
    public String getString() {
        return "Hello world!";
    }

    @PostMapping("/tests")
    public SuccessCreatedTest addTest(@RequestBody CreateTest test) {
        return testService.addTest(test);
    }

    @GetMapping("/get_current")
    public User getUser() {
        return userService.getCurrentUser();
    }
}
