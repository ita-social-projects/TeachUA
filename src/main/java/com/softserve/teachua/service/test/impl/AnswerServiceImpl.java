package com.softserve.teachua.service.test.impl;

import com.softserve.teachua.model.test.Answer;
import com.softserve.teachua.repository.test.AnswerRepository;
import com.softserve.teachua.service.test.AnswerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.softserve.teachua.utils.NullValidator.*;


@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class AnswerServiceImpl implements AnswerService {
    private final AnswerRepository answerRepository;

    @Override
    public List<Answer> findByQuestionId(Long questionId) {
        checkNull(questionId, "Question id");
        return answerRepository.findAllByQuestionId(questionId);
    }

    @Override
    public List<Answer> findAllById(List<Long> ids) {
        return answerRepository.findAllById(ids);
    }
}
