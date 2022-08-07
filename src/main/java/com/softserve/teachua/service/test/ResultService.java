package com.softserve.teachua.service.test;

import com.softserve.teachua.dto.test.result.CreateResult;
import com.softserve.teachua.dto.test.result.SuccessCreatedResult;
import com.softserve.teachua.dto.test.result.UserResult;
import com.softserve.teachua.dto.test.test.ResultTest;
import com.softserve.teachua.model.test.Answer;
import com.softserve.teachua.model.test.Result;

import java.util.List;

/**
 * This interface contains all methods needed to manage results.
 */
public interface ResultService {
    /**
     * This method returns entity {@code Result} of result by id.
     * @param id - put result id.
     * @return new {@code Result}.
     */
    Result findById(Long id);

    /**
     * This method returns an integer value of grade of a certain result.
     * @param answers - put list of selected answers.
     * @return int
     */
    int countGrade(List<Answer> answers);

    /**
     * This method adds result and answer history.
     * @param result - put result entity.
     * @param selectedAnswers - put list of answer entities.
     */
    void createResult(Result result, List<Answer> selectedAnswers);

    /**
     * This method returns all results by user id.
     * @param userId - put user id here.
     * @return new {@code List<Result>}
     */
    List<Result> findResultsByUserId(Long userId);

    /**
     * This method returns dto {@code ResultTest} by test id and result id.
     * @param resultId - put result id.
     * @return new {@code ResultTest}.
     */
    ResultTest getResultTest(Long resultId);

    /**
     * This method returns dto {@code SuccessCreatedResult} if result was successfully added.
     * @param resultDto - put body of dto {@code CreateResult}.
     * @return new {@code SuccessCreatedResult}.
     */
    SuccessCreatedResult saveResult(CreateResult resultDto);

    /**
     * This method returns all result DTOs by user id and group id.
     * @param groupId - put group id here.
     * @param userId - put user id here.
     * @return new {@code List<UserResult>}
     */
    List<UserResult> findUserResultsByGroupIdAndUserId(Long groupId, Long userId);

    /**
     * This method returns all result DTOs by user id and group id and test id.
     * @param groupId - put group id here.
     * @param userId - put user id here.
     * @param testId - put test id here.
     * @return new {@code List<UserResult>}
     */
    List<UserResult> findUserResultsByGroupIdAndUserIdAndTestId(Long groupId, Long userId, Long testId);
}
