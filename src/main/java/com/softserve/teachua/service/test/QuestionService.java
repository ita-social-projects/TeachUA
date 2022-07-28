package com.softserve.teachua.service.test;

import com.softserve.teachua.dto.test.question.QuestionResponse;
import com.softserve.teachua.model.test.Question;
import com.softserve.teachua.model.test.Test;

import java.util.List;

public interface QuestionService {
    List<QuestionResponse> findQuestionsByTestId(Long test);
    List<QuestionResponse> findQuestionsByTest(Test test);
    Question save(Question question);
}
