package com.softserve.teachua.service.test;

import com.softserve.teachua.dto.test.result.CreateResult;
import com.softserve.teachua.model.User;
import com.softserve.teachua.model.test.Answer;
import com.softserve.teachua.model.test.Question;
import com.softserve.teachua.model.test.Result;

import java.util.List;

public interface ResultService {
    Result findById(Long id);
    int countGrade(CreateResult resultDto, List<Question> questions);
    void createResult(Result result, List<Answer> selectedAnswers);
    List<Result> findByUser(User user);
}
