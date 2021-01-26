package com.softserve.teachua.service;

import com.softserve.teachua.dto.RoleResponce;
import com.softserve.teachua.model.Role;

import java.util.List;

public interface RoleService {
    List<RoleResponce> getListOfRoles();

    Role findByName(String name);
}
