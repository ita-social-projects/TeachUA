package com.softserve.teachua.service.test.impl;

import com.softserve.teachua.model.test.Answer;
import com.softserve.teachua.repository.test.AnswerRepository;
import com.softserve.teachua.service.test.AnswerService;
import static com.softserve.teachua.utils.test.validation.NullValidator.checkNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class AnswerServiceImpl implements AnswerService {
    private final AnswerRepository answerRepository;

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
