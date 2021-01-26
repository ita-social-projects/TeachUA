package com.softserve.teachua.controller;

import com.softserve.teachua.dto.controller.UserResponse;
import com.softserve.teachua.dto.service.UserProfile;
import com.softserve.teachua.model.User;
import com.softserve.teachua.service.UserService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user/{id}")
    public UserResponse findById(@PathVariable("id") Long id) {
        return userService.getUserProfileById(id);
    }

    @GetMapping("/users")
    public List<UserResponse> findAllUsers() {
        return userService.getListOfUsers();
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<UserProfile> updateById(@PathVariable("id") Long id) {
        userService.getUserProfileById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.updateUserProfileById(id));
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<UserResponse> deleteById(@PathVariable("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.deleteUserById(id));
    }

}
