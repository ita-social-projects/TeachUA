package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.role.RoleProfile;
import com.softserve.teachua.dto.role.RoleResponse;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.DatabaseRepositoryException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Role;
import com.softserve.teachua.repository.RoleRepository;
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
public class RoleServiceImpl implements RoleService {
    private static final String ROLE_ALREADY_EXIST = "Role already exist with name: %s";
    private static final String ROLE_NOT_FOUND_BY_ID = "Role not found by id: %s";
    private static final String ROLE_NOT_FOUND_BY_NAME = "Role not found by name: %s";
    private static final String ROLE_DELETING_ERROR = "Can't delete role cause of relationship";

    private final RoleRepository roleRepository;
    private final ArchiveService archiveService;
    private final DtoConverter dtoConverter;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, ArchiveService archiveService, DtoConverter dtoConverter) {
        this.roleRepository = roleRepository;
        this.archiveService = archiveService;
        this.dtoConverter = dtoConverter;
    }

    /**
     * The method returns list of existing {@link  Role}.
     *
     * @return list of {@link  Role}.
     */
    @Override
    public List<RoleResponse> getListOfRoles() {
        log.info("**/getting all roles");
        return roleRepository.findAll()
                .stream()
                .map(role -> new RoleResponse(role.getId(), role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public RoleResponse getRoleProfileById(Integer id) {
        return dtoConverter.convertToDto(getRoleById(id), RoleResponse.class);
    }

    /**
     * The method returns {@link  Role} by role id.
     *
     * @param id - put role id.
     * @return Role {@link  Role}.
     * @throws NotExistException {@link NotExistException} if the role doesn't exist.
     */
    @Override
    public Role getRoleById(Integer id) {
        Optional<Role> optionalRole = getOptionalRoleById(id);
        if (!optionalRole.isPresent()) {
            throw new NotExistException(String.format(ROLE_NOT_FOUND_BY_ID, id));
        }

        log.info("**/getting RoleResponse by id = " + id);
        return optionalRole.get();
    }

    /**
     * The method updates existing role by role id.
     *
     * @param id          - put role id.
     * @param roleProfile - put RoleProfile to updating role
     * @return RoleProfile {@link  RoleProfile}.
     * @throws NotExistException {@link NotExistException} if the role doesn't exist.
     */
    @Override
    public RoleProfile updateRole(Integer id, RoleProfile roleProfile) {
        Role role = getRoleById(id);
        Role newRole = dtoConverter.convertToEntity(roleProfile, role)
                .withId(id);

        log.info("**/updating role by id = " + newRole);
        return dtoConverter.convertToDto(roleRepository.save(newRole), RoleProfile.class);
    }

    /**
     * The method returns {@link  Role} by role name.
     *
     * @param name - put role name.
     * @return RoleResponse {@link  RoleResponse}.
     * @throws NotExistException {@link NotExistException} if the role doesn't exist.
     */
    @Override
    public Role findByName(String name) {
        Optional<Role> optionalRole = roleRepository.findByName(name);
        if (!optionalRole.isPresent()) {
            throw new NotExistException(String.format(ROLE_NOT_FOUND_BY_NAME, name));
        }

        log.info("**/getting role by name = " + name);
        return optionalRole.get();
    }

    /**
     * The method adds new role {@link  Role}
     *
     * @param roleProfile - put RoleProfile to adding new role
     * @return RoleProfile {@link  RoleProfile}.
     * @throws AlreadyExistException {@link AlreadyExistException} if the same role already exists.
     */
    @Override
    public RoleProfile addNewRole(RoleProfile roleProfile) {
        if (isRoleExistByName(roleProfile.getRoleName())) {
            throw new AlreadyExistException(String.format(ROLE_ALREADY_EXIST, roleProfile.getRoleName()));
        }

        Role role = roleRepository.save(dtoConverter.convertToEntity(roleProfile, new Role()));
        log.info("**/adding new role = " + roleProfile.getRoleName());
        return dtoConverter.convertToDto(role, RoleProfile.class);
    }

    /**
     * The method deletes role {@link  Role}
     *
     * @param id - id of role to delete
     * @return RoleRespone {@link  RoleResponse}.
     * @throws NotExistException {@link NotExistException} if the role doesn't exist.
     * @throws DatabaseRepositoryException {@link DatabaseRepositoryException} if role has re.
     */
    @Override
    public RoleResponse deleteRoleById(Integer id) {
        Role role = getRoleById(id);

        archiveService.saveModel(role);

        try {
            roleRepository.deleteById(id);
            roleRepository.flush();
        } catch (DataAccessException | ValidationException e) {
            throw new DatabaseRepositoryException(ROLE_DELETING_ERROR);
        }

        log.info("role {} was successfully deleted", role);
        return dtoConverter.convertToDto(role, RoleResponse.class);
    }

    private boolean isRoleExistByName(String name) {
        return roleRepository.existsByName(name);
    }
    private Optional<Role> getOptionalRoleById(Integer id) {
        return roleRepository.findById(id);
    }
}
