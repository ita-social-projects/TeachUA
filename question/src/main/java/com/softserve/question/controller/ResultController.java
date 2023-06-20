package com.softserve.question.controller;

import com.softserve.question.controller.marker.Api;
import com.softserve.question.dto.result.CreateResult;
import com.softserve.question.dto.result.SuccessCreatedResult;
import com.softserve.question.dto.result.UserResult;
import com.softserve.question.dto.test.ResultTest;
import com.softserve.question.service.ResultService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is for managing results.
 */
@RestController
@RequestMapping("/api/v1/test/result")
public class ResultController implements Api {
    private final ResultService resultService;

    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    /**
     * Use this endpoint to get all user results in a specific group.
     * This controller returns a list of result DTOs.
     *
     * @param groupId - put group id here.
     * @param userId  - put user id here.
     * @return new {@code List<UserResult>}
     */
    @GetMapping(params = {"groupId", "userId"})
    public List<UserResult> getUserResults(@RequestParam("groupId") Long groupId,
                                           @RequestParam("userId") Long userId) {
        return resultService.findUserResultsByGroupIdAndUserId(groupId, userId);
    }

    /**
     * Use this endpoint to get all user results in a specific group with a specific test.
     * This controller returns a list of result DTOs.
     *
     * @param groupId - put group id here.
     * @param userId  - put user id here.
     * @param testId  - put test id here.
     * @return new {@code List<UserResult>}
     */
    @GetMapping(params = {"groupId", "userId", "testId"})
    public List<UserResult> getUserResultsByTest(@RequestParam("groupId") Long groupId,
                                                 @RequestParam("userId") Long userId,
                                                 @RequestParam("testId") Long testId) {
        return resultService.findUserResultsByGroupIdAndUserIdAndTestId(groupId, userId, testId);
    }

    /**
     * Use this endpoint to get information about the result of passing a test.
     * This controller returns a test DTO {@code ResultTest}.
     *
     * @param resultId - put result id here.
     * @return new {@code ResultTest}.
     */
    @GetMapping(value = "/{resultId}")
    public ResultTest getTestResult(@PathVariable Long resultId) {
        return resultService.getDetailedResultById(resultId);
    }

    /**
     * Use this endpoint to store the test result.
     * This controller returns a result DTO {@code SuccessCreatedResult}
     * which contains general information about the test result.
     *
     * @param result - put result details here.
     * @return new {@code SuccessCreatedResult}.
     */
    @PostMapping
    public SuccessCreatedResult saveResult(@RequestBody CreateResult result) {
        return resultService.saveResult(result);
    }
}
