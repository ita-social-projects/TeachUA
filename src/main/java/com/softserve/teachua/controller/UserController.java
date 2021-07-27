package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.user.*;
import com.softserve.teachua.model.User;
import com.softserve.teachua.security.JwtProvider;
import com.softserve.teachua.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
public class UserController implements Api {

    private final UserService userService;

    private final JwtProvider jwtProvider;


    @Autowired
    public UserController(UserService userService, JwtProvider jwtProvider) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    /**
     * The controller returns information {@code UserResponse} about user.
     *
     * @param id - put user id.
     * @return new {@code UserResponse}.
     */
    @GetMapping("/user/{id}")
    public UserResponse getUserById(@PathVariable("id") Long id, HttpServletRequest httpServletRequest) {

        userService.validateUserId(id, httpServletRequest);
        return userService.getUserProfileById(id);
    }


    @GetMapping("/user")
    public User getUserByEmail(@RequestParam("email") String email, HttpServletRequest httpServletRequest) {
        userService.validateUserId(userService.getUserByEmail(email).getId(), httpServletRequest);
        return userService.getUserByEmail(email);
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
            @RequestBody UserUpdateProfile userProfile, HttpServletRequest httpServletRequest) {
        userService.validateUserId(id, httpServletRequest);
        return userService.updateUser(id, userProfile);
    }
    @PutMapping("/user/update")
    public SuccessUpdatedUser updateUserByManager(
            @Valid
            @RequestBody UserUpdateProfile userProfile, HttpServletRequest httpServletRequest) {
//        userService.validateUserId(userProfile.getId(), httpServletRequest); == todo think about validation
        return userService.updateUser(userProfile.getId(), userProfile);
    }

    /**
     * The controller returns dto {@code UserResponse} of deleted user by id.
     *
     * @param id - put user id.
     * @return new {@code UserResponse}.
     */
    @DeleteMapping("/user/{id}")
    public UserResponse deleteUser(@PathVariable Long id) {
        log.info("=======");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth!=null){
            auth.getAuthorities().stream().forEach(a -> a.getAuthority());
        }else{
            System.out.println("auth is null");
        }
        return userService.deleteUserById(id);
    }


    @PostMapping("/resetPassword")
    public UserLogin resetPassword(
            @Valid
            @RequestBody UserLogin userProfile, HttpServletRequest httpServletRequest) {
        log.info("Controller \"reset\", userProfile = " + userProfile.toString());
      //  userService.validateUserId(userProfile., httpServletRequest);
        return userService.resetPassword(userProfile);//, userProfile);
    }
}
