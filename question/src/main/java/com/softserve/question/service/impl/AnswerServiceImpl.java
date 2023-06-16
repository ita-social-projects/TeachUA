package com.softserve.question.service.impl;

import com.softserve.question.model.Answer;
import com.softserve.question.repository.AnswerRepository;
import com.softserve.question.service.AnswerService;
import static com.softserve.question.util.validation.NullValidator.checkNull;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
public class AnswerServiceImpl implements AnswerService {
    private final AnswerRepository answerRepository;

    public AnswerServiceImpl(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Answer> findByQuestionId(Long questionId) {
        checkNull(questionId, "Question id");
        return answerRepository.findAllByQuestionId(questionId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Answer> findAllById(List<Long> ids) {
        return answerRepository.findAllById(ids);
    }
}
