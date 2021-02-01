package com.softserve.teachua.service;

import com.softserve.teachua.dto.RoleResponse;
import com.softserve.teachua.model.Role;

import java.util.List;

public interface RoleService {
    List<RoleResponse> getListOfRoles();

    Role findByName(String name);
}
