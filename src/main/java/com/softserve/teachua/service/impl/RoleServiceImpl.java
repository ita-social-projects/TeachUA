package com.softserve.teachua.service.impl;

import com.softserve.teachua.dto.RoleResponse;
import com.softserve.teachua.model.Role;
import com.softserve.teachua.repository.RoleRepository;
import com.softserve.teachua.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<RoleResponse> getListOfRoles() {
        return roleRepository.findAll()
                .stream()
                .map(role -> new RoleResponse(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }
}
