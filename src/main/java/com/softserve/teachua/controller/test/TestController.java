package com.softserve.teachua.controller.test;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.test.group.ResponseGroup;
import com.softserve.teachua.dto.test.test.*;
import com.softserve.teachua.service.test.GroupService;
import com.softserve.teachua.service.test.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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
     * Use this endpoint to get all archived tests.
     * The controller returns a list of test DTOs {@code List<TestProfile>}.
     *
     * @return new {@code List<TestProfile>}.
     */
    @GetMapping(value = "/tests/archived", produces = APPLICATION_JSON_VALUE)
    public List<TestProfile> getArchivedTests(){
        return testService.findArchivedTestProfiles();
    }

    /**
     * Use this endpoint to get general information about a specific test.
     * The controller returns a test DTO {@code ViewTest}.
     *
     * @param id - put test id here.
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
     * @param id - put test id here.
     * @return new {@code PassTest}.
     */
    @GetMapping(value = "/tests/{id}/passing", produces = APPLICATION_JSON_VALUE)
    public PassTest passTest(@PathVariable Long id){
        return testService.findPassTestById(id);
    }

    /**
     * Use this endpoint to get a list of groups that contain a specific test.
     * This controller return a list of group DTOs {@code List<ResponseGroup>}.
     *
     * @param id - put test id here.
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
     * @param test - put information about the test and questions that relate to it here.
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
     * Use this endpoint to archive the test.
     *
     * @param id - post test id here.
     */
    @PostMapping(value = "/tests/{id}/archive")
    public TestProfile archiveTest(@PathVariable Long id) {
        return testService.archiveTestById(id);
    }

    /**
     * Use this endpoint to restore the test.
     *
     * @param id - post test id here.
     */
    @PostMapping(value = "/tests/{id}/restore")
    public TestProfile restoreTest(@PathVariable Long id) {
        return testService.restoreTestById(id);
    }
}
