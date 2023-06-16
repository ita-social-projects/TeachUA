package com.softserve.question.controller;

import com.softserve.question.controller.marker.Api;
import com.softserve.question.dto.group.GroupProfile;
import com.softserve.question.dto.group.UpdateGroup;
import com.softserve.question.service.GroupService;
import com.softserve.question.service.GroupTestService;
import jakarta.validation.Valid;
import java.util.List;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is for managing groups.
 */
@RestController
public class GroupController implements Api {
    private final GroupService groupService;
    private final GroupTestService groupsTestsService;

    public GroupController(GroupService groupService, GroupTestService groupsTestsService) {
        this.groupService = groupService;
        this.groupsTestsService = groupsTestsService;
    }

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
