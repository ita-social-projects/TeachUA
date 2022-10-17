package com.softserve.teachua.service;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.role.RoleProfile;
import com.softserve.teachua.dto.role.RoleResponse;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Archive;
import com.softserve.teachua.model.Role;
import com.softserve.teachua.model.archivable.ComplaintArch;
import com.softserve.teachua.model.archivable.RoleArch;
import com.softserve.teachua.repository.RoleRepository;
import com.softserve.teachua.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
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
    private Role newRole;
    private Role updRole;
    private Role correctRole;
    private RoleProfile newRoleProfile;
    private RoleResponse correctRoleResponse;
    private RoleArch roleArch;

    private final int CORRECT_ID = 1;
    private final int WRONG_ID = 50;
    private final int NEW_ID = 2;

    private final String CORRECT_NAME = RoleData.ADMIN.getDBRoleName();
    private final String WRONG_NAME = "NOT_EXIST";
    private final String NEW_NAME = "NEW_ROLE";

    @BeforeEach
    public void init() {
        newRole = Role.builder().id(NEW_ID).name(NEW_NAME).build();
        updRole = Role.builder().id(CORRECT_ID).name(NEW_NAME).build();
        correctRole = Role.builder().id(CORRECT_ID).name(CORRECT_NAME).build();
        newRoleProfile = RoleProfile.builder().roleName(NEW_NAME).build();
        correctRoleResponse = RoleResponse.builder().roleName(CORRECT_NAME).build();
        roleArch = RoleArch.builder().name(CORRECT_NAME).build();
    }

    @Test
    public void getListOfRolesShouldReturnCorrectRoleResponse() {
        when(roleRepository.findAll()).thenReturn(Arrays.asList(correctRole));
        when(dtoConverter.convertToDto(correctRole, RoleResponse.class)).thenReturn(correctRoleResponse);

        List<RoleResponse> actual = roleService.getListOfRoles();
        assertEquals(actual.isEmpty(), false);
        assertEquals(actual.get(0), correctRoleResponse);
    }

    @Test
    public void getRoleProfileByCorrectIdShouldReturnCorrectRoleResponse() {
        when(roleRepository.findById(CORRECT_ID)).thenReturn(Optional.of(correctRole));
        when(dtoConverter.convertToDto(correctRole, RoleResponse.class)).thenReturn(correctRoleResponse);

        RoleResponse actual = roleService.getRoleProfileById(CORRECT_ID);
        assertEquals(actual, correctRoleResponse);
    }

    @Test
    public void getRoleProfileByWrongIdShouldReturnCorrectRoleResponse() {
        when(roleRepository.findById(WRONG_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            roleService.getRoleProfileById(WRONG_ID);
        }).isInstanceOf(NotExistException.class);
    }

    @Test
    public void getRoleByCorrectNameShouldReturnCorrectRole() {
        when(roleRepository.findByName(CORRECT_NAME)).thenReturn(Optional.of(correctRole));

        Role actual = roleRepository.findByName(CORRECT_NAME).get();
        assertEquals(actual, correctRole);
    }

    @Test
    public void getRoleByCorrectIdShouldReturnCorrectRole() {
        when(roleRepository.findById(CORRECT_ID)).thenReturn(Optional.of(correctRole));
        Role actual = roleService.getRoleById(CORRECT_ID);
        assertEquals(actual, correctRole);
    }

    @Test
    public void getRoleByWrongIdShouldReturnCorrectRole() {
        when(roleRepository.findById(WRONG_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            roleService.getRoleById(WRONG_ID);
        }).isInstanceOf(NotExistException.class);
    }

    @Test
    public void updateRoleShouldReturnNewRoleProfile() {
        when(roleRepository.findById(CORRECT_ID)).thenReturn(Optional.of(correctRole));
        when(dtoConverter.convertToEntity(newRoleProfile, correctRole)).thenReturn(updRole);
        when(roleRepository.save(updRole)).thenReturn(updRole);
        when(dtoConverter.convertToDto(updRole, RoleProfile.class)).thenReturn(newRoleProfile);

        RoleProfile actual = roleService.updateRole(CORRECT_ID, newRoleProfile);
        assertEquals(actual, newRoleProfile);
    }

    @Test
    public void updateRoleWithWrongIdTest() {
        when(roleRepository.findById(WRONG_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            roleService.updateRole(WRONG_ID, newRoleProfile);
        }).isInstanceOf(NotExistException.class);
    }

    @Test
    public void getRoleByWrongNameShouldReturnCorrectRole() {
        when(roleRepository.findByName(WRONG_NAME)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            roleService.findByName(WRONG_NAME);
        }).isInstanceOf(NotExistException.class);
    }

    @Test
    public void getRoleByWrongNameShouldThrowNotExistException() {
        when(roleRepository.findByName(WRONG_NAME)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            roleService.findByName(WRONG_NAME);
        }).isInstanceOf(NotExistException.class);
    }

    @Test
    public void addNewRoleShouldReturnNewRoleProfile() {
        when(roleRepository.existsByName(NEW_NAME)).thenReturn(false);
        when(dtoConverter.convertToEntity(newRoleProfile, new Role())).thenReturn(newRole);
        when(roleRepository.save(any())).thenReturn(newRole);
        when(dtoConverter.convertToDto(newRole, RoleProfile.class)).thenReturn(newRoleProfile);

        RoleProfile actual = roleService.addNewRole(newRoleProfile);
        assertEquals(actual, newRoleProfile);
    }

    @Test
    public void addRoleShouldThrowAlreadyExistException() {
        newRoleProfile.setRoleName(CORRECT_NAME);
        when(roleRepository.existsByName(CORRECT_NAME)).thenReturn(true);

        assertThatThrownBy(() -> {
            roleService.addNewRole(newRoleProfile);
        }).isInstanceOf(AlreadyExistException.class);
    }

    @Test
    public void deleteRoleShouldReturnCorrectRoleResponse() {
        when(roleRepository.findById(CORRECT_ID)).thenReturn(Optional.of(correctRole));
        // when(archiveService.saveModel(correctRole)).thenReturn(correctRole);
        doNothing().when(roleRepository).deleteById(CORRECT_ID);
        doNothing().when(roleRepository).flush();
        when(dtoConverter.convertToDto(correctRole, RoleResponse.class)).thenReturn(correctRoleResponse);
        when(dtoConverter.convertToDto(correctRole, RoleArch.class)).thenReturn(roleArch);
        when(archiveService.saveModel(roleArch)).thenReturn(Archive.builder().build());
        RoleResponse actual = roleService.deleteRoleById(CORRECT_ID);
        assertEquals(actual, correctRoleResponse);
    }

    @Test
    public void deleteRoleShouldThrowNotExistException() {
        when(roleRepository.findById(WRONG_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            roleService.deleteRoleById(WRONG_ID);
        }).isInstanceOf(NotExistException.class);
    }
}
