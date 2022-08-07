package com.softserve.teachua.service.test.impl;

import com.softserve.teachua.controller.test.GroupController;
import com.softserve.teachua.dto.test.answer.CreateGroup;
import com.softserve.teachua.dto.test.group.ResponseGroup;
import com.softserve.teachua.model.User;
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
    public CreateGroup save(CreateGroup createGroup) {
        Group group = modelMapper.map(createGroup, Group.class);
        groupRepository.save(group);
        return createGroup;
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
