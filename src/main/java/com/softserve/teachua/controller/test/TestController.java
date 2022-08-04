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

/**
 * This controller is for managing tests.
 * */

@RequiredArgsConstructor
@RestController
public class TestController implements Api {
    private final TestService testService;
    private final GroupService groupService;

    /**
     * Use this endpoint to get all unarchived tests.
     * The controller returns a list of test DTOs {@code List<TestProfile>}.
     *
     * @return new {@code List<TestProfile>}.
     */
    @GetMapping(value = "/tests", produces = APPLICATION_JSON_VALUE)
    public List<TestProfile> getUnarchivedTests(){
        return testService.findUnarchivedTestProfiles();
    }

    /**
     * Use this endpoint to get general information about a specific test.
     * The controller returns a test DTO {@code ViewTest}.
     *
     * @param id - post test id here.
     * @return new {@code ViewTest}.
     */
    @GetMapping(value = "/tests/{id}", produces = APPLICATION_JSON_VALUE)
    public ViewTest viewTest(@PathVariable Long id){
        return testService.findViewTestById(id);
    }

    /**
     * Use this endpoint to start taking the test.
     * This controller returns a test DTO {@code PassTest}
     * which contains information about the test and questions.
     *
     * @param id - post test id here.
     * @return new {@code PassTest}.
     */
    @GetMapping(value = "/tests/{id}/passing", produces = APPLICATION_JSON_VALUE)
    public PassTest passTest(@PathVariable Long id){
        return testService.findPassTestById(id);
    }

    /**
     * Use this endpoint to get information about the result of passing a test.
     * This controller returns a test DTO {@code ResultTest}.
     *
     * @param testId   - post test id here.
     * @param resultId - post result id here.
     * @return new {@code ResultTest}.
     */
    @GetMapping(value = "/tests/{testId}/results/{resultId}", produces = APPLICATION_JSON_VALUE)
    public ResultTest getTestResult(@PathVariable Long testId, @PathVariable Long resultId) {
        return testService.getResultTest(testId, resultId);
    }

    /**
     * Use this endpoint to get a list of groups that contain a specific test.
     * This controller return a list of group DTOs {@code List<ResponseGroup>}.
     *
     * @param id - post test id here.
     * @return new {@code List<ResponseGroup>}.
     */
    @GetMapping(value = "/tests/{id}/groups", produces = APPLICATION_JSON_VALUE)
    public List<ResponseGroup> getGroups(@PathVariable Long id) {
        return groupService.findResponseGroupsByTestId(id);
    }

    /**
     * Use this endpoint to create a test.
     * This controller returns a test DTO {@code SuccessCreatedTest}
     * which contains general information about the test.
     *
     * @param test - post information about the test and questions that relate to it.
     * @return new {@code SuccessCreatedTest}.
     */
    @ResponseStatus(value = CREATED)
    @PostMapping(value = "/tests",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public SuccessCreatedTest addTest(@RequestBody CreateTest test) {
        return testService.addTest(test);
    }

    /**
     * Use this endpoint to store the test result.
     * This controller returns a result DTO {@code SuccessCreatedResult}
     * which contains general information about the test result.
     *
     * @param result - post
     * @return new {@code SuccessCreatedResult}.
     */
    @ResponseStatus(value = CREATED)
    @PostMapping(value = "/tests/result",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public SuccessCreatedResult saveResult(@RequestBody CreateResult result){
        return testService.saveResult(result);
    }

    /**
     * Use this endpoint to archive the test.
     *
     * @param id - post test id here.
     */
    @PostMapping(value = "/tests/{id}/archiving")
    public void archiveTest(@PathVariable Long id) {
        testService.archiveTestById(id);
    }
}
