package com.softserve.teachua.service.test.impl;

import com.softserve.teachua.model.test.QuestionTest;
import com.softserve.teachua.repository.test.QuestionTestRepository;
import com.softserve.teachua.service.test.QuestionTestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class QuestionTestServiceImpl implements QuestionTestService {
    private final QuestionTestRepository questionTestRepository;

    public void save(QuestionTest questionTest) {
        validate(questionTest);
        questionTestRepository.save(questionTest);
    }

    private void validate(QuestionTest questionTest) {
        String message = "";
        if (Objects.isNull(questionTest.getQuestion()))
            message += "Question in questionTest is null;";

        if (Objects.isNull(questionTest.getTest()))
            message += "Test in questionTest is null;";

        if (!message.isEmpty())
            throw new IllegalArgumentException(message);
    }
}
