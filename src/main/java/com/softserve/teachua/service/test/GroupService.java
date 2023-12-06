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
     * Find a group by its ID.
     * @param id a group ID
     * @return a group dto found by its ID
     */
    GroupProfile findGroupProfileById(Long id);

    /**
     * Find a group entity by its enrollment key.
     * @param enrollmentKey enrollment key
     * @return a group entity found by its enrollment key
     */
    Group findByEnrollmentKey(String enrollmentKey);

    /**
     * Find a list of dto {@code List<ResponseGroup} by test id.
     * @param id test ID
     * @return a list of dto {@code List<ResponseGroup}
     */
    List<ResponseGroup> findResponseGroupsByTestId(Long id);

    /**
     * Find all Group entities.
     * @return a list of Group entities
     */
    List<Group> findAll();

    /**
     *  Find all Group entities by test ID.
     * @param id test ID
     * @return a list of Group entities found by test ID
     */
    List<Group> findAllByTestId(Long id);

    /**
     * Find a Group entity by id.
     * @param groupId group ID
     * @return a Group entity by ID
     */
    Group findGroupById(Long groupId);

    /**
     * Add a group.
     * @param group a group entity
     * @return dto {@code GroupProfile} if a group was successfully added
     */
    GroupProfile save(GroupProfile group);

    /**
     * Update a group.
     * @param group a group entity
     * @param groupId a group ID
     * @return dto {@code UpdateGroup} if a group was successfully updated
     */
    UpdateGroup updateById(UpdateGroup group, Long groupId);

    /**
     * Find all group profiles.
     * @return a list of group DTOs
     */
    List<GroupProfile> findAllGroupProfiles();
}
