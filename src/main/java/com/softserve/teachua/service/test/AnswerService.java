package com.softserve.teachua.service.test;

import com.softserve.teachua.model.test.Answer;

import java.util.List;

public interface AnswerService {
    List<Answer> findByQuestionId(Long id);
}
