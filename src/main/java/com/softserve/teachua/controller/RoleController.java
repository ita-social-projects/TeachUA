package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.role.RoleProfile;
import com.softserve.teachua.dto.role.RoleResponse;
import com.softserve.teachua.service.RoleService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Tag(name="role", description="the Role API")
@SecurityRequirement(name = "api")
public class RoleController implements Api {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * Use this endpoint to return a role.
     * The controller returns {@code RoleResponse}.
     * @param id - put role id.
     * @return {@code RoleResponse}
     */
    @GetMapping("/role/{id}")
    public RoleResponse getRole(@PathVariable Integer id) {
        return roleService.getRoleProfileById(id);
    }

    /**
     * Use this endpoint to add a new role.
     * The controller returns {@code RoleProfile}.
     * @param roleProfile - put json role here.
     * @return new {@code RoleProfile}
     */
    @PostMapping("/role")
    public RoleProfile addRole(@Valid @RequestBody RoleProfile roleProfile) {
        return roleService.addNewRole(roleProfile);
    }

    /**
     * Use this endpoint to update existing role.
     * The controller returns {@code RoleProfile}.
     * @param id          - put role id.
     * @param roleProfile - put json role
     * @return new {@code RoleProfile}
     */
    @PutMapping("/role/{id}")
    public RoleProfile addRole(
            @PathVariable Integer id,
            @Valid
            @RequestBody RoleProfile roleProfile) {
        return roleService.updateRole(id, roleProfile);
    }

    /**
     * Use this endpoint to delete role by id.
     * The controller returns {@code RoleResponse}.
     * @param id - put role id here.
     * @return {@code RoleResponse}
     */
    @DeleteMapping("/role/{id}")
    public RoleResponse deleteRole(@PathVariable Integer id) {
        return roleService.deleteRoleById(id);
    }


    /**
     * Use this endpoint to return array of existing roles.
     * The controller returns {@code List<RoleResponse>}.
     * @return {@code List<RoleResponse>}
     */
    @GetMapping("/roles")
    public List<RoleResponse> getRoles() {
        return roleService.getListOfRoles();
    }
}
