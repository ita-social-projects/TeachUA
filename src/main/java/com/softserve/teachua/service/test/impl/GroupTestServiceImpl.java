package com.softserve.teachua.service.test.impl;

import com.softserve.teachua.model.test.GroupTest;
import com.softserve.teachua.repository.test.GroupTestRepository;
import com.softserve.teachua.service.test.GroupService;
import com.softserve.teachua.service.test.GroupTestService;
import com.softserve.teachua.service.test.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class GroupTestServiceImpl implements GroupTestService {
    private final GroupTestRepository groupTestRepository;
    private final GroupService groupService;
    private final TestService testService;

    @Override
    public void addTestToGroup(Long testId, Long groupId) {
        if (Objects.isNull(testId) || Objects.isNull(groupId))
            throw new IllegalArgumentException("Test id or Group id can't be null");

        GroupTest groupTest = new GroupTest();
        groupTest.setGroup(groupService.findById(groupId));
        groupTest.setTest(testService.findById(testId));
        groupTestRepository.save(groupTest);
    }
}
