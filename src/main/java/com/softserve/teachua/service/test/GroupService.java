package com.softserve.teachua.service.test;

import com.softserve.teachua.dto.test.group.GroupProfile;
import com.softserve.teachua.dto.test.group.ResponseGroup;
import com.softserve.teachua.dto.test.group.UpdateGroup;
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
     * This method returns a list of groups.
     * @return new {@code List<Group>}
     */
    List<Group> findAll();

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
     * This method returns dto {@code GroupProfile} if group was successfully added.
     * @param group - put a group entity.
     * @return new {@code GroupProfile}.
     */
    GroupProfile save(GroupProfile group);

    /**
     * This method returns dto {@code UpdateGroup} if group was successfully updated.
     * @param group   - put a group entity.
     * @param groupId - put group.
     * @return new {@code UpdateGroup}.
     */
    UpdateGroup updateById(UpdateGroup group, Long groupId);

    /**
     * This method returns a list of group DTOs.
     * @return new {@code List<GroupProfile>}.
     */
    List<GroupProfile> findAllGroupProfiles();
}
