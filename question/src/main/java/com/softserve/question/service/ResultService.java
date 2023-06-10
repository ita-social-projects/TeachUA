package com.softserve.question.service;

import com.softserve.question.dto.result.CreateResult;
import com.softserve.question.dto.result.SuccessCreatedResult;
import com.softserve.question.dto.result.UserResult;
import com.softserve.question.dto.test.ResultTest;
import com.softserve.question.model.Answer;
import com.softserve.question.model.Result;
import java.util.List;

/**
 * This interface contains all methods needed to manage results.
 */
public interface ResultService {
    /**
     * Find a result by given id.
     *
     * @param id result id
     * @return entity {@code Result}
     */
    Result findById(Long id);

    /**
     * Calculate an integer value of grade of a certain result.
     *
     * @param answers a list of selected answers.
     * @return a grade represented by an integer value
     */
    int countGrade(List<Answer> answers);

    /**
     * Find all results by given user id.
     *
     * @param userId user id
     * @return all results by user id
     */
    List<Result> findResultsByUserId(Long userId);

    /**
     * Get dto {@code ResultTest} by result id.
     *
     * @param resultId result id
     * @return dto {@code ResultTest} by result id.
     */
    ResultTest getDetailedResultById(Long resultId);

    /**
     * Create a new result from the dto {@code CreateResult}.
     *
     * @param resultDto body of dto {@code CreateResult}
     * @return dto {@code SuccessCreatedResult} if a result was successfully added
     */
    SuccessCreatedResult saveResult(CreateResult resultDto);

    /**
     * Find user results by given user id and group id.
     *
     * @param groupId group id
     * @param userId user id
     * @return all result DTOs by user id and group id
     */
    List<UserResult> findUserResultsByGroupIdAndUserId(Long groupId, Long userId);

    /**
     * Find user results by given user id, group id and test id.
     *
     * @param groupId group id
     * @param userId user id
     * @param testId test id
     * @return all result DTOs by user id, group id and test id.
     */
    List<UserResult> findUserResultsByGroupIdAndUserIdAndTestId(Long groupId, Long userId, Long testId);
}
