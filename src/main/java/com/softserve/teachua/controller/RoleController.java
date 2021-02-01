package com.softserve.teachua.controller;

import com.softserve.teachua.dto.RoleResponce;
import org.springframework.web.bind.annotation.*;

@RestController
public class RoleController {

    /**
     * The method which return role.
     *
     * @return {@link RoleResponce}
     */
    @GetMapping("/role/{id}")
    public RoleResponce getRole(@PathVariable Long id) {
        // TODO
        return new RoleResponce();
    }

    /**
     * The method which return role.
     *
     * @return {@link RoleResponce}
     */
    @PostMapping("/role/{id}")
    public RoleResponce addRole() {
        // TODO
        return new RoleResponce();
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
     * @return {@link RoleResponce}
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
