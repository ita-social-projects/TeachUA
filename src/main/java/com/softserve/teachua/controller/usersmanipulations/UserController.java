package com.softserve.teachua.controller.usersmanipulations;

import com.softserve.teachua.dto.RoleResponce;
import com.softserve.teachua.dto.security.UserEntity;
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

    @GetMapping("/user/byId/{id}")
    public UserEntity findById(@PathVariable("id") Long id) {
        log.info("**/getting user by id = " + userService.getUserEntityById(id));
        return userService.getUserEntityById(id);
    }

    @GetMapping("/user/byId/{email}")
    public UserEntity findByEmail(@PathVariable("email") String email) {
        log.info("**/getting user by email = " + userService.getUserEntity(email));
        return userService.getUserEntity(email);
    }

    @PostMapping("/users")
    public List<User> findAllUsers() {
        log.info("**/getting list of users = " + userService.getListOfUsers());
        return userService.getListOfUsers();
    }

    @PutMapping("/user/byId/{id}")
    public ResponseEntity<UserEntity> updateById(@PathVariable("id") Long id) {
        log.info("**/ = " + userService.getUserEntityById(id));
        userService.getUserEntityById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.updateUserEntityById(id));
    }

    @DeleteMapping("/user/byId/{id}")
    public ResponseEntity<UserEntity> deleteById(@PathVariable("id") Long id) {
        log.info("**/delete user = " + id);
        userService.getUserEntityById(id);
        return userService.deleteUserById(id);
    }

    // TODO move
    @GetMapping("/roles")
    public List<RoleResponce> listRoles() {
        log.info("/roles");
        return userService.getAllRoles();
    }
}
