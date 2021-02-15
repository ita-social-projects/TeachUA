package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.role.RoleProfile;
import com.softserve.teachua.dto.role.RoleResponse;
import com.softserve.teachua.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class RoleController implements Api {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * The method which return role.
     *
     * @param id - put role id.
     * @return {@link RoleResponse}
     */
    @GetMapping("/role/{id}")
    public RoleResponse getRole(@PathVariable Integer id) {
        return roleService.getRoleProfileById(id);
    }

    /**
     * The method which adds a new role.
     *
     * @param roleProfile - put json role
     * @return {@link RoleProfile}
     */
    @PostMapping("/role")
    public RoleProfile addRole(@Valid @RequestBody RoleProfile roleProfile) {
        return roleService.addNewRole(roleProfile);
    }

    /**
     * The method which updates existing role.
     *
     * @param id          - put role id.
     * @param roleProfile - put json role
     * @return {@link RoleProfile}
     */
    @PutMapping("/role/{id}")
    public RoleProfile addRole(
            @PathVariable Integer id,
            @Valid
            @RequestBody RoleProfile roleProfile) {
        return roleService.updateRole(id, roleProfile);
    }

    /**
     * The method deletes role by id.
     *
     * @return {...}
     */
    @DeleteMapping("/role/{id}")
    public RoleResponse deleteRole(@PathVariable Integer id) {
        return roleService.deleteRoleById(id);
    }


    /**
     * The method which return array of existing roles.
     *
     * @return {@link RoleResponse}
     */
    @GetMapping("/roles")
    public List<RoleResponse> getRoles() {
        return roleService.getListOfRoles();
    }
}
