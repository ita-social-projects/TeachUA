package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.user.SuccessUpdatedUser;
import com.softserve.teachua.dto.user.UserPasswordUpdate;
import com.softserve.teachua.dto.user.UserResponse;
import com.softserve.teachua.dto.user.UserUpdateProfile;
import com.softserve.teachua.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is for managing the users.
 */
@Slf4j
@RestController
@Tag(name = "user", description = "the User API")
@SecurityRequirement(name = "api")
public class UserController implements Api {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Use this endpoint to get user by id. Only accessible for ADMIN or profile owner.
     *
     * @param id - put user id.
     * @return {@code UserResponse}.
     */
    @PreAuthorize("hasRole('ADMIN') or (isAuthenticated() and authentication.principal.id == #id)")
    @GetMapping("/user/{id}")
    public UserResponse getUserById(@PathVariable("id") Long id) {
        return userService.getUserProfileById(id);
    }

    /**
     * Use this endpoint to get users by roleName. Only accessible for ADMIN.
     *
     * @param roleName - put role name.
     * @return {@code List<UserResponse>}.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/{role}")
    public List<UserResponse> getUsersByRole(@PathVariable("role") String roleName) {
        return userService.getUserResponsesByRole(roleName);
    }

    /**
     * Use this endpoint to get users. Only accessible for ADMIN.
     *
     * @return new {@code List <UserResponse>}.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public List<UserResponse> findAllUsers() {
        return userService.getListOfUsers();
    }

    /**
     * Use this endpoint to update user by id. Only accessible for ADMIN or profile owner.
     *
     * @param id          - put user id.
     * @param userProfile - Place dto with all parameters for update existed user.
     * @return {@code SuccessUpdatedUser}.
     */
    @PreAuthorize("hasRole('ADMIN') or (isAuthenticated() and authentication.principal.id == #id)")
    @PutMapping("/user/{id}")
    public SuccessUpdatedUser updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateProfile userProfile) {
        return userService.updateUser(id, userProfile);
    }

    /**
     * Use this endpoint to delete user by id. Only accessible for ADMIN or profile owner.
     */
    @PreAuthorize("hasRole('ADMIN') or (isAuthenticated() and authentication.principal.id == #id)")
    @DeleteMapping("/user/{id}")
    public UserResponse deleteUser(@PathVariable Long id) {
        return userService.deleteUserById(id);
    }

    /**
     * Use this endpoint to change user password. Only accessible for ADMIN or profile owner.
     *
     * @param passwordUpdate - password
     * @param id             - id
     */
    @PreAuthorize("hasRole('ADMIN') or (isAuthenticated() and authentication.principal.id == #id)")
    @PatchMapping("/user/{id}")
    public void changePassword(@PathVariable("id") Long id, @Valid @RequestBody UserPasswordUpdate passwordUpdate) {
        userService.updatePassword(id, passwordUpdate);
    }
}
