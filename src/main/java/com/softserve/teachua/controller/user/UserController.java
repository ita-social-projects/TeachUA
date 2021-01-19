package com.softserve.teachua.controller.user;

import com.softserve.teachua.dto.service.UserProfile;
import com.softserve.teachua.model.User;
import com.softserve.teachua.service.UserService;
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
    public UserProfile findById(@PathVariable("id") Long id) {
        log.info("**/getting user by id = " + userService.getUserProfileById(id));
        return userService.getUserProfileById(id);
    }

    @PostMapping("/users")
    public List<User> findAllUsers() {
        log.info("**/getting list of users = " + userService.getListOfUsers());
        return userService.getListOfUsers();
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<UserProfile> updateById(@PathVariable("id") Long id) {
        log.info("**/ = " + userService.getUserProfileById(id));
        userService.getUserProfileById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.updateUserProfileById(id));
    }

    @DeleteMapping("/user//{id}")
    public ResponseEntity<UserProfile> deleteById(@PathVariable("id") Long id) {
        log.info("**/delete user = " + id);
        userService.getUserProfileById(id);
        return userService.deleteUserById(id);
    }

}
