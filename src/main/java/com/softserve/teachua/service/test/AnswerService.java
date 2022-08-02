package com.softserve.teachua.service.test;

import com.softserve.teachua.model.test.Answer;

import java.util.List;
import java.util.Optional;

public interface AnswerService {
    List<Answer> findByQuestionId(Long id);
    List<Answer> findAllById(List<Long> ids);
}
