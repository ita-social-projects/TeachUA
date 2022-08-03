package com.softserve.teachua.controller.test;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.test.answer.CreateGroup;
import com.softserve.teachua.service.test.GroupService;
import com.softserve.teachua.service.test.GroupTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class GroupController implements Api {
    private final GroupService groupService;
    private final GroupTestService groupsTestsService;

    // TODO: implement the ability to get all users by their group
    @GetMapping("/groups/{id}/users")
    public Object findUsersByGroup(@PathVariable Long id) {
        return null;
    }

    @PatchMapping("/groups/{groupId}/tests/{testId}")
    public void addTestToGroup(@PathVariable Long groupId,
                               @PathVariable Long testId) {
        groupsTestsService.addTestToGroup(testId, groupId);
    }

    @PostMapping("/groups")
    public CreateGroup addGroup(@RequestBody CreateGroup group) {
        return groupService.save(group);
    }
}
