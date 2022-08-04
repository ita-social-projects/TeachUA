package com.softserve.teachua.service.test;

import com.softserve.teachua.dto.test.result.CreateResult;
import com.softserve.teachua.model.User;
import com.softserve.teachua.model.test.Answer;
import com.softserve.teachua.model.test.Question;
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
     * @param resultDto - put body of dto {@code CreateResult}.
     * @param questions - put list of test questions.
     * @return int
     */
    int countGrade(CreateResult resultDto, List<Question> questions);

    /**
     * This method adds result and answer history.
     * @param result - put result entity.
     * @param selectedAnswers - put list of answer entities.
     */
    void createResult(Result result, List<Answer> selectedAnswers);
    List<Result> findByUser(User user);
}
