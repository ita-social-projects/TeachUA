package com.softserve.teachua.controller.test;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.test.result.CreateResult;
import com.softserve.teachua.dto.test.result.SuccessCreatedResult;
import com.softserve.teachua.dto.test.result.UserResult;
import com.softserve.teachua.dto.test.test.ResultTest;
import com.softserve.teachua.service.test.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * This controller is for managing results.
 * */

@RequiredArgsConstructor
@RestController
public class ResultController implements Api {
    private final ResultService resultService;

    /**
     * Use this endpoint to get all user results in a specific group.
     * This controller returns a list of result DTOs.
     *
     * @param groupId - put group id here.
     * @param userId  - put user id here.
     * @return new {@code List<UserResult>}
     */
    @GetMapping(value = "/results/groups/{groupId}/users/{userId}",
            produces = APPLICATION_JSON_VALUE)
    public List<UserResult> getUserResults(@PathVariable Long groupId,
                                           @PathVariable Long userId) {
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
    @GetMapping(value = "/results/groups/{groupId}/users/{userId}/tests/{testId}",
            produces = APPLICATION_JSON_VALUE)
    public List<UserResult> getUserResultsByTest(@PathVariable Long groupId,
                                                 @PathVariable Long userId,
                                                 @PathVariable Long testId) {
        return resultService.findUserResultsByGroupIdAndUserIdAndTestId(groupId, userId, testId);
    }

    /**
     * Use this endpoint to get information about the result of passing a test.
     * This controller returns a test DTO {@code ResultTest}.
     *
     * @param resultId - put result id here.
     * @return new {@code ResultTest}.
     */
    @GetMapping(value = "/results/{resultId}",
            produces = APPLICATION_JSON_VALUE)
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
    @ResponseStatus(value = CREATED)
    @PostMapping(value = "/results",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public SuccessCreatedResult saveResult(@RequestBody CreateResult result){
        return resultService.saveResult(result);
    }
}
