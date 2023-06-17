package com.softserve.question.controller;

import com.softserve.question.controller.marker.Api;
import com.softserve.question.dto.group.ResponseGroup;
import com.softserve.question.dto.test.CreateTest;
import com.softserve.question.dto.test.PassTest;
import com.softserve.question.dto.test.SuccessCreatedTest;
import com.softserve.question.dto.test.TestProfile;
import com.softserve.question.dto.test.ViewTest;
import com.softserve.question.service.GroupService;
import com.softserve.question.service.TestService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is for managing tests.
 */
@RestController
@RequestMapping("/api/v1/test")
public class TestController implements Api {
    private final TestService testService;
    private final GroupService groupService;

    public TestController(TestService testService, GroupService groupService) {
        this.testService = testService;
        this.groupService = groupService;
    }

    /**
     * Use this endpoint to get all unarchived tests.
     * The controller returns a list of test DTOs {@code List<TestProfile>}.
     *
     * @return new {@code List<TestProfile>}.
     */
    @GetMapping("/all")
    public List<TestProfile> getUnarchivedTests() {
        return testService.findUnarchivedTestProfiles();
    }

    /**
     * Use this endpoint to get all archived tests.
     * The controller returns a list of test DTOs {@code List<TestProfile>}.
     *
     * @return new {@code List<TestProfile>}.
     */
    @GetMapping("/all/archived")
    public List<TestProfile> getArchivedTests() {
        return testService.findArchivedTestProfiles();
    }

    /**
     * Use this endpoint to get general information about a specific test.
     * The controller returns a test DTO {@code ViewTest}.
     *
     * @param id - put test id here.
     * @return new {@code ViewTest}.
     */
    @GetMapping("/{id}")
    public ViewTest viewTest(@PathVariable Long id) {
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
    @GetMapping(value = "/{id}/passing")
    public PassTest passTest(@PathVariable Long id) {
        return testService.findPassTestById(id);
    }

    /**
     * Use this endpoint to get a list of groups that contain a specific test.
     * This controller return a list of group DTOs {@code List<ResponseGroup>}.
     *
     * @param id - put test id here.
     * @return new {@code List<ResponseGroup>}.
     */
    @GetMapping(value = "/{id}/groups")
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
    @PostMapping
    public SuccessCreatedTest addTest(@RequestBody CreateTest test) {
        return testService.addTest(test);
    }

    /**
     * Use this endpoint to archive the test.
     *
     * @param id - post test id here.
     */
    @PostMapping("/{id}/archive")
    public TestProfile archiveTest(@PathVariable Long id) {
        return testService.archiveTestById(id);
    }

    /**
     * Use this endpoint to restore the test.
     *
     * @param id - post test id here.
     */
    @PostMapping("/{id}/restore")
    public TestProfile restoreTest(@PathVariable Long id) {
        return testService.restoreTestById(id);
    }
}
