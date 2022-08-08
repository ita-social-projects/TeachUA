package com.softserve.teachua.service.test.impl;

import com.softserve.teachua.controller.test.GroupController;
import com.softserve.teachua.dto.test.group.GroupProfile;
import com.softserve.teachua.dto.test.group.ResponseGroup;
import com.softserve.teachua.dto.test.group.UpdateGroup;
import com.softserve.teachua.model.test.Group;
import com.softserve.teachua.repository.test.GroupRepository;
import com.softserve.teachua.service.test.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static com.softserve.teachua.utils.NullValidator.checkNull;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    @Override
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
    public Group findById(Long groupId) {
        checkNull(groupId, "Group id");
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new NoSuchElementException(
                        String.format("There's no group with id '%d'", groupId)));
    }

    @Override
    public List<Group> findAllByTestId(Long testId) {
        checkNull(testId, "Test id");
        return groupRepository.findByTestId(testId);
    }

    @Override
    public UpdateGroup updateById(UpdateGroup updateGroup, Long groupId) {
        Group group = findById(groupId);
        group.setEndDate(updateGroup.getEndDate());
        group.setStartDate(updateGroup.getStartDate());
        group.setTitle(updateGroup.getTitle());
        return updateGroup;
    }

    @Override
    public GroupProfile save(GroupProfile groupProfile) {
        Group group = modelMapper.map(groupProfile, Group.class);
        groupRepository.save(group);
        return groupProfile;
    }

    @Override
    public Group findByEnrollmentKey(String enrollmentKey) {
        checkNull(enrollmentKey, "Enrollment key");
        return groupRepository.findByEnrollmentKey(enrollmentKey)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Enrollment key '%s' is incorrect", enrollmentKey)));
    }

    @Override
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
}
