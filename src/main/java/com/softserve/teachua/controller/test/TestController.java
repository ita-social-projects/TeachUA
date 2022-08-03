package com.softserve.teachua.controller.test;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.test.group.ResponseGroup;
import com.softserve.teachua.dto.test.result.CreateResult;
import com.softserve.teachua.dto.test.result.SuccessCreatedResult;
import com.softserve.teachua.dto.test.test.*;
import com.softserve.teachua.service.test.GroupService;
import com.softserve.teachua.service.test.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.*;
import static org.springframework.http.HttpStatus.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class TestController implements Api {
    private final TestService testService;
    private final GroupService groupService;

    @GetMapping(value = "/tests", produces = APPLICATION_JSON_VALUE)
    public List<TestProfile> getUnarchivedTests(){
        return testService.findUnarchivedTestProfiles();
    }

    @GetMapping(value = "/tests/{id}", produces = APPLICATION_JSON_VALUE)
    public ViewTest viewTest(@PathVariable Long id){
        return testService.findViewTestById(id);
    }

    @GetMapping(value = "/tests/{id}/passing", produces = APPLICATION_JSON_VALUE)
    public PassTest passTest(@PathVariable Long id){
        return testService.findPassTestById(id);
    }

    @GetMapping(value = "/tests/{testId}/results/{resultId}", produces = APPLICATION_JSON_VALUE)
    public ResultTest getTestResult(@PathVariable Long testId, @PathVariable Long resultId) {
        return testService.getResultTest(testId, resultId);
    }

    @GetMapping(value = "/tests/{id}/groups", produces = APPLICATION_JSON_VALUE)
    public List<ResponseGroup> getGroups(@PathVariable Long id) {
        return groupService.findResponseGroupsByTestId(id);
    }

    @ResponseStatus(value = CREATED)
    @PostMapping(value = "/tests",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public SuccessCreatedTest addTest(@RequestBody CreateTest test) {
        return testService.addTest(test);
    }

    @ResponseStatus(value = CREATED)
    @PostMapping(value = "/tests/result",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public SuccessCreatedResult createResult(@RequestBody CreateResult result){
        return testService.saveResult(result);
    }

    @PostMapping(value = "/tests/{id}/archiving")
    public void archiveTest(@PathVariable Long id) {
        testService.archiveTestById(id);
    }
}
