package com.softserve.teachua.service.test.impl;

import com.softserve.teachua.model.test.Answer;
import com.softserve.teachua.repository.test.AnswerRepository;
import com.softserve.teachua.service.test.AnswerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class AnswerServiceImpl implements AnswerService {
    private final AnswerRepository answerRepository;

    @Override
    public List<Answer> findByQuestionId(Long id) {
        if (Objects.isNull(id))
            throw new IllegalArgumentException("Question id can't be null");

        return answerRepository.findAllByQuestionId(id);
    }

    @Override
    public List<Answer> findAllById(List<Long> ids) {
        return answerRepository.findAllById(ids);
    }
}
