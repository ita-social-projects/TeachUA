package com.softserve.teachua.service;


import com.softserve.teachua.dto.controller.CategoryResponse;
import com.softserve.teachua.dto.controller.SuccessCreatedCategory;
import com.softserve.teachua.dto.service.CategoryProfile;
import com.softserve.teachua.model.Category;


import java.util.List;

public interface CategoryService {

    CategoryResponse getCategoryProfileById(Long id);

    Category getCategoryById(Long id);

    Category getCategoryByName(String name);

    SuccessCreatedCategory addCategory(CategoryProfile categoryProfile);

    List<CategoryResponse> getListOfCategories();

    CategoryResponse deleteCategoryById(Long id);
}
