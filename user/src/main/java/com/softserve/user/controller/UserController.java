package com.softserve.user.controller;

import com.softserve.commons.certificate.client.CertificateClient;
import com.softserve.commons.certificate.dto.CertificateUserResponse;
import com.softserve.commons.constant.RoleData;
import com.softserve.user.controller.marker.Api;
import com.softserve.user.dto.SuccessUpdatedUser;
import com.softserve.user.dto.UserPasswordUpdate;
import com.softserve.user.dto.UserResponse;
import com.softserve.user.dto.UserUpdateProfile;
import com.softserve.user.security.UserPrincipal;
import com.softserve.user.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is for managing the users.
 */
@Slf4j
@RestController
//@Tag(name = "user", description = "the User API")
//@SecurityRequirement(name = "api")
@RequestMapping("/api/v1/user")
public class UserController implements Api {
    private final UserService userService;
    private final CertificateClient certificateClient;

    @Autowired
    public UserController(UserService userService, CertificateClient certificateClient) {
        this.userService = userService;
        this.certificateClient = certificateClient;
    }

    /**
     * Use this endpoint to get user by id. Only accessible for ADMIN or profile owner.
     *
     * @param id - put user id.
     * @return {@code UserResponse}.
     */
    @PreAuthorize("hasRole('ADMIN') or (isAuthenticated() and authentication.principal.id == #id)")
    @GetMapping("/{id}")
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
    @GetMapping("/{role}")
    public List<UserResponse> getUsersByRole(@PathVariable("role") String roleName) {
        return userService.getUserResponsesByRole(roleName);
    }

    /**
     * Use this endpoint to get users. Only accessible for ADMIN.
     *
     * @return new {@code List <UserResponse>}.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
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
    @PutMapping("/{id}")
    public SuccessUpdatedUser updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateProfile userProfile) {
        return userService.updateUser(id, userProfile);
    }

    /**
     * Use this endpoint to delete user by id. Only accessible for ADMIN or profile owner.
     */
    @PreAuthorize("hasRole('ADMIN') or (isAuthenticated() and authentication.principal.id == #id)")
    @DeleteMapping("/{id}")
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
    @PatchMapping("/{id}")
    public void changePassword(@PathVariable("id") Long id, @Valid @RequestBody UserPasswordUpdate passwordUpdate) {
        userService.updatePassword(id, passwordUpdate);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/certificates")
    public List<CertificateUserResponse> getCertificatesOfAuthenticatedUser() {
        UserPrincipal userPrincipal =
                (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return certificateClient.getListOfCertificatesByUserEmail(userPrincipal.getUsername());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/existsById/{id}")
    public boolean existsById(@PathVariable("id") Long id) {
        return userService.existsById(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/role/{userId}")
    RoleData getUserRoleByUserId(@PathVariable("userId") Long id) {
        return RoleData.valueOf(userService.getUserById(id).getRole().getName().replace("ROLE_", ""));
    }
}
