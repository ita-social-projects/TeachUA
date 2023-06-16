package com.softserve.question.service.impl;

import com.softserve.question.model.GroupTest;
import com.softserve.question.repository.GroupTestRepository;
import com.softserve.question.service.GroupService;
import com.softserve.question.service.GroupTestService;
import com.softserve.question.service.TestService;
import static com.softserve.question.util.validation.NullValidator.checkNullIds;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
public class GroupTestServiceImpl implements GroupTestService {
    private final GroupTestRepository groupTestRepository;
    private final GroupService groupService;
    private final TestService testService;

    public GroupTestServiceImpl(GroupTestRepository groupTestRepository, GroupService groupService,
                                TestService testService) {
        this.groupTestRepository = groupTestRepository;
        this.groupService = groupService;
        this.testService = testService;
    }

    @Override
    public GroupTest addTestToGroup(Long testId, Long groupId) {
        checkNullIds(testId, groupId);
        GroupTest groupTest = new GroupTest();
        groupTest.setGroup(groupService.findGroupById(groupId));
        groupTest.setTest(testService.findById(testId));
        groupTestRepository.save(groupTest);
        log.info("**/Test with id '{}' has been added to group with id '{}'.", testId, groupId);
        return groupTest;
    }
}
