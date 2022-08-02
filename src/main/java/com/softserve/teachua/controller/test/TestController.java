package com.softserve.teachua.controller.test;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.test.result.CreateResult;
import com.softserve.teachua.dto.test.result.SuccessCreatedResult;
import com.softserve.teachua.dto.test.test.*;
import com.softserve.teachua.service.UserService;
import com.softserve.teachua.service.test.ResultService;
import com.softserve.teachua.service.test.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/tests")
@RestController
public class TestController implements Api {
    private final TestService testService;
    private final UserService userService;
    private final ResultService resultService;

    @PostMapping("/{id}")
    public void archiveTest(@PathVariable Long id) {
        testService.archiveTestById(id);
    }

    @GetMapping("/{testId}/results/{resultId}")
    public ResultTest getTestResult(@PathVariable Long testId, @PathVariable Long resultId) {
        return testService.getResultTest(testId, resultId);
    }

    @PostMapping
    public SuccessCreatedTest addTest(@RequestBody CreateTest test) {
        return testService.addTest(test);
    }

    @PostMapping("/test/{id}")
    public SuccessCreatedResult createResult(@PathVariable Long id,
                                             @RequestBody CreateResult result){
        result.setUserId(userService.getCurrentUser().getId());
        result.setTestId(id);
        return testService.saveResult(result);
    }

    @GetMapping("/test/{id}")
    public PassTest passTest(@PathVariable Long id){
        return testService.findPassTestById(id);
    }

    @GetMapping
    public List<TestProfile> getUnarchivedTests(){
        return testService.findUnarchivedTestProfiles();
    }
}
