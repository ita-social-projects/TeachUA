package com.softserve.teachua.service;

import com.softserve.teachua.dto.category.CategoryProfile;
import com.softserve.teachua.dto.category.SuccessCreatedCategory;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Category;
import com.softserve.teachua.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class CategoryServiceTest {
    @MockBean
    private CategoryRepository categoryRepository;

    private final CategoryService categoryService;

    @Autowired
    public CategoryServiceTest(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    private Category category1;
    private Category category2;

    private CategoryProfile category1Profile;
    private CategoryProfile category2Profile;

    private List<Category> categoryList;

    private static final String CATEGORY_1_NAME = "CATEGORY_1_NAME";
    private static final String CATEGORY_2_NAME = "CATEGORY_2_NAME";
    private static final String SOME_NOT_EXISTS_NAME = "SOME_NOT_EXISTS_NAME";

    private static final long CATEGORY_1_ID = 1L;
    private static final long CATEGORY_2_ID = 200L;
    private static final long SOME_NOT_EXISTS_CATEGORY_ID = 500L;

    @Test
    public void testGetCategoryById() {
        final long CATEGORY_1 = 1L;
        final long CATEGORY_2 = 200L;
        final long NOT_EXIST_CATEGORY = 140L;

        Category category1 = new Category();
        Category category2 = new Category();

        category1.setName("testGetCategoryById category1");
        category2.setName("testGetCategoryById category2");

        Mockito.when(categoryRepository.findById(CATEGORY_1))
                .thenReturn(Optional.of(category1));

        Mockito.when(categoryRepository.findById(CATEGORY_2))
                .thenReturn(Optional.of(category2));

        Mockito.when(categoryRepository.findById(NOT_EXIST_CATEGORY))
                .thenReturn(Optional.empty());

        assertThat(categoryService.getCategoryById(CATEGORY_1).getName()).isEqualTo(category1.getName());
        assertThat(categoryService.getCategoryById(CATEGORY_2).getName()).isEqualTo(category2.getName());

        assertThat(categoryService.getCategoryById(CATEGORY_1)).isEqualTo(category1);
        assertThat(categoryService.getCategoryById(CATEGORY_2)).isEqualTo(category2);

        assertThatThrownBy(() -> {
            categoryService.getCategoryById(NOT_EXIST_CATEGORY);
        }).isInstanceOf(NotExistException.class);
    }

    @BeforeEach
    void intializeAndMockMethods() {
        category1 = new Category();
        category2 = new Category();

        category1Profile = new CategoryProfile();
        category2Profile = new CategoryProfile();

        categoryList = new ArrayList<>();
        categoryList.add(category1);
        categoryList.add(category2);

        category1.setName(CATEGORY_1_NAME);
        category1.setId(CATEGORY_1_ID);

        category2.setName(CATEGORY_2_NAME);
        category2.setId(CATEGORY_2_ID);

        category1Profile.setName(CATEGORY_1_NAME);
        category1Profile.setId(CATEGORY_1_ID);

        category2Profile.setName(CATEGORY_2_NAME);
        category2Profile.setId(CATEGORY_2_ID);

        Mockito.when(categoryRepository.findById(CATEGORY_1_ID)).thenReturn(Optional.of(category1));
        Mockito.when(categoryRepository.findById(CATEGORY_2_ID)).thenReturn(Optional.of(category2));
        Mockito.when(categoryRepository.findById(Mockito.argThat(arg -> arg != CATEGORY_1_ID && arg != CATEGORY_2_ID))).thenReturn(Optional.empty());

        Mockito.when(categoryRepository.findByName(CATEGORY_1_NAME)).thenReturn(Optional.of(category1));
        Mockito.when(categoryRepository.findByName(CATEGORY_2_NAME)).thenReturn(Optional.of(category2));
        Mockito.when(categoryRepository.findByName(Mockito.argThat(arg -> !arg.equals(CATEGORY_1_NAME) && !arg.equals(CATEGORY_2_NAME)))).thenReturn(Optional.empty());

        Mockito.when(categoryRepository.existsByName(CATEGORY_1_NAME)).thenReturn(true);
        Mockito.when(categoryRepository.existsByName(CATEGORY_2_NAME)).thenReturn(true);
        Mockito.when(categoryRepository.existsByName(Mockito.argThat(arg -> !arg.equals(CATEGORY_1_NAME) && !arg.equals(CATEGORY_2_NAME)))).thenReturn(false);

        Mockito.when(categoryRepository.findAll()).thenReturn(categoryList);

        Mockito.when(categoryRepository.save(Mockito.any(Category.class)))
                .thenAnswer(invocation -> invocation.getArguments()[0]);

    }

    @Test
    public void testGetCategoryByName() {
        assertThat(categoryService.getCategoryByName(CATEGORY_1_NAME).getName()).isEqualTo(category1.getName());
        assertThat(categoryService.getCategoryByName(CATEGORY_2_NAME).getName()).isEqualTo(category2.getName());

        assertThat(categoryService.getCategoryByName(CATEGORY_1_NAME)).isEqualTo(category1);
        assertThat(categoryService.getCategoryByName(CATEGORY_2_NAME)).isEqualTo(category2);

        assertThatThrownBy(() -> {
            categoryService.getCategoryByName(SOME_NOT_EXISTS_NAME);
        }).isInstanceOf(NotExistException.class);
    }

    @Test
    public void testGetCategoryProfileById() {
        Mockito.when(categoryRepository.findById(CATEGORY_1_ID)).thenReturn(Optional.of(category1));

        Mockito.when(categoryRepository.findById(CATEGORY_2_ID)).thenReturn(Optional.of(category2));

        Mockito.when(categoryRepository.findById(SOME_NOT_EXISTS_CATEGORY_ID)).thenReturn(Optional.empty());

        assertThat(categoryService.getCategoryProfileById(CATEGORY_1_ID).getName()).isEqualTo(category1.getName());
        assertThat(categoryService.getCategoryProfileById(CATEGORY_2_ID).getName()).isEqualTo(category2.getName());

        assertThat(categoryService.getCategoryProfileById(CATEGORY_1_ID).getId()).isEqualTo(CATEGORY_1_ID);
        assertThat(categoryService.getCategoryProfileById(CATEGORY_2_ID).getId()).isEqualTo(CATEGORY_2_ID);

        assertThatThrownBy(() -> {
            categoryService.getCategoryById(SOME_NOT_EXISTS_CATEGORY_ID);
        }).isInstanceOf(NotExistException.class);
    }

    @Test
    public void testAddCategory() {
        CategoryProfile newCategoryProfile = new CategoryProfile();

        newCategoryProfile.setId(SOME_NOT_EXISTS_CATEGORY_ID);
        newCategoryProfile.setName(SOME_NOT_EXISTS_NAME);

        SuccessCreatedCategory successCreatedCategory = categoryService.addCategory(newCategoryProfile);

        assertThat(successCreatedCategory.getName()).isEqualTo(SOME_NOT_EXISTS_NAME);

        assertThatThrownBy(() -> {
            categoryService.addCategory(category1Profile);
        }).isInstanceOf(AlreadyExistException.class);

        assertThatThrownBy(() -> {
            categoryService.addCategory(category2Profile);
        }).isInstanceOf(AlreadyExistException.class);
    }

    @Test
    public void testGetListOfCategories() {
        Set<String> expectedCategoriesNames = categoryList.stream()
                .map(c -> c.getName()).collect(Collectors.toSet());

        Set<String> actualCategoriesNames = categoryService.getAllCategories().stream()
                .map(c -> c.getName()).collect(Collectors.toSet());

        assertThat(categoryService.getAllCategories().size()).isEqualTo(2);

        assertThat(actualCategoriesNames).isEqualTo(expectedCategoriesNames);
    }

    @Test
    public void testDeleteCategoryById() {
       /* final long CATEGORY_1 = 1L;
        final long CATEGORY_2 = 200L;
        final long NOT_EXISTS_CATEGORY = 10000L;

        String category1Name = "testDeleteCategoryById Name1";
        String category2Name = "testDeleteCategoryById Name2";

        Category category1 = new Category();
        Category category2 = new Category();

        category1.setName(category1Name);
        category2.setName(category2Name);

        category1.setId(CATEGORY_1);
        category2.setId(CATEGORY_2);*/
    /*Mockito.when(categoryRepository.findById(CATEGORY_1_ID)).thenReturn(Optional.of(category1));
        Mockito.when(categoryRepository.findById(CATEGORY_2)).thenReturn(Optional.of(category2));
        Mockito.when(categoryRepository.findById(NOT_EXISTS_CATEGORY)).thenReturn(Optional.empty());*/

        Mockito.doNothing().when(categoryRepository).deleteById(CATEGORY_1_ID);
        Mockito.doNothing().when(categoryRepository).deleteById(CATEGORY_2_ID);
        Mockito.doNothing().when(categoryRepository).deleteById(SOME_NOT_EXISTS_CATEGORY_ID);

        assertThat(categoryService.deleteCategoryById(CATEGORY_1_ID).getName()).isEqualTo(CATEGORY_1_NAME);
        assertThat(categoryService.deleteCategoryById(CATEGORY_2_ID).getName()).isEqualTo(CATEGORY_2_NAME);

        assertThatThrownBy(() -> {
            categoryService.deleteCategoryById(SOME_NOT_EXISTS_CATEGORY_ID);
        }).isInstanceOf(NotExistException.class);
    }

    @Test
    public void testGetPossibleCategoryByName() {
        String keyword = "myKeyword";
        String categoryWithKeywordName = "testGetPossibleCategoryByName Name2" + keyword + " some text";

        Category categoryWithKeyword = new Category();
        categoryWithKeyword.setName(categoryWithKeywordName);

        List<Category> categories = new ArrayList<>();
        categories.add(categoryWithKeyword);

        Mockito.when(categoryRepository.findRandomTop3ByName(keyword))
                .thenReturn(categories);

        Set<String> expectedCategoriesNames = categories.stream()
                .map(c -> c.getName()).collect(Collectors.toSet());

        Set<String> actualCategoriesNames = categoryService.getPossibleCategoryByName(keyword).stream()
                .map(c -> c.getName()).collect(Collectors.toSet());

        assertThat(categoryService.getPossibleCategoryByName(keyword).size()).isEqualTo(1);

        assertThat(actualCategoriesNames).isEqualTo(expectedCategoriesNames);
    }

    @Test
    public void testUpdateCategory() {
        Mockito.when(categoryRepository.save(Mockito.any(Category.class)))
                .thenAnswer(invocation -> invocation.getArguments()[0]);

        CategoryProfile newCategoryProfile = new CategoryProfile();
        String newCategoryName = "testUpdateCategory NewName";
        newCategoryProfile.setName(newCategoryName);
        newCategoryProfile.setId(CATEGORY_1_ID);

        assertThat(categoryService.updateCategory(CATEGORY_1_ID, newCategoryProfile).getId()).isEqualTo(CATEGORY_1_ID);
        assertThat(categoryService.updateCategory(CATEGORY_1_ID, newCategoryProfile).getName()).isEqualTo(newCategoryName);

        assertThatThrownBy(() -> {
            categoryService.updateCategory(SOME_NOT_EXISTS_CATEGORY_ID, newCategoryProfile);
        }).isInstanceOf(NotExistException.class);
    }

    /*
    To Test:
        + CategoryResponse getCategoryProfileById(Long id);
        + Category getCategoryById(Long id);
        + Category getCategoryByName(String name);
        - SuccessCreatedCategory addCategory(CategoryProfile categoryProfile);
        + List<CategoryResponse> getListOfCategories();
        + CategoryResponse deleteCategoryById(Long id);
        + List<SearchPossibleResponse> getPossibleCategoryByName(String text);
        + CategoryProfile updateCategory(Long id, CategoryProfile categoryProfile);
     */
}
