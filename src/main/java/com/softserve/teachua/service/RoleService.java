package com.softserve.teachua.service;

import com.softserve.teachua.dto.role.RoleProfile;
import com.softserve.teachua.dto.role.RoleResponse;
import com.softserve.teachua.model.Role;

import java.util.List;

public interface RoleService {
    List<RoleResponse> getListOfRoles();

    RoleResponse getRoleProfileById(Integer id);

    Role getRoleById(Integer id);

    RoleProfile updateRole(Integer id, RoleProfile roleProfile);

    Role findByName(String name);

    RoleProfile addNewRole(RoleProfile roleProfile);

    RoleResponse deleteRoleById(Integer id);
}
