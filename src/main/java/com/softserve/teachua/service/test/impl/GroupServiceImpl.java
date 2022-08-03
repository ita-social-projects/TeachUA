package com.softserve.teachua.service.test.impl;

import com.softserve.teachua.controller.test.GroupController;
import com.softserve.teachua.dto.test.answer.CreateGroup;
import com.softserve.teachua.dto.test.group.ResponseGroup;
import com.softserve.teachua.dto.test.user.UserResponse;
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
import java.util.Objects;

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
    public Group findById(Long groupId) {
        if (Objects.isNull(groupId))
            throw new IllegalArgumentException("Group id can't be null");

        return groupRepository.findById(groupId)
                .orElseThrow(() -> new NoSuchElementException(
                        String.format("There's no group with id '%d'", groupId)
                ));
    }

    @Override
    public List<Group> findAllByTestId(Long id) {
        if (Objects.isNull(id))
            throw new IllegalArgumentException("Test id can't be null");

        return groupRepository.findByTestId(id);
    }

    @Override
    public CreateGroup save(CreateGroup createGroup) {
        Group group = modelMapper.map(createGroup, Group.class);
        groupRepository.save(group);
        return createGroup;
    }

    @Override
    public Group findByEnrollmentKey(String enrollmentKey) {
        return groupRepository.findByEnrollmentKey(enrollmentKey)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Enrollment key '%s' is incorrect", enrollmentKey)
                ));
    }

    @Override
    public List<ResponseGroup> findResponseGroupsByTestId(Long id) {
        List<ResponseGroup> responseGroups = new ArrayList<>();
        List<Group> groups = findAllByTestId(id);

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
