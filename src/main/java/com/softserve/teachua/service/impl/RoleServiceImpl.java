package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.role.*;
import com.softserve.teachua.exception.*;
import com.softserve.teachua.model.Role;
import com.softserve.teachua.repository.RoleRepository;
import com.softserve.teachua.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {
    private static final String ROLE_ALREADY_EXIST = "Role already exist with name: %s";
    private static final String ROLE_NOT_FOUND_BY_ID = "Role not found by id: %s";
    private static final String ROLE_NOT_FOUND_BY_NAME = "Role not found by name: %s";

    private final RoleRepository roleRepository;
    private final DtoConverter dtoConverter;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, DtoConverter dtoConverter) {
        this.roleRepository = roleRepository;
        this.dtoConverter = dtoConverter;
    }

    @Override
    public List<RoleResponse> getListOfRoles() {
        log.info("**/getting all roles" );
        return roleRepository.findAll()
                .stream()
                .map(role -> new RoleResponse(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public RoleResponse getRoleResponseById(Integer id){
        Optional<Role> optionalRole = roleRepository.findById(id);
        if (!optionalRole.isPresent()) {
            String roleNotFoundById = String.format(ROLE_NOT_FOUND_BY_ID, id);
            log.error(roleNotFoundById);
            throw new NotExistException(roleNotFoundById);
        }
        log.info("**/getting RoleResponse by id = " + id);
        return dtoConverter.convertToDto(optionalRole.get(),RoleResponse.class);
    }

    @Override
    public RoleProfile updateRole(Integer id, RoleProfile roleProfile) {
        Optional<Role> optionalRole = roleRepository.findById(id);
        if (!optionalRole.isPresent()) {
            String roleNotFoundById = String.format(ROLE_NOT_FOUND_BY_ID, id);
            log.error(roleNotFoundById);
            throw new NotExistException(roleNotFoundById);
        }
        Role role = dtoConverter.convertToEntity(roleProfile, new Role());
        role.setId(id);
        log.info("**/updating role by id = " + id);
        return dtoConverter.convertToDto(roleRepository.save(role),RoleProfile.class);
    }

    @Override
    public Role findByName(String name) {
        Optional<Role> optionalRole = roleRepository.findByName(name);
        if (!optionalRole.isPresent()) {
            String roleNotFoundByName = String.format(ROLE_NOT_FOUND_BY_NAME, name);
            log.error(roleNotFoundByName);
            throw new NotExistException(roleNotFoundByName);
        }
        log.info("**/getting role by name = " + name);
        return optionalRole.get();
    }

    @Override
    public RoleProfile addNewRole(RoleProfile roleProfile) {
            if (roleRepository.findByName(roleProfile.getRoleName()).isPresent()) {
                String roleAlreadyExist = String.format(ROLE_ALREADY_EXIST, roleProfile.getRoleName());
                log.error(roleAlreadyExist);
                throw new AlreadyExistException(roleAlreadyExist);
            }
            Role role = roleRepository.save(dtoConverter.convertToEntity(roleProfile, new Role()));
            log.info("**/adding new role = " + roleProfile.getRoleName());
            return dtoConverter.convertToDto(role, RoleProfile.class);
    }
}
