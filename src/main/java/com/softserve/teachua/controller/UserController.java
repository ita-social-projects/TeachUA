package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.user.SuccessUpdatedUser;
import com.softserve.teachua.dto.user.UserProfile;
import com.softserve.teachua.dto.user.UserResponse;
import com.softserve.teachua.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController implements Api {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * The controller returns information {@code UserResponse} about user.
     *
     * @param id - put user id.
     * @return new {@code UserResponse}.
     */
    @GetMapping("/user/{id}")
    public UserResponse findById(@PathVariable("id") Long id) {
        return userService.getUserProfileById(id);
    }

    /**
     * The controller returns information {@code List <UserResponse>} about users.
     *
     * @return new {@code List <UserResponse>}.
     */
    @GetMapping("/users")
    public List<UserResponse> findAllUsers() {
        return userService.getListOfUsers();
    }

    /**
     * The controller returns information {@code UserResponse} about updated user.
     *
     * @param userProfile - Place dto with all parameters for update existed user.
     * @return new {@code UserProfile}.
     */
    @PutMapping("/user/{id}")
    public SuccessUpdatedUser updateUser(
            @PathVariable Long id,
            @Valid
            @RequestBody UserProfile userProfile) {
        return userService.updateUser(id, userProfile);
    }

    /**
     * The controller returns dto {@code UserResponse} of deleted user by id.
     *
     * @param id - put user id.
     * @return new {@code UserResponse}.
     */
    @DeleteMapping("/user/{id}")
    public UserResponse deleteUser(@PathVariable Long id) {
        return userService.deleteUserById(id);
    }
}
