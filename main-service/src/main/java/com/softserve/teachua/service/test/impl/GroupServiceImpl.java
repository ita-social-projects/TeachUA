package com.softserve.teachua.service.test.impl;

import com.softserve.teachua.controller.test.GroupController;
import com.softserve.teachua.dto.test.group.GroupProfile;
import com.softserve.teachua.dto.test.group.ResponseGroup;
import com.softserve.teachua.dto.test.group.UpdateGroup;
import com.softserve.clients.exception.NotExistException;
import com.softserve.teachua.model.test.Group;
import com.softserve.teachua.repository.test.GroupRepository;
import com.softserve.teachua.service.test.GroupService;
import static com.softserve.teachua.utils.test.Messages.INCORRECT_ENROLLMENT_KEY_MESSAGE;
import static com.softserve.teachua.utils.test.Messages.NO_ID_MESSAGE;
import static com.softserve.teachua.utils.test.validation.NullValidator.checkNull;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Link;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class GroupServiceImpl implements GroupService {
    private static final String NOT_EXIST_GROUP_EXCEPTION = "Group with id %d not exists";

    private final GroupRepository groupRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public GroupProfile findGroupProfileById(Long id) {
        return modelMapper.map(findGroupById(id), GroupProfile.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Group findGroupById(Long groupId) {
        checkNull(groupId, "Group id");
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new NotExistException(
                        String.format(NO_ID_MESSAGE, "group", groupId)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupProfile> findAllGroupProfiles() {
        List<GroupProfile> groupProfiles = new ArrayList<>();
        for (Group group : findAll()) {
            GroupProfile groupProfile = modelMapper.map(group, GroupProfile.class);
            UpdateGroup updGroup = modelMapper.map(groupProfile, UpdateGroup.class);
            Link updateGroup = linkTo(methodOn(GroupController.class)
                    .updateGroup(group.getId(), updGroup))
                    .withRel("update");
            groupProfile.add(updateGroup);
            groupProfiles.add(groupProfile);
        }
        return groupProfiles;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Group> findAllByTestId(Long testId) {
        checkNull(testId, "Test id");
        return groupRepository.findByTestId(testId);
    }

    @Override
    @Transactional(readOnly = true)
    public Group findByEnrollmentKey(String enrollmentKey) {
        checkNull(enrollmentKey, "Enrollment key");
        return groupRepository.findByEnrollmentKey(enrollmentKey)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format(INCORRECT_ENROLLMENT_KEY_MESSAGE, enrollmentKey)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResponseGroup> findResponseGroupsByTestId(Long testId) {
        checkNull(testId, "Test id");
        List<ResponseGroup> responseGroups = new ArrayList<>();
        List<Group> groups = findAllByTestId(testId);

        for (Group group : groups) {
            ResponseGroup responseGroup = modelMapper.map(group, ResponseGroup.class);
            Link link = linkTo(methodOn(GroupController.class).findUsersByGroup(group.getId()))
                    .withSelfRel();
            responseGroup.add(link);
            responseGroups.add(responseGroup);
        }
        return responseGroups;
    }

    @Override
    public GroupProfile save(GroupProfile groupProfile) {
        checkNull(groupProfile, "Group");
        Group group = modelMapper.map(groupProfile, Group.class);
        groupRepository.save(group);
        log.info("**/Group has been created. {}.", group);
        return groupProfile;
    }

    @Override
    public UpdateGroup updateById(UpdateGroup updateGroup, Long groupId) {
        checkNull(updateGroup, "Group");
        checkNull(groupId, "Group ID");
        if (!groupRepository.existsById(groupId)) {
            throw new NotExistException(String.format(NOT_EXIST_GROUP_EXCEPTION, groupId));
        }

        Group group = modelMapper.map(updateGroup, Group.class);
        group.setId(groupId);
        groupRepository.save(group);
        log.info("**/Group with id '{}' has been updated", groupId);
        return updateGroup;
    }
}
