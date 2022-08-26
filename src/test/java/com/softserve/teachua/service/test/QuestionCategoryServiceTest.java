package com.softserve.teachua.service.test;

import com.softserve.teachua.dto.test.questionCategory.QuestionCategoryProfile;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.test.QuestionCategory;
import com.softserve.teachua.repository.test.QuestionCategoryRepository;
import com.softserve.teachua.service.test.impl.QuestionCategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class QuestionCategoryServiceTest {

    @Mock
    private QuestionCategoryRepository questionCategoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private QuestionCategoryServiceImpl questionCategoryService;

    private QuestionCategory questionCategory;
    private QuestionCategoryProfile questionCategoryProfile;

    private final Long EXISTING_CATEGORY_ID = 1L;
    private final Long NOT_EXISTING_CATEGORY_ID = 100L;

    private final String EXISTING_CATEGORY_TITLE = "Existing Category Title";
    private final String NOT_EXISTING_CATEGORY_TITLE = "Not Existing Category Title";
    private final String NEW_CATEGORY_TITLE = "New Category Title";

    @BeforeEach
    public void setUp() {
        questionCategory = QuestionCategory.builder()
                .title(EXISTING_CATEGORY_TITLE)
                .build();
        questionCategoryProfile = QuestionCategoryProfile.builder()
                .title(EXISTING_CATEGORY_TITLE)
                .build();
    }

    @Test
    void findByExistingIdShouldReturnCategory() {
        when(questionCategoryRepository.findById(EXISTING_CATEGORY_ID)).thenReturn(Optional.of(questionCategory));

        QuestionCategory actual = questionCategoryService.findById(EXISTING_CATEGORY_ID);
        assertEquals(questionCategory, actual);
    }

    @Test
    void findByNotExistingIdShouldThrowNotExistException() {
        when(questionCategoryRepository.findById(NOT_EXISTING_CATEGORY_ID)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> questionCategoryService.findById(NOT_EXISTING_CATEGORY_ID))
                .isInstanceOf(NotExistException.class);
    }

    @Test
    void findByNullIdShouldThrowNotExistException() {
        assertThatThrownBy(() -> questionCategoryService.findById(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findByExistingTitleShouldReturnCategory() {
        when(questionCategoryRepository.findByTitle(EXISTING_CATEGORY_TITLE)).thenReturn(Optional.of(questionCategory));

        QuestionCategory actual = questionCategoryService.findByTitle(EXISTING_CATEGORY_TITLE);
        assertEquals(questionCategory, actual);
    }

    @Test
    void findByNotExistingTitleShouldThrowNotExistException() {
        when(questionCategoryRepository.findByTitle(NOT_EXISTING_CATEGORY_TITLE)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> questionCategoryService.findByTitle(NOT_EXISTING_CATEGORY_TITLE))
                .isInstanceOf(NotExistException.class);
    }

    @Test
    void findByNullTitleShouldThrowNotExistException() {
        assertThatThrownBy(() -> questionCategoryService.findByTitle(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findAllCategoryProfilesShouldReturnListOfCategoryProfiles() {
        when(questionCategoryRepository.findAll()).thenReturn(Collections.singletonList(questionCategory));
        when(modelMapper.map(questionCategory, QuestionCategoryProfile.class)).thenReturn(questionCategoryProfile);

        List<QuestionCategoryProfile> actual = questionCategoryService.findAllCategoryProfiles();
        assertEquals(questionCategoryProfile, actual.get(0));
    }

    @Test
    void saveCategoryWithNotExistingTitleShouldReturnCategoryProfile() {
        QuestionCategory newCategory = QuestionCategory.builder()
                .title(NEW_CATEGORY_TITLE)
                .build();
        QuestionCategoryProfile newCategoryProfile = QuestionCategoryProfile.builder()
                .title(NEW_CATEGORY_TITLE)
                .build();
        when(questionCategoryRepository.existsByTitle(NEW_CATEGORY_TITLE)).thenReturn(false);
        when(modelMapper.map(newCategoryProfile, QuestionCategory.class)).thenReturn(newCategory);
        when(questionCategoryRepository.save(newCategory)).thenReturn(newCategory);

        QuestionCategoryProfile actual = questionCategoryService.save(newCategoryProfile);
        assertEquals(newCategoryProfile, actual);
    }

    @Test
    void saveCategoryWithExistingTitleShouldThrowAlreadyExistException() {
        when(questionCategoryRepository.existsByTitle(EXISTING_CATEGORY_TITLE)).thenReturn(true);

        assertThatThrownBy(() -> questionCategoryService.save(questionCategoryProfile))
                .isInstanceOf(AlreadyExistException.class);
    }

    @Test
    void saveCategoryWithNullTitleShouldThrowIllegalArgException() {
        assertThatThrownBy(() -> questionCategoryService.save(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void updateCategoryWithNotExistingTitleShouldReturnCategoryProfile() {
        QuestionCategory newCategory = QuestionCategory.builder()
                .title(NEW_CATEGORY_TITLE)
                .build();
        QuestionCategoryProfile newCategoryProfile = QuestionCategoryProfile.builder()
                .title(NEW_CATEGORY_TITLE)
                .build();
        when(questionCategoryRepository.existsByTitle(NEW_CATEGORY_TITLE)).thenReturn(false);
        when(questionCategoryRepository.findById(EXISTING_CATEGORY_ID)).thenReturn(Optional.of(questionCategory));
        when(questionCategoryRepository.save(newCategory)).thenReturn(newCategory);

        QuestionCategoryProfile actual = questionCategoryService.updateById(newCategoryProfile, EXISTING_CATEGORY_ID);
        assertEquals(newCategoryProfile, actual);
    }

    @Test
    void updateCategoryWithNotExistingIdShouldThrowNotExistException() {
        when(questionCategoryRepository.existsByTitle(EXISTING_CATEGORY_TITLE)).thenReturn(false);
        when(questionCategoryRepository.findById(NOT_EXISTING_CATEGORY_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> questionCategoryService.updateById(questionCategoryProfile, NOT_EXISTING_CATEGORY_ID))
                .isInstanceOf(NotExistException.class);
    }

    @Test
    void updateCategoryWithExistingTitleShouldThrowAlreadyExistException() {
        when(questionCategoryRepository.existsByTitle(EXISTING_CATEGORY_TITLE)).thenReturn(true);

        assertThatThrownBy(() -> questionCategoryService.updateById(questionCategoryProfile, EXISTING_CATEGORY_ID))
                .isInstanceOf(AlreadyExistException.class);
    }

    @Test
    void updateCategoryWithNullTitleShouldThrowIllegalArgException() {
        assertThatThrownBy(() -> questionCategoryService.updateById(null, EXISTING_CATEGORY_ID))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void updateCategoryWithNullIdShouldThrowIllegalArgException() {
        assertThatThrownBy(() -> questionCategoryService.updateById(questionCategoryProfile, null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
