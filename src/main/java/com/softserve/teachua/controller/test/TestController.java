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

@RequiredArgsConstructor
@RestController
public class TestController implements Api {
    private final TestService testService;
    private final UserService userService;
    private final ResultService resultService;

    @GetMapping("/tests")
    public TestsContainer getUnarchivedTests(){
        return testService.findUnarchivedTestProfiles();
    }

    @GetMapping("/tests/{id}")
    public PassTest passTest(@PathVariable Long id){
        return testService.findPassTestById(id);
    }

    @GetMapping("/tests/{testId}/results/{resultId}")
    public ResultTest getTestResult(@PathVariable Long testId, @PathVariable Long resultId) {
        return testService.getResultTest(testId, resultId);
    }

    @PostMapping("/tests")
    public SuccessCreatedTest addTest(@RequestBody CreateTest test) {
        return testService.addTest(test);
    }

    @PostMapping("/tests/result")
    public SuccessCreatedResult createResult(@RequestBody CreateResult result){
        return testService.saveResult(result);
    }

    @PostMapping("/tests/{id}/archiving")
    public void archiveTest(@PathVariable Long id) {
        testService.archiveTestById(id);
    }
}
