package com.softserve.teachua.controller.test;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.test.group.GroupProfile;
import com.softserve.teachua.dto.test.group.UpdateGroup;
import com.softserve.teachua.dto.test.user.UserResponse;
import com.softserve.teachua.service.test.GroupService;
import com.softserve.teachua.service.test.GroupTestService;
import com.softserve.teachua.service.test.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * This controller is for managing groups.
 * */

@RequiredArgsConstructor
@RestController
public class GroupController implements Api {
    private final GroupService groupService;
    private final GroupTestService groupsTestsService;
    private final SubscriptionService subscriptionService;

    /**
     * Use this endpoint to get a particular group by id.
     *
     * @return new {@code GroupProfile}.
     */
    @GetMapping(value = "/groups/{id}", produces = APPLICATION_JSON_VALUE)
    public GroupProfile getGroup(@PathVariable Long id) {
        return groupService.findGroupProfileById(id);
    }

    /**
     * Use this endpoint to get all groups.
     *
     * @return new {@code List<GroupProfile>}.
     */
    @GetMapping(value = "/groups", produces = APPLICATION_JSON_VALUE)
    public List<GroupProfile> getGroups() {
        return groupService.findAllGroupProfiles();
    }

    /**
     * Use this endpoint to get all users by specific group.
     * The controller returns list of user DTOs {@code List<UserResponse>}.
     *
     * @param id - put group ud here.
     * @return new {@code List<UserResponse>}.
     */
    @GetMapping(value = "/groups/{id}/users", produces = APPLICATION_JSON_VALUE)
    public List<UserResponse> findUsersByGroup(@PathVariable Long id) {
        return subscriptionService.getUserResponsesByGroupId(id);
    }

    /**
     * Use this endpoint to add a test to a specific group.
     *
     * @param groupId - put group id here.
     * @param testId  - put test id here.
     */
    @PostMapping(value = "/groups/{groupId}/tests/{testId}")
    public void addTestToGroup(@PathVariable Long groupId,
                               @PathVariable Long testId) {
        groupsTestsService.addTestToGroup(testId, groupId);
    }

    /**
     * Use this endpoint to create a group.
     * This controller returns group DTO {@code CreateGroup}.
     *
     * @param group - put of group parameters here.
     * @return new {@code CreateGroup} - shows the created group.
     */
    @ResponseStatus(value = CREATED)
    @PostMapping(value = "/groups",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public GroupProfile addGroup(@Valid @RequestBody GroupProfile group) {
        return groupService.save(group);
    }

    /**
     * Use this endpoint to update the group.
     * This controller returns group DTO {@code UpdateGroup}.
     *
     * @param groupId - put group id here.
     * @param group   - put the updated group parameters here.
     * @return new {@code UpdateGroup} - shows the updated group.
     */

    @PatchMapping(value = "/groups/{groupId}",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public UpdateGroup updateGroup(@PathVariable Long groupId,
                                   @Valid @RequestBody UpdateGroup group) {
        return groupService.updateById(group, groupId);
    }
}
