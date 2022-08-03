package com.softserve.teachua.service.test;

import com.softserve.teachua.dto.test.answer.CreateGroup;
import com.softserve.teachua.dto.test.group.ResponseGroup;
import com.softserve.teachua.dto.test.user.UserResponse;
import com.softserve.teachua.model.test.Group;

import java.util.List;

public interface GroupService {
    Group findByEnrollmentKey(String enrollmentKey);
    List<ResponseGroup> findResponseGroupsByTestId(Long id);
    List<Group> findAllByTestId(Long id);
    Group findById(Long groupId);
    CreateGroup save(CreateGroup group);
}
