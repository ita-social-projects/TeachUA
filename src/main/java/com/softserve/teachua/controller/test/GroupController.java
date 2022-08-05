package com.softserve.teachua.controller.test;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.test.answer.CreateGroup;
import com.softserve.teachua.dto.test.user.UserResponse;
import com.softserve.teachua.service.test.GroupService;
import com.softserve.teachua.service.test.GroupTestService;
import com.softserve.teachua.service.test.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * This controller is for managing the groups.
 * */

@RequiredArgsConstructor
@RestController
public class GroupController implements Api {
    private final GroupService groupService;
    private final GroupTestService groupsTestsService;
    private final SubscriptionService subscriptionService;

    /**
     * Use this endpoint to get all users by specific group.
     * The controller returns list of user DTOs {@code List<UserResponse>}.
     *
     * @param id - post group ud here.
     * @return new {@code List<UserResponse>}.
     */
    @GetMapping(value = "/groups/{id}/users", produces = APPLICATION_JSON_VALUE)
    public List<UserResponse> findUsersByGroup(@PathVariable Long id) {
        return subscriptionService.getUserResponseByGroupId(id);
    }

    /**
     * Use this endpoint to add a test to a specific group.
     *
     * @param groupId - post group id here.
     * @param testId  - post test id here.
     */
    @ResponseStatus(value = CREATED)
    @PostMapping(value = "/groups/{groupId}/tests/{testId}")
    public void addTestToGroup(@PathVariable Long groupId,
                               @PathVariable Long testId) {
        groupsTestsService.addTestToGroup(testId, groupId);
    }

    /**
     * Use this endpoint to create a group.
     * This controller returns group DTO {@code CreateGroup}.
     *
     * @param group - post parameters of group here.
     * @return new {@code CreateGroup} - shows the created group.
     */
    @ResponseStatus(value = CREATED)
    @PostMapping(value = "/groups",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public CreateGroup addGroup(@RequestBody CreateGroup group) {
        return groupService.save(group);
    }
}
