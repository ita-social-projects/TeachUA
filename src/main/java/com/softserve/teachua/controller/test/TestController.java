package com.softserve.teachua.controller.test;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.test.question.QuestionResponse;
import com.softserve.teachua.dto.test.result.CreateResult;
import com.softserve.teachua.dto.test.result.SuccessCreatedResult;
import com.softserve.teachua.dto.test.test.CreateTest;
import com.softserve.teachua.dto.test.test.SuccessCreatedTest;
import com.softserve.teachua.model.User;
import com.softserve.teachua.service.UserService;
import com.softserve.teachua.service.test.QuestionService;
import com.softserve.teachua.service.test.ResultService;
import com.softserve.teachua.service.test.TestService;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class TestController implements Api {
    private final TestService testService;
    private final UserService userService;
    private final QuestionService questionService;
    private final ResultService resultService;

    @AllowedRoles(value = {RoleData.USER, RoleData.MANAGER, RoleData.ADMIN})
    @GetMapping("/tests/user")
    public String getCurrentUser() {
        User user = userService.getCurrentUser();
        return user.getEmail();
    }

    @PostMapping("/tests")
    public SuccessCreatedTest addTest(@RequestBody CreateTest test) {
        return testService.addTest(test);
    }

    @GetMapping("/tests/{id}")
    public void archiveTest(@PathVariable Long id) {
        testService.archiveTestById(id);
    }

    @GetMapping("/test/{id}")
    public List<QuestionResponse> findQuestionsByTestId(@PathVariable Long id) {
        return questionService.findQuestionResponsesByTestId(id);
    }

    @PostMapping("/test/{id}")
    public SuccessCreatedResult createResult(@PathVariable Long id,
                                             @RequestBody CreateResult result){
        result.setUserId(userService.getCurrentUser().getId());
        result.setTestId(id);
        return resultService.addResult(result);
    }
}
