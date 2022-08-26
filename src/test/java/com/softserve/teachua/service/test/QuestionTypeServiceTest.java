package com.softserve.teachua.service.test;

import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.test.QuestionType;
import com.softserve.teachua.repository.test.QuestionTypeRepository;
import com.softserve.teachua.service.test.impl.QuestionTypeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith({MockitoExtension.class})
class QuestionTypeServiceTest {

    @Mock
    private QuestionTypeRepository questionTypeRepository;

    @InjectMocks
    private QuestionTypeServiceImpl questionTypeService;

    private QuestionType questionType;
    private final String EXISTING_TITLE = "Existing title";

    @BeforeEach
    public void setUp() {
        questionType = QuestionType.builder()
                .title(EXISTING_TITLE)
                .build();
    }

    @Test
    void findByExistingTitleShouldReturnQuestionType() {
        when(questionTypeRepository.findByTitle(EXISTING_TITLE)).thenReturn(Optional.of(questionType));

        QuestionType actual = questionTypeService.findByTitle(EXISTING_TITLE);
        assertEquals(questionType, actual);
    }

    @Test
    void findByNotExistingTitleShouldThrowNotExistException() {
        String notExistingTitle = "Not existing title";
        when(questionTypeRepository.findByTitle(notExistingTitle)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> questionTypeService.findByTitle(notExistingTitle))
                .isInstanceOf(NotExistException.class);
    }

    @Test
    void findByNullTitleShouldThrowIllegalArgException() {
        assertThatThrownBy(() -> questionTypeService.findByTitle(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}