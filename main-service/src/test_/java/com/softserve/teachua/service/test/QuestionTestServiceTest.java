package com.softserve.teachua.service.test;

import com.softserve.teachua.model.test.Question;
import com.softserve.teachua.model.test.QuestionTest;
import com.softserve.teachua.model.test.Test;
import com.softserve.teachua.repository.test.QuestionTestRepository;
import com.softserve.question.service.impl.QuestionTestServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class QuestionTestServiceTest {

    @Mock
    private QuestionTestRepository questionTestRepository;

    @InjectMocks
    private QuestionTestServiceImpl questionTestService;

    private QuestionTest questionTest;
    private Question question;
    private Test test;

    private final Long EXISTING_QUESTION_ID = 1L;
    private final Long EXISTING_TEST_ID = 1L;

    @org.junit.jupiter.api.Test
    void saveNullShouldThrowIllegalArgumentException() {
        assertThatThrownBy(() -> questionTestService.save(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @org.junit.jupiter.api.Test
    void saveQuestionTestWithNullQuestionShouldThrowIllegalArgumentException() {
        question = null;
        test = Test.builder()
                .id(EXISTING_TEST_ID)
                .build();
        questionTest = QuestionTest.builder()
                .test(test)
                .question(question)
                .build();
        assertThatThrownBy(() -> questionTestService.save(questionTest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @org.junit.jupiter.api.Test
    void saveQuestionTestWithNullTestShouldThrowIllegalArgumentException() {
        question = Question.builder()
                .id(EXISTING_QUESTION_ID)
                .build();
        test = null;
        questionTest = QuestionTest.builder()
                .test(test)
                .question(question)
                .build();
        assertThatThrownBy(() -> questionTestService.save(questionTest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @org.junit.jupiter.api.Test
    void saveQuestionTestShouldReturnQuestionTest() {
        question = Question.builder()
                .id(EXISTING_QUESTION_ID)
                .build();
        test = Test.builder()
                .id(EXISTING_TEST_ID)
                .build();
        questionTest = QuestionTest.builder()
                .test(test)
                .question(question)
                .build();
        when(questionTestRepository.save(questionTest)).thenReturn(questionTest);

        QuestionTest actual = questionTestService.save(questionTest);
        assertEquals(questionTest, actual);
    }
}
