package com.softserve.teachua.service.test;

import com.softserve.teachua.dto.test.questionType.QuestionTypeProfile;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.EntityIsUsedException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.test.QuestionType;
import com.softserve.teachua.repository.test.QuestionRepository;
import com.softserve.teachua.repository.test.QuestionTypeRepository;
import com.softserve.teachua.service.test.impl.QuestionTypeServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith({MockitoExtension.class})
class QuestionTypeServiceTest {
    @Mock
    private QuestionTypeRepository questionTypeRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Spy
    private ModelMapper modelMapper = new ModelMapper();

    @InjectMocks
    private QuestionTypeServiceImpl questionTypeService;

    private QuestionType questionType;
    private static final String EXISTING_TITLE = "Existing title";
    private static final long EXISTING_ID = 1;
    private static final long NON_EXISTING_ID = 2;
    private static QuestionTypeProfile questionTypeProfile;

    @BeforeAll
    static void setAllUp() {
        questionTypeProfile = QuestionTypeProfile.builder()
                .title(EXISTING_TITLE)
                .build();
    }

    @BeforeEach
    public void setUp() {
        questionType = QuestionType.builder()
                .title(EXISTING_TITLE)
                .build();
    }

    @Test
    @DisplayName("Finding category by correct id returns correct result")
    void findByCorrectId() {
        when(questionTypeRepository.findById(EXISTING_ID)).thenReturn(Optional.of(questionType));

        QuestionType actual = questionTypeService.findById(EXISTING_ID);
        assertEquals(questionType, actual);
    }

    @Test
    @DisplayName("Finding category by wrong id throws NonExistException")
    void findByWrongId() {
        assertThrows(NotExistException.class, () -> questionTypeService.findById(NON_EXISTING_ID));
    }

    @Test
    @DisplayName("Saving question type works")
    void saveCorrect() {
        assertEquals(questionTypeProfile, questionTypeService.save(questionTypeProfile));
    }

    @Test
    @DisplayName("Saving existing question type throws AlreadyExistException")
    void saveException() {
        when(questionTypeRepository.existsByTitle(anyString())).thenReturn(true);
        assertThrows(AlreadyExistException.class, () -> questionTypeService.save(questionTypeProfile));
    }

    @Test
    @DisplayName("Updating question type works")
    void updateCorrect() {
        when(questionTypeRepository.findById(EXISTING_ID)).thenReturn(Optional.of(questionType));
        assertEquals(questionTypeProfile, questionTypeService.updateById(questionTypeProfile, EXISTING_ID));
    }

    @Test
    @DisplayName("Updating non existing question type throws NotExistException")
    void updateException() {
        assertThrows(NotExistException.class, () -> questionTypeService.updateById(questionTypeProfile, NON_EXISTING_ID));
    }

    @Test
    @DisplayName("Deleting question type works")
    void deleteCorrect() {
        assertDoesNotThrow(() -> questionTypeService.deleteById(EXISTING_ID));
    }

    @Test
    @DisplayName("Deleting a used question type throws EntityIsUsedException")
    void deleteException() {
        when(questionRepository.existsByQuestionTypeId(EXISTING_ID)).thenReturn(true);
        assertThrows(EntityIsUsedException.class, () -> questionTypeService.deleteById(EXISTING_ID));
    }

    @Test
    @DisplayName("Finding all question types works")
    void findAllCorrect() {
        when(questionTypeRepository.findAll()).thenReturn(Collections.singletonList(questionType));
        assertEquals(Collections.singletonList(questionType), questionTypeService.findAll());
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
