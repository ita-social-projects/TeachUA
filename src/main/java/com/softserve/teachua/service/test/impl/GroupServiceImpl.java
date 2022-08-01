package com.softserve.teachua.service.test.impl;

import com.softserve.teachua.model.test.Group;
import com.softserve.teachua.repository.test.GroupRepository;
import com.softserve.teachua.service.test.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;

    public Group findByEnrollmentKey(String enrollmentKey) {
        return groupRepository.findByEnrollmentKey(enrollmentKey)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Enrollment key '%s' is incorrect", enrollmentKey)
                ));
    }
}
