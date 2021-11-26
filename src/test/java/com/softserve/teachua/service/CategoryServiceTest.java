package com.softserve.teachua.service;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.category.CategoryProfile;
import com.softserve.teachua.dto.category.CategoryResponse;
import com.softserve.teachua.dto.category.SuccessCreatedCategory;
import com.softserve.teachua.dto.search.SearchPossibleResponse;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.DatabaseRepositoryException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Category;
import com.softserve.teachua.repository.CategoryRepository;
import com.softserve.teachua.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.validation.ValidationException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    private final static Long CORRECT_ID = 1L;
    private final static Long WRONG_ID = 1000L;
    private final static String CORRECT_NAME = "Correct name";
    private final static String WRONG_NAME = "Wrong name";
    private final PageRequest pageRequest = PageRequest.of(0, 2, Sort.by("sortby"));
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private DtoConverter dtoConverter;
    @Mock
    private ArchiveService archiveService;
    @InjectMocks
    private CategoryServiceImpl categoryService;
    private Category correctCategory = Category.builder()
            .id(CORRECT_ID)
            .name(CORRECT_NAME)
            .build();
    ;
    private CategoryProfile categoryProfile = CategoryProfile.builder()
            .id(CORRECT_ID)
            .name(CORRECT_NAME)
            .build();
    private List<Category> list;
    private List<CategoryResponse> responseList;

    @BeforeEach
    public void setMocks() {
        list = new LinkedList<>();
        for (long i = 1L; i <= 3; i++) {
            list.add(Category.builder()
                    .id(i)
                    .name(i + " category")
                    .sortby((int) (i * 10))
                    .build());
        }
        responseList = new LinkedList<>();
        for (Category category : list) {
            responseList.add(CategoryResponse.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .sortby(category.getSortby())
                    .build());
        }
    }

    @Test
    public void getCategoryByCorrectIdShouldReturnCorrectCategory() {
        setMockReturnOptionalWhenFindByCorrectId();
        assertThat(categoryService.getCategoryById(CORRECT_ID)).isEqualTo(correctCategory);
    }

    @Test
    public void getCategoryByWrongIdShouldThrowNotExistException() {
        when(categoryRepository.findById(WRONG_ID)).thenReturn(Optional.empty());
        NotExistException exception = assertThrows(NotExistException.class, () -> categoryService.getCategoryById(WRONG_ID));
        assertTrue(exception.getMessage().contains(WRONG_ID.toString()));
    }

    @Test
    public void getCategoryByNameIdShouldReturnCorrectCategory() {
        when(categoryRepository.findByName(CORRECT_NAME)).thenReturn(Optional.of(correctCategory));
        assertThat(categoryService.getCategoryByName(CORRECT_NAME)).isEqualTo(correctCategory);
    }

    @Test
    public void getCategoryByWrongNameShouldThrowNotExistException() {
        when(categoryRepository.findByName(WRONG_NAME)).thenReturn(Optional.empty());
        NotExistException exception = assertThrows(NotExistException.class, () -> categoryService.getCategoryByName(WRONG_NAME));
        assertTrue(exception.getMessage().contains(WRONG_NAME));
    }

    @Test
    public void addCategoryShouldSuccessfullyCreateCategory() {
        when(categoryRepository.existsByName(categoryProfile.getName())).thenReturn(false);
        when(dtoConverter.convertToEntity(categoryProfile, new Category())).thenReturn(correctCategory);
        when(categoryRepository.save(correctCategory)).thenReturn(correctCategory);
        SuccessCreatedCategory successCreatedCategory =
                SuccessCreatedCategory.builder()
                        .id(categoryProfile.getId())
                        .name(categoryProfile.getName()).build();
        when(dtoConverter.convertToDto(correctCategory, SuccessCreatedCategory.class)).thenReturn(successCreatedCategory);
        assertThat(categoryService.addCategory(categoryProfile)).isEqualTo(successCreatedCategory);
    }

    @Test
    public void addCategoryWithExistingNameShouldThrowAlreadyExistException() {
        when(categoryRepository.existsByName(categoryProfile.getName())).thenReturn(true);
        AlreadyExistException exception = assertThrows(AlreadyExistException.class, () -> categoryService.addCategory(categoryProfile));
        assertTrue(exception.getMessage().contains(categoryProfile.getName()));
    }

    @Test
    public void allCategoriesShouldBeSortedByValue() {
        when(categoryRepository.findInSortedOrder()).thenReturn(list);
        setUpMocksForConvertFromCategoryToCategoryResponse();
        assertThat(categoryService.getAllCategories()).isEqualTo(responseList);
    }

    @Test
    public void getAllCategoriesWhenThereAreNoCategoriesShouldReturnEmptyList() {
        when(categoryRepository.findInSortedOrder()).thenReturn(Collections.emptyList());
        assertThat(categoryService.getAllCategories()).hasSize(0);
    }

    @Test
    public void getListOfCategoriesShouldReturnCorrectPageable() {
        when(categoryRepository.findAll(pageRequest)).thenReturn(new PageImpl<>(list, pageRequest, list.size()));
        setUpMocksForConvertFromCategoryToCategoryResponse();
        Page<CategoryResponse> expectedPage = new PageImpl<>(responseList, pageRequest, responseList.size());
        assertThat(categoryService.getListOfCategories(pageRequest)).isEqualTo(expectedPage);
    }

    @Test
    public void getListOfCategoriesWhenThereAreNoCategoriesShouldReturnEmptyPage() {
        when(categoryRepository.findAll(pageRequest)).thenReturn(Page.empty(pageRequest));
        assertThat(categoryService.getListOfCategories(pageRequest)).hasSize(0);
    }

    @Test
    public void deleteCategoryByIdShouldSuccessfullyDeleteCategory() {
        CategoryResponse response = CategoryResponse.builder()
                .id(correctCategory.getId())
                .name(correctCategory.getName())
                .build();
        setMockReturnOptionalWhenFindByCorrectId();
        when(dtoConverter.convertToDto(correctCategory, CategoryResponse.class)).thenReturn(response);
        assertThat(categoryService.deleteCategoryById(CORRECT_ID)).isEqualTo(response);
    }

    @Test
    public void deleteCategoryByIdShouldThrowDatabaseRepositoryExceptionWhenAnErrorOccurred() {
        doThrow(new ValidationException()).doNothing().when(categoryRepository).deleteById(CORRECT_ID);
        setMockReturnOptionalWhenFindByCorrectId();
        assertThrows(DatabaseRepositoryException.class, () -> categoryService.deleteCategoryById(CORRECT_ID));
    }

    @Test
    public void getPossibleCategoryByNameShouldReturnListWithThreeElements() {
        when(categoryRepository.findRandomTop3ByName(CORRECT_NAME)).thenReturn(list);
        List<SearchPossibleResponse> possibleResponses = new LinkedList<>();
        for (int i = 0; i < list.size(); i++) {
            possibleResponses.add(
                    SearchPossibleResponse.builder()
                            .id(list.get(i).getId())
                            .name(list.get(i).getName()).build());
            when(dtoConverter.convertToDto(list.get(i), SearchPossibleResponse.class)).thenReturn(possibleResponses.get(i));
        }
        assertThat(categoryService.getPossibleCategoryByName(CORRECT_NAME)).isEqualTo(possibleResponses);
    }

    @Test
    public void getPossibleCategoryByNameShouldReturnEmptyListIfThereAreNoCategories() {
        when(categoryRepository.findRandomTop3ByName(CORRECT_NAME)).thenReturn(Collections.emptyList());
        assertThat(categoryService.getPossibleCategoryByName(CORRECT_NAME)).hasSize(0);
    }

    @Test
    public void updateCategoryShouldSuccessfullyUpdateCategoryAndSetCorrectId() {
        setMockReturnOptionalWhenFindByCorrectId();
        correctCategory.setId(WRONG_ID);
        when(dtoConverter.convertToEntity(categoryProfile, correctCategory)).thenReturn(correctCategory);
        CategoryProfile profileWithId = categoryProfile.withId(CORRECT_ID);
        when(dtoConverter.convertToDto(any(), any())).thenReturn(profileWithId);
        assertThat(categoryService.updateCategory(CORRECT_ID, categoryProfile)).isEqualTo(profileWithId);
    }

    private void setMockReturnOptionalWhenFindByCorrectId() {
        when(categoryRepository.findById(CORRECT_ID)).thenReturn(Optional.of(correctCategory));
    }

    private void setUpMocksForConvertFromCategoryToCategoryResponse() {
        for (int i = 0; i < list.size(); i++) {
            when(dtoConverter.convertToDto(list.get(i), CategoryResponse.class)).thenReturn(responseList.get(i));
        }
    }
}
