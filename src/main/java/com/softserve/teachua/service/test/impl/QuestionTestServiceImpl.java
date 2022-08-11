package com.softserve.teachua.service.test.impl;

import com.softserve.teachua.model.test.QuestionTest;
import com.softserve.teachua.repository.test.QuestionTestRepository;
import com.softserve.teachua.service.test.QuestionTestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.softserve.teachua.utils.test.Messages.*;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class QuestionTestServiceImpl implements QuestionTestService {
    private final QuestionTestRepository questionTestRepository;

    @Override
    public void save(QuestionTest questionTest) {
        validate(questionTest);
        questionTestRepository.save(questionTest);
    }

    private void validate(QuestionTest questionTest) {
        String message = "";
        if (Objects.isNull(questionTest.getQuestion()))
            message += QUESTION_IS_NULL_MESSAGE;

        if (Objects.isNull(questionTest.getTest()))
            message += TEST_IS_NULL_MESSAGE;

        if (!message.isEmpty())
            throw new IllegalArgumentException(message);
    }
}
