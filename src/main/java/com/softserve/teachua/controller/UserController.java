package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.user.SuccessUpdatedUser;
import com.softserve.teachua.dto.user.UserPasswordUpdate;
import com.softserve.teachua.dto.user.UserResponse;
import com.softserve.teachua.dto.user.UserUpdateProfile;
import com.softserve.teachua.security.JwtProvider;
import com.softserve.teachua.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * This controller is for managing the users.
 */

@Slf4j
@RestController
@Tag(name = "user", description = "the User API")
@SecurityRequirement(name = "api")
public class UserController implements Api {
    private final UserService userService;
    private final JwtProvider jwtProvider;

    @Autowired
    public UserController(UserService userService, JwtProvider jwtProvider) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    /**
     * Use this endpoint to get user by id. The controller returns {@code UserResponse}.
     *
     * @param id
     *            - put user id.
     *
     * @return {@code UserResponse}.
     */
    @GetMapping("/user/{id}")
    public UserResponse getUserById(@PathVariable("id") Long id, HttpServletRequest httpServletRequest) {
        userService.validateUserId(id, httpServletRequest);
        return userService.getUserProfileById(id);
    }

    /**
     * Use this endpoint to get users by roleName. The controller returns {@code List<UserResponse>}.
     *
     * @param roleName
     *            - put role name.
     *
     * @return {@code List<UserResponse>}.
     */
    @GetMapping("/users/{role}")
    public List<UserResponse> getUsersByRole(@PathVariable("role") String roleName) {
        return userService.getUserResponsesByRole(roleName);
    }

    /**
     * Use this endpoint to get users. The controller returns {@code List <UserResponse>}.
     *
     * @return new {@code List <UserResponse>}.
     */
    @GetMapping("/users")
    public List<UserResponse> findAllUsers() {
        return userService.getListOfUsers();
    }

    /**
     * Use this endpoint to update user by id. The controller returns {@code SuccessUpdatedUser}.
     *
     * @param id
     *            - put user id.
     * @param userProfile
     *            - Place dto with all parameters for update existed user.
     * @param httpServletRequest
     *            - autowired by spring.
     *
     * @return {@code SuccessUpdatedUser}.
     */
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/user/{id}")
    public SuccessUpdatedUser updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateProfile userProfile,
            HttpServletRequest httpServletRequest) {
        userService.validateUserId(id, httpServletRequest);
        return userService.updateUser(id, userProfile);
    }

    /**
     * Use this endpoint to delete user by id. The controller returns {@code UserResponse}.
     */
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/user/{id}")
    public UserResponse deleteUser(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            auth.getAuthorities().stream().forEach(a -> a.getAuthority());
        } else {
            log.debug("auth is null");
        }
        return userService.deleteUserById(id);
    }

    /**
     * Use this endpoint to change user password. The controller returns {@code UserResponse}.
     *
     * @param httpServletRequest
     *            - autowired by spring.
     * @param passwordUpdate
     *            - password
     * @param id
     *            - id
     */
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/user/{id}")
    public void changePassword(@PathVariable("id") Long id, @Valid @RequestBody UserPasswordUpdate passwordUpdate,
            HttpServletRequest httpServletRequest) {
        userService.validateUserId(id, httpServletRequest);
        userService.updatePassword(id, passwordUpdate);
    }
}
