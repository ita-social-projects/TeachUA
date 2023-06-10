package com.softserve.question.service.impl;

import com.softserve.question.model.QuestionTest;
import com.softserve.question.repository.QuestionTestRepository;
import com.softserve.question.service.QuestionTestService;
import static com.softserve.question.util.validation.NullValidator.checkNull;
import static com.softserve.question.util.Messages.QUESTION_IS_NULL_MESSAGE;
import static com.softserve.question.util.Messages.TEST_IS_NULL_MESSAGE;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class QuestionTestServiceImpl implements QuestionTestService {
    private final QuestionTestRepository questionTestRepository;

    @Override
    public QuestionTest save(QuestionTest questionTest) {
        checkNull(questionTest, "Question-test");
        validate(questionTest);
        return questionTestRepository.save(questionTest);
    }

    private void validate(QuestionTest questionTest) {
        String message = "";
        if (Objects.isNull(questionTest.getQuestion())) {
            message += QUESTION_IS_NULL_MESSAGE;
        }

        if (Objects.isNull(questionTest.getTest())) {
            message += TEST_IS_NULL_MESSAGE;
        }

        if (!message.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }
}
