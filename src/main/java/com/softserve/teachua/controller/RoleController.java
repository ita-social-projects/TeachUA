package com.softserve.teachua.controller;

import com.softserve.teachua.dto.RoleResponse;
import org.springframework.web.bind.annotation.*;

@RestController
public class RoleController {

    /**
     * The method which return role.
     *
     * @return {@link RoleResponse}
     */
    @GetMapping("/role/{id}")
    public RoleResponse getRole(@PathVariable Long id) {
        // TODO
        return new RoleResponse();
    }

    /**
     * The method which return role.
     *
     * @return {@link RoleResponse}
     */
    @PostMapping("/role/{id}")
    public RoleResponse addRole() {
        // TODO
        return new RoleResponse();
    }

    /**
     * The method which return array of existing roles.
     *
     * @return {...}
     */
    @DeleteMapping("/role/{id}")
    public String deleteRole(@PathVariable Long id) {
        // TODO
        return "DeleteMapping, Method deleteRole, role id: " + id;
    }

    /**
     * The method which return array of existing roles.
     *
     * @return {@link RoleResponse}
     */
    @GetMapping("/roles")
    public String getRoles() {
        // TODO
        return "GetMapping, Method getRoles";
    }

    @PostMapping("/roles")
    public String addRoles() {
        // TODO
        return "PostMapping, Method addRoles";
    }

    @DeleteMapping("/roles")
    public String deleteRoles() {
        // TODO
        return "DeleteMapping, Method deleteRoles";
    }

}
