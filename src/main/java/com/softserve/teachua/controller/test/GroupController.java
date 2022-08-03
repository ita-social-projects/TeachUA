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

@RequiredArgsConstructor
@RestController
public class GroupController implements Api {
    private final GroupService groupService;
    private final GroupTestService groupsTestsService;
    private final SubscriptionService subscriptionService;

    // TODO: implement the ability to get all users by their group
    @GetMapping("/groups/{id}/users")
    public List<UserResponse> findUsersByGroup(@PathVariable Long id) {
        return subscriptionService.getUserResponseByGroupId(id);
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
