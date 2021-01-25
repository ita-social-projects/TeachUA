package com.softserve.teachua.service;


import com.softserve.teachua.dto.controller.CategoryResponse;
import com.softserve.teachua.dto.controller.SuccessCreatedCategory;
import com.softserve.teachua.dto.service.CategoryProfile;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface CategoryService {

    CategoryResponse getCategoryById(Long id);

    List<CategoryResponse> getListOfCategories();

    SuccessCreatedCategory addCategory(CategoryProfile categoryProfile);

    ResponseEntity<CategoryProfile> deleteCategoryById(Long id);
}
