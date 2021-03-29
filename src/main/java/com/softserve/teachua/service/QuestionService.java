package com.softserve.teachua.service;

import com.softserve.teachua.dto.question.QuestionProfile;
import com.softserve.teachua.dto.question.QuestionResponse;
import com.softserve.teachua.model.Question;

import java.util.List;

public interface QuestionService {
    Question getQuestionById(Long id);

    QuestionResponse addQuestion(QuestionProfile questionProfile);

    QuestionProfile updateQuestionById(Long id, QuestionProfile questionProfile);

    QuestionProfile deleteQuestionById(Long id);

    List<QuestionResponse> getAllQuestions();
}
