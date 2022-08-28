package com.softserve.teachua.service.test;

import com.softserve.teachua.dto.test.question.QuestionResponse;
import com.softserve.teachua.model.test.Question;
import com.softserve.teachua.repository.test.QuestionRepository;
import com.softserve.teachua.service.test.impl.QuestionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private AnswerService answerService;

    @InjectMocks
    private QuestionServiceImpl questionService;

    private Question question;
    private QuestionResponse questionResponse;
    private com.softserve.teachua.model.test.Test test;

    private final Long EXISTING_TEST_ID = 1L;
    private final Long NOT_EXISTING_TEST_ID = 100L;

    @BeforeEach
    public void setUp() {
        Long existingQuestionId = 1L;
        String descriptionOfQuestion = "Description of question with id %d";
        String titleOfQuestion = "Title of question with id %d";
        question = Question.builder()
                .id(existingQuestionId)
                .title(String.format(titleOfQuestion, 1L))
                .description(String.format(descriptionOfQuestion, 1L))
                .answers(Collections.emptySet())
                .build();
        questionResponse = QuestionResponse.builder()
                .title(String.format(titleOfQuestion, 1L))
                .description(String.format(descriptionOfQuestion, 1L))
                .answerTitles(Collections.emptyList())
                .build();
        test = com.softserve.teachua.model.test.Test.builder()
                .id(EXISTING_TEST_ID)
                .build();
    }

    @Test
    void findByExistingTestIdShouldReturnListOfQuestions() {
        when(questionRepository.findQuestionsByTestId(EXISTING_TEST_ID))
                .thenReturn(Collections.singletonList(question));

        List<Question> actual = questionService.findQuestionsByTestId(EXISTING_TEST_ID);
        assertEquals(question, actual.get(0));
    }

    @Test
    void findByNotExistingTestIdShouldReturnEmptyList() {
        when(questionRepository.findQuestionsByTestId(NOT_EXISTING_TEST_ID)).thenReturn(Collections.emptyList());

        List<Question> actual = questionService.findQuestionsByTestId(NOT_EXISTING_TEST_ID);
        assertEquals(Collections.emptyList(), actual);
    }

    @Test
    void findByNullTestIdShouldThrowIllegalArgException() {
        assertThatThrownBy(() -> questionService.findQuestionsByTestId(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findByExistingTestIdEagerShouldReturnListOfQuestions() {
        when(questionRepository.findAllQuestionsByTestIdFetch(EXISTING_TEST_ID))
                .thenReturn(Collections.singletonList(question));

        List<Question> actual = questionService.findQuestionsByTestIdEager(EXISTING_TEST_ID);
        assertEquals(question, actual.get(0));
    }

    @Test
    void findByNotExistingTestIdEagerShouldReturnEmptyList() {
        when(questionRepository.findAllQuestionsByTestIdFetch(NOT_EXISTING_TEST_ID)).thenReturn(Collections.emptyList());

        List<Question> actual = questionService.findQuestionsByTestIdEager(NOT_EXISTING_TEST_ID);
        assertEquals(Collections.emptyList(), actual);
    }

    @Test
    void findByNullTestIdEagerShouldThrowIllegalArgException() {
        assertThatThrownBy(() -> questionService.findQuestionsByTestIdEager(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findByExistingTestShouldReturnListOfQuestions() {
        when(questionRepository.findQuestionsByTestId(EXISTING_TEST_ID))
                .thenReturn(Collections.singletonList(question));

        List<Question> actual = questionService.findQuestionsByTest(test);
        assertEquals(question, actual.get(0));
    }

    @Test
    void findByNullTestShouldThrowIllegalArgException() {
        assertThatThrownBy(() -> questionService.findQuestionsByTest(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findQuestionResponseByExistingTestIdShouldReturnListOfQuestionResponses() {
        when(questionRepository.findQuestionsByTestId(EXISTING_TEST_ID))
                .thenReturn(Collections.singletonList(question));
        when(modelMapper.map(question, QuestionResponse.class)).thenReturn(questionResponse);
        when(answerService.findByQuestionId(question.getId())).thenReturn(Collections.emptyList());

        List<QuestionResponse> actual = questionService.findQuestionResponsesByTestId(EXISTING_TEST_ID);
        assertEquals(questionResponse, actual.get(0));
    }

    @Test
    void findQuestionResponseByNotExistingTestIdShouldReturnEmptyList() {
        when(questionRepository.findQuestionsByTestId(NOT_EXISTING_TEST_ID)).thenReturn(Collections.emptyList());

        List<QuestionResponse> actual = questionService.findQuestionResponsesByTestId(NOT_EXISTING_TEST_ID);
        assertEquals(Collections.emptyList(), actual);
    }

    @Test
    void findQuestionResponseByNullTestIdShouldThrowIllegalArgException() {
        assertThatThrownBy(() -> questionService.findQuestionResponsesByTestId(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findQuestionResponseByExistingTestShouldReturnListOfQuestionResponses() {
        com.softserve.teachua.model.test.Test test = com.softserve.teachua.model.test.Test.builder()
                .id(EXISTING_TEST_ID)
                .build();
        when(questionRepository.findQuestionsByTestId(EXISTING_TEST_ID))
                .thenReturn(Collections.singletonList(question));
        when(modelMapper.map(question, QuestionResponse.class)).thenReturn(questionResponse);
        when(answerService.findByQuestionId(question.getId())).thenReturn(Collections.emptyList());

        List<QuestionResponse> actual = questionService.findQuestionResponsesByTest(test);
        assertEquals(questionResponse, actual.get(0));
    }

    @Test
    void findQuestionResponseByNullTestShouldThrowIllegalArgException() {
        assertThatThrownBy(() -> questionService.findQuestionResponsesByTest(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void saveQuestionShouldReturnSameQuestion() {
        when(questionRepository.save(question)).thenReturn(question);

        assertEquals(question, questionService.save(question));
    }

    @Test
    void saveNullQuestionShouldThrowIllegalArgException() {
        assertThatThrownBy(() -> questionService.save(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}