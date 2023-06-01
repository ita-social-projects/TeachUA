package com.softserve.teachua.service.test;

import com.softserve.teachua.model.test.Answer;
import com.softserve.teachua.repository.test.AnswerRepository;
import com.softserve.teachua.service.test.impl.AnswerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnswerServiceTest {

    @Mock
    private AnswerRepository answerRepository;

    @InjectMocks
    private AnswerServiceImpl answerService;

    private List<Answer> listOfAnswers;
    private List<Long> existingIds;
    private List<Long> notExistingIds;

    @BeforeEach
    public void setUp() {
        existingIds = Arrays.asList(1L, 2L, 3L);
        notExistingIds = Arrays.asList(100L, 200L, 300L);
        listOfAnswers = new ArrayList<>();
        String textOfAnswer = "Text of answer with id %d";
        for (Long l : existingIds) {
            Answer answer = Answer.builder()
                    .id(l)
                    .correct(true)
                    .text(String.format(textOfAnswer, l))
                    .value(1)
                    .build();
            listOfAnswers.add(answer);
        }
    }

    @Test
    void findByExistingQuestionIdShouldReturnListOfAnswers() {
        Long existingQuestionId = 1L;
        when(answerRepository.findAllByQuestionId(existingQuestionId)).thenReturn(listOfAnswers);

        List<Answer> actual = answerService.findByQuestionId(existingQuestionId);
        assertEquals(listOfAnswers, actual);
    }

    @Test
    void findByNotExistingQuestionIdShouldReturnEmptyList() {
        Long notExistingQuestionId = 100L;
        when(answerRepository.findAllByQuestionId(notExistingQuestionId)).thenReturn(Collections.emptyList());

        List<Answer> actual = answerService.findByQuestionId(notExistingQuestionId);
        assertEquals(Collections.emptyList(), actual);
    }

    @Test
    void findByNullQuestionIdShouldThrowIllegalArgException() {
        assertThatThrownBy(() -> answerService.findByQuestionId(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findAllByExistingIdsShouldReturnListOfAnswers() {
        when(answerRepository.findAllById(existingIds)).thenReturn(listOfAnswers);

        List<Answer> actual = answerService.findAllById(existingIds);
        assertEquals(listOfAnswers, actual);
    }

    @Test
    void findAllByNotExistingIdsShouldReturnEmptyList() {
        when(answerRepository.findAllById(notExistingIds)).thenReturn(Collections.emptyList());

        List<Answer> actual = answerService.findAllById(notExistingIds);
        assertEquals(Collections.emptyList(), actual);
    }
}