package com.softserve.teachua.service.test;

import com.softserve.teachua.dto.test.question.QuestionResponse;
import com.softserve.teachua.model.test.Question;
import com.softserve.teachua.model.test.Test;

import java.util.List;

public interface QuestionService {
    List<Question> findQuestionsByTestId(Long testId);
    List<Question> findQuestionsByTest(Test test);
    List<QuestionResponse> findQuestionResponsesByTestId(Long test);
    List<QuestionResponse> findQuestionResponsesByTest(Test test);
    Question save(Question question);
}
