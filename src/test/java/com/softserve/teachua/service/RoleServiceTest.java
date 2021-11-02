package com.softserve.teachua.service;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.role.RoleProfile;
import com.softserve.teachua.dto.role.RoleResponse;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Role;
import com.softserve.teachua.repository.RoleRepository;
import com.softserve.teachua.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {
    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ArchiveService archiveService;

    @Mock
    private DtoConverter dtoConverter;

    @InjectMocks
    private RoleServiceImpl roleService;
    private Role role;
    private Role correctRole;
    private RoleProfile roleProfile;
    private RoleResponse correctRoleResponse;

    private final int CORRECT_ID = 1;
    private final int NOT_EXISTING_ID = 50;

    private final String EXISTING_NAME = "ROLE_ADMIN";
    private final String NOT_EXISTING_NAME = "NOT_EXIST";
    private final String NEW_NAME = "NEW_ROLE";

    @BeforeEach
    public void init() {
        role = Role.builder().id(CORRECT_ID).name(EXISTING_NAME).build();
        correctRole = Role.builder().id(CORRECT_ID).name(EXISTING_NAME).build();
        roleProfile = new RoleProfile(NEW_NAME);
    }

    @Test
    public void getRoleByCorrectIdShouldReturnCorrectRole() {
        when(roleRepository.findById(CORRECT_ID)).thenReturn(Optional.of(correctRole));
        Role actual = roleService.getRoleById(CORRECT_ID);
        assertEquals(actual, correctRole);
    }

    @Test
    public void getRoleByWrongIdShouldReturnCorrectRole() {
        when(roleRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            roleService.getRoleById(NOT_EXISTING_ID);
        }).isInstanceOf(NotExistException.class);
    }

    @Test
    public void updateRoleTest() {
        when(roleRepository.findById(CORRECT_ID)).thenReturn(Optional.of(role));
        when(roleRepository.save(any())).thenReturn(role);
        when(dtoConverter.convertToEntity(roleProfile, role)).thenReturn(Role.builder()
                .id(CORRECT_ID).name(roleProfile.getRoleName()).build());
        when(dtoConverter.convertToDto(role, RoleProfile.class)).thenReturn(new RoleProfile(roleProfile.getRoleName()));

        RoleProfile updatedRole = roleService.updateRole(CORRECT_ID, roleProfile);
        assertEquals(updatedRole.getRoleName(), roleProfile.getRoleName());
    }

    @Test
    public void updateRoleWithWrongIdTest() {
        when(roleRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            roleService.updateRole(NOT_EXISTING_ID, roleProfile);
        }).isInstanceOf(NotExistException.class);
    }

    @Test
    public void getRoleByNameTest() {
        when(roleRepository.findByName(EXISTING_NAME)).thenReturn(Optional.of(role));

        Role actual = roleService.findByName(EXISTING_NAME);
        assertEquals(actual, role);
    }

    @Test
    public void getRoleByNotExistingNameTest() {
        when(roleRepository.findByName(NOT_EXISTING_NAME)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            roleService.findByName(NOT_EXISTING_NAME);
        }).isInstanceOf(NotExistException.class);
    }

    @Test
    public void addNewRoleTest() {
        Role newRole = Role.builder().id(NOT_EXISTING_ID).name(NEW_NAME).build();

        when(roleRepository.existsByName(NEW_NAME)).thenReturn(false);
        when(dtoConverter.convertToEntity(roleProfile, new Role())).thenReturn(newRole);
        when(roleRepository.save(any())).thenReturn(newRole);
        when(dtoConverter.convertToDto(newRole, RoleProfile.class))
                .thenReturn(new RoleProfile(NEW_NAME));

        RoleProfile actual = roleService.addNewRole(new RoleProfile(NEW_NAME));
        assertEquals(actual.getRoleName(), newRole.getName());
    }

    @Test
    public void addExistingRoleTest() {
        roleProfile.setRoleName(EXISTING_NAME);
        when(roleRepository.existsByName(EXISTING_NAME)).thenReturn(true);

        assertThatThrownBy(() -> {
            roleService.addNewRole(roleProfile);
        }).isInstanceOf(AlreadyExistException.class);
    }

    @Test
    public void deleteRoleByIdTest() {
        when(roleRepository.findById(CORRECT_ID)).thenReturn(Optional.of(role));
        when(archiveService.saveModel(role)).thenReturn(role);
        doNothing().when(roleRepository).deleteById(CORRECT_ID);
        doNothing().when(roleRepository).flush();
        when(dtoConverter.convertToDto(role, RoleResponse.class)).thenReturn(new RoleResponse(role.getId(), role.getName()));

        RoleResponse roleResponse = roleService.deleteRoleById(CORRECT_ID);
        assertEquals(roleResponse.getRoleName(), role.getName());
    }

    @Test
    public void deleteNotExistingRoleTest() {
        when(roleRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            roleService.deleteRoleById(NOT_EXISTING_ID);
        }).isInstanceOf(NotExistException.class);
    }
}
