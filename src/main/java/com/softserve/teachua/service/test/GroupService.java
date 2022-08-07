package com.softserve.teachua.service.test;

import com.softserve.teachua.dto.test.answer.CreateGroup;
import com.softserve.teachua.dto.test.group.ResponseGroup;
import com.softserve.teachua.model.test.Group;

import java.util.List;

/**
 * This interface contains all methods needed to manage groups.
 */
public interface GroupService {
    /**
     * This method returns a group entity found by its enrollment key.
     * @param enrollmentKey - put enrollment key.
     * @return new {@code Group}.
     */
    Group findByEnrollmentKey(String enrollmentKey);

    /**
     * This method returns a list of dto {@code List<ResponseGroup} found by test id.
     * @param id - put test id.
     * @return new {@code List<ResponseGroup>}.
     */
    List<ResponseGroup> findResponseGroupsByTestId(Long id);

    /**
     * This method returns a list of Group entities found by test id.
     * @param id - put test id.
     * @return new {@code List<Group>}.
     */
    List<Group> findAllByTestId(Long id);

    /**
     * This method returns a Group entity by id.
     * @param groupId - put group id.
     * @return new {@code Group}.
     */
    Group findById(Long groupId);

    /**
     * This method returns dto {@code CreateGroup} if group was successfully added.
     * @param group - put a group entity.
     * @return new {@code Group}.
     */
    CreateGroup save(CreateGroup group);
}
