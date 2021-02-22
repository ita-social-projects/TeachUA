package com.softserve.teachua.service;


import com.softserve.teachua.dto.category.CategoryProfile;
import com.softserve.teachua.dto.category.CategoryResponse;
import com.softserve.teachua.dto.category.SuccessCreatedCategory;
import com.softserve.teachua.dto.search.SearchPossibleResponse;
import com.softserve.teachua.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {

    CategoryResponse getCategoryProfileById(Long id);

    Category getCategoryById(Long id);

    Category getCategoryByName(String name);

    SuccessCreatedCategory addCategory(CategoryProfile categoryProfile);

    List<CategoryResponse> getAllCategories();

    Page<CategoryResponse> getListOfCategories(Pageable pageable);

    CategoryResponse deleteCategoryById(Long id);

    List<SearchPossibleResponse> getPossibleCategoryByName(String text);

    CategoryProfile updateCategory(Long id, CategoryProfile categoryProfile);
}
