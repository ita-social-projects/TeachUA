package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.role.RoleProfile;
import com.softserve.teachua.dto.role.RoleResponse;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.DatabaseRepositoryException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Role;
import com.softserve.teachua.model.archivable.RoleArch;
import com.softserve.teachua.repository.RoleRepository;
import com.softserve.teachua.service.ArchiveMark;
import com.softserve.teachua.service.ArchiveService;
import com.softserve.teachua.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class RoleServiceImpl implements RoleService, ArchiveMark<Role> {
    private static final String ROLE_ALREADY_EXIST = "Role already exist with name: %s";
    private static final String ROLE_NOT_FOUND_BY_ID = "Role not found by id: %s";
    private static final String ROLE_NOT_FOUND_BY_NAME = "Role not found by name: %s";
    private static final String ROLE_DELETING_ERROR = "Can't delete role cause of relationship";
    private static final String ROLE_PREFIX = "ROLE_";

    private final RoleRepository roleRepository;
    private final ArchiveService archiveService;
    private final DtoConverter dtoConverter;
    private final ObjectMapper objectMapper;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository,
                           ArchiveService archiveService,
                           DtoConverter dtoConverter,
                           ObjectMapper objectMapper) {
        this.roleRepository = roleRepository;
        this.archiveService = archiveService;
        this.dtoConverter = dtoConverter;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<RoleResponse> getListOfRoles() {
        log.debug("**/getting all roles");
        return roleRepository.findAll()
                .stream()
                .map(role -> (RoleResponse) dtoConverter.convertToDto(role, RoleResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public RoleResponse getRoleProfileById(Integer id) {
        return dtoConverter.convertToDto(getRoleById(id), RoleResponse.class);
    }

    @Override
    public Role getRoleById(Integer id) {
        Optional<Role> optionalRole = getOptionalRoleById(id);
        if (!optionalRole.isPresent()) {
            throw new NotExistException(String.format(ROLE_NOT_FOUND_BY_ID, id));
        }

        log.debug("**/getting RoleResponse by id = " + id);
        return optionalRole.get();
    }

    @Override
    public RoleProfile updateRole(Integer id, RoleProfile roleProfile) {
        Role role = getRoleById(id);
        Role newRole = dtoConverter.convertToEntity(roleProfile, role)
                .withId(id);

        log.debug("**/updating role by id = " + newRole);
        return dtoConverter.convertToDto(roleRepository.save(newRole), RoleProfile.class);
    }

    @Override
    public Role findByName(String name) {
        Optional<Role> optionalRole = roleRepository.findByName(ROLE_PREFIX+name);
        if (!optionalRole.isPresent()) {
            throw new NotExistException(String.format(ROLE_NOT_FOUND_BY_NAME, name));
        }

        log.debug("**/getting role by name = " + name);
        return optionalRole.get();
    }

    @Override
    public RoleProfile addNewRole(RoleProfile roleProfile) {
        if (isRoleExistByName(roleProfile.getRoleName())) {
            throw new AlreadyExistException(String.format(ROLE_ALREADY_EXIST, roleProfile.getRoleName()));
        }

        Role role = roleRepository.save(dtoConverter.convertToEntity(roleProfile, new Role()));
        log.debug("**/adding new role = " + roleProfile.getRoleName());
        return dtoConverter.convertToDto(role, RoleProfile.class);
    }

    @Override
    public RoleResponse deleteRoleById(Integer id) {
        Role role = getRoleById(id);

        try {
            roleRepository.deleteById(id);
            roleRepository.flush();
        } catch (DataAccessException | ValidationException e) {
            throw new DatabaseRepositoryException(ROLE_DELETING_ERROR);
        }

        archiveModel(role);

        log.debug("role {} was successfully deleted", role);
        return dtoConverter.convertToDto(role, RoleResponse.class);
    }

    private boolean isRoleExistByName(String name) {
        return roleRepository.existsByName(name);
    }

    private Optional<Role> getOptionalRoleById(Integer id) {
        return roleRepository.findById(id);
    }

    @Override
    public void archiveModel(Role role) {
        RoleArch roleArch = dtoConverter.convertToDto(role, RoleArch.class);
        archiveService.saveModel(roleArch);
    }

    @Override
    public void restoreModel(String archiveObject) throws JsonProcessingException {
        RoleArch roleArch = objectMapper.readValue(archiveObject, RoleArch.class);
        roleRepository.save(dtoConverter.convertToEntity(roleArch, Role.builder().build()));
    }
}
