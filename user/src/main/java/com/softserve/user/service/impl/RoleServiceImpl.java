package com.softserve.user.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.amqp.message_producer.impl.ArchiveMQMessageProducer;
import com.softserve.commons.client.ArchiveClient;
import com.softserve.commons.exception.AlreadyExistException;
import com.softserve.commons.exception.DatabaseRepositoryException;
import com.softserve.commons.exception.NotExistException;
import com.softserve.commons.util.converter.DtoConverter;
import com.softserve.user.dto.role.RoleProfile;
import com.softserve.user.dto.role.RoleResponse;
import com.softserve.user.model.Role;
import com.softserve.user.repository.RoleRepository;
import com.softserve.user.service.RoleService;
import jakarta.validation.ValidationException;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class RoleServiceImpl implements RoleService {
    private static final String ROLE_ALREADY_EXIST = "Role already exist with name: %s";
    private static final String ROLE_NOT_FOUND_BY_ID = "Role not found by id: %s";
    private static final String ROLE_NOT_FOUND_BY_NAME = "Role not found by name: %s";
    private static final String ROLE_DELETING_ERROR = "Can't delete role cause of relationship";
    private final RoleRepository roleRepository;
    private final DtoConverter dtoConverter;
    private final ObjectMapper objectMapper;
    private final ArchiveMQMessageProducer<Role> archiveMQMessageProducer;
    private final ArchiveClient archiveClient;

    public RoleServiceImpl(RoleRepository roleRepository, DtoConverter dtoConverter,
                           ObjectMapper objectMapper, ArchiveMQMessageProducer<Role> archiveMQMessageProducer,
                           ArchiveClient archiveClient) {
        this.roleRepository = roleRepository;
        this.dtoConverter = dtoConverter;
        this.objectMapper = objectMapper;
        this.archiveMQMessageProducer = archiveMQMessageProducer;
        this.archiveClient = archiveClient;
    }

    @Override
    public List<RoleResponse> getListOfRoles() {
        log.debug("**/getting all roles");
        return roleRepository.findAll().stream()
                .map(role -> (RoleResponse) dtoConverter.convertToDto(role, RoleResponse.class))
                .toList();
    }

    @Override
    public RoleResponse getRoleProfileById(Integer id) {
        return dtoConverter.convertToDto(getRoleById(id), RoleResponse.class);
    }

    @Override
    public Role getRoleById(Integer id) {
        Optional<Role> optionalRole = getOptionalRoleById(id);
        if (optionalRole.isEmpty()) {
            throw new NotExistException(String.format(ROLE_NOT_FOUND_BY_ID, id));
        }

        log.debug("**/getting RoleResponse by id = " + id);
        return optionalRole.get();
    }

    @Override
    public RoleProfile updateRole(Integer id, RoleProfile roleProfile) {
        Role role = getRoleById(id);
        Role newRole = dtoConverter.convertToEntity(roleProfile, role).withId(id);

        log.debug("**/updating role by id = " + newRole);
        return dtoConverter.convertToDto(roleRepository.save(newRole), RoleProfile.class);
    }

    @Override
    public Role findByName(String name) {
        Optional<Role> optionalRole = roleRepository.findByName(name);
        if (optionalRole.isEmpty()) {
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

    private void archiveModel(Role role) {
        archiveMQMessageProducer.publish(role);
    }

    @Override
    public void restoreModel(Integer id) {
        var role = objectMapper.convertValue(
                archiveClient.restoreModel(Role.class.getName(), id),
                Role.class);
        roleRepository.save(role);
    }
}
