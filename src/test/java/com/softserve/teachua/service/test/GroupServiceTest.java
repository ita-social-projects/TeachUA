package com.softserve.teachua.service.test;

import com.softserve.teachua.dto.test.group.GroupProfile;
import com.softserve.teachua.dto.test.group.ResponseGroup;
import com.softserve.teachua.dto.test.group.UpdateGroup;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.test.Group;
import com.softserve.teachua.repository.test.GroupRepository;
import com.softserve.teachua.service.test.impl.GroupServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private GroupServiceImpl groupService;

    private Group group;
    private GroupProfile groupProfile;
    private ResponseGroup responseGroup;
    private UpdateGroup updateGroup;

    private final Long EXISTING_GROUP_ID = 1L;
    private final Long NOT_EXISTING_GROUP_ID = 100L;

    private final String EXISTING_GROUP_TITLE = "Existing Group Title";
    private final String NEW_GROUP_TITLE = "New Group Title";

    private final String EXISTING_ENROLLMENT_KEY = "Existing Enrollment Key";
    private final String NOT_EXISTING_ENROLLMENT_KEY = "Not Existing Enrollment Key";

    private final LocalDate START_DATE = LocalDate.of(2022, 1, 1);
    private final LocalDate END_DATE = LocalDate.of(2022, 1, 31);

    private final Long EXISTING_TEST_ID = 1L;
    private final Long NOT_EXISTING_TEST_ID = 100L;

    @BeforeEach
    public void setUp() {
        group = Group.builder()
                .id(EXISTING_GROUP_ID)
                .title(EXISTING_GROUP_TITLE)
                .enrollmentKey(EXISTING_ENROLLMENT_KEY)
                .startDate(START_DATE)
                .endDate(END_DATE)
                .build();
        groupProfile = GroupProfile.builder()
                .title(EXISTING_GROUP_TITLE)
                .enrollmentKey(EXISTING_ENROLLMENT_KEY)
                .startDate(START_DATE)
                .endDate(END_DATE)
                .build();
        responseGroup = ResponseGroup.builder()
                .title(EXISTING_GROUP_TITLE)
                .build();
        updateGroup = UpdateGroup.builder()
                .title(NEW_GROUP_TITLE)
                .startDate(START_DATE)
                .endDate(END_DATE)
                .build();
    }

    @Test
    void findGroupByExistingIdShouldReturnGroup() {
        when(groupRepository.findById(EXISTING_GROUP_ID)).thenReturn(Optional.of(group));

        Group actual = groupService.findById(EXISTING_GROUP_ID);
        assertEquals(group, actual);
    }

    @Test
    void findGroupByNotExistingIdShouldThrowNotExistException() {
        when(groupRepository.findById(NOT_EXISTING_GROUP_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> groupService.findById(NOT_EXISTING_GROUP_ID))
                .isInstanceOf(NotExistException.class);
    }

    @Test
    void findGroupByNullIdShouldThrowIllegalArgException() {
        assertThatThrownBy(() -> groupService.findById(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findAllGroupsShouldReturnListOfGroups() {
        when(groupRepository.findAll()).thenReturn(Collections.singletonList(group));
        assertEquals(group, groupService.findAll().get(0));
    }

    @Test
    void findAllGroupProfilesShouldReturnListOfGroupProfiles() {
        when(groupRepository.findAll()).thenReturn(Collections.singletonList(group));
        when(modelMapper.map(group, GroupProfile.class)).thenReturn(groupProfile);

        List<GroupProfile> actual = groupService.findAllGroupProfiles();
        assertEquals(groupProfile, actual.get(0));
    }

    @Test
    void findAllGroupsByExistingTestIdShouldReturnListOfGroups() {
        when(groupRepository.findByTestId(EXISTING_TEST_ID)).thenReturn(Collections.singletonList(group));
        List<Group> actual = groupService.findAllByTestId(EXISTING_TEST_ID);
        assertEquals(group, actual.get(0));
    }

    @Test
    void findAllGroupsByNotExistingTestIdShouldReturnListOfGroups() {
        when(groupRepository.findByTestId(NOT_EXISTING_TEST_ID)).thenReturn(Collections.emptyList());
        List<Group> actual = groupService.findAllByTestId(NOT_EXISTING_TEST_ID);
        assertEquals(0, actual.size());
    }

    @Test
    void findAllGroupsByNullIdShouldThrowIllegalArgException() {
        assertThatThrownBy(() -> groupService.findAllByTestId(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findGroupByExistingEnrollmentKeyShouldReturnGroup() {
        when(groupRepository.findByEnrollmentKey(EXISTING_ENROLLMENT_KEY)).thenReturn(Optional.of(group));

        Group actual = groupService.findByEnrollmentKey(EXISTING_ENROLLMENT_KEY);
        assertEquals(group, actual);
    }

    @Test
    void findGroupByNotExistingEnrollmentKeyShouldThrowIllegalArgException() {
        when(groupRepository.findByEnrollmentKey(NOT_EXISTING_ENROLLMENT_KEY)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> groupService.findByEnrollmentKey(NOT_EXISTING_ENROLLMENT_KEY))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findGroupByNullEnrollmentKeyShouldThrowIllegalArgException() {
        assertThatThrownBy(() -> groupService.findByEnrollmentKey(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findResponseGroupsByExistingTestIdShouldReturnListOfGroupResponse() {
        when(groupService.findAllByTestId(EXISTING_TEST_ID)).thenReturn(Collections.singletonList(group));
        when(modelMapper.map(group, ResponseGroup.class)).thenReturn(responseGroup);

        List<ResponseGroup> actual = groupService.findResponseGroupsByTestId(EXISTING_TEST_ID);
        assertEquals(responseGroup, actual.get(0));
    }

    @Test
    void findResponseGroupsByNotExistingTestIdShouldReturnEmptyList() {
        when(groupService.findAllByTestId(NOT_EXISTING_TEST_ID)).thenReturn(Collections.emptyList());

        List<ResponseGroup> actual = groupService.findResponseGroupsByTestId(NOT_EXISTING_TEST_ID);
        assertEquals(0, actual.size());
    }

    @Test
    void findResponseGroupsByNullTestIdShouldReturnListOfGroupResponse() {
        assertThatThrownBy(() -> groupService.findResponseGroupsByTestId(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void saveGroupShouldReturnSavedGroupProfile() {
        Group newGroup = Group.builder()
                .title(NEW_GROUP_TITLE)
                .enrollmentKey(NOT_EXISTING_ENROLLMENT_KEY)
                .startDate(START_DATE)
                .endDate(END_DATE)
                .build();
        GroupProfile newGroupProfile = GroupProfile.builder()
                .title(NEW_GROUP_TITLE)
                .enrollmentKey(NOT_EXISTING_ENROLLMENT_KEY)
                .startDate(START_DATE)
                .endDate(END_DATE)
                .build();
        when(modelMapper.map(newGroupProfile, Group.class)).thenReturn(newGroup);
        when(groupRepository.save(any())).thenReturn(newGroup);

        GroupProfile actual = groupService.save(newGroupProfile);
        assertEquals(groupProfile, actual);
    }

    @Test
    void saveNullShouldThrowIllegalArgException() {
        assertThatThrownBy(() -> groupService.save(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void updateGroupWithNotExistingIdShouldThrowNotExistException() {
        when(groupRepository.findById(NOT_EXISTING_GROUP_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> groupService.updateById(updateGroup, NOT_EXISTING_GROUP_ID))
                .isInstanceOf(NotExistException.class);
    }

    @Test
    void updateGroupWithExistingIdShouldReturnUpdateGroup() {
        when(groupRepository.findById(EXISTING_GROUP_ID)).thenReturn(Optional.of(group));

        UpdateGroup actual = groupService.updateById(updateGroup, EXISTING_GROUP_ID);
        assertEquals(updateGroup, actual);
    }

    @Test
    void updateNullShouldThrowIllegalArgException() {
        assertThatThrownBy(() -> groupService.updateById(null, EXISTING_GROUP_ID))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void updateGroupWithNullIdShouldThrowIllegalArgException(){
        assertThatThrownBy(() -> groupService.updateById(updateGroup, null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
