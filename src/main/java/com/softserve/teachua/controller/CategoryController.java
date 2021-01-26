package com.softserve.teachua.controller;

import com.softserve.teachua.dto.controller.CategoryResponse;
import com.softserve.teachua.dto.controller.SuccessCreatedCategory;
import com.softserve.teachua.dto.service.CategoryProfile;
import com.softserve.teachua.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    /**
     * The controller returns dto {@code CategoryResponse} about category.
     *
     * @param id - put category id.
     * @return new {@code CategoryResponse}.
     */
    @GetMapping("/category/{id}")
    public CategoryResponse getCategory(@PathVariable Long id) {
        return categoryService.getCategoryProfileById(id);
    }

    @GetMapping("/categories")
    public List<CategoryResponse> getAllCategories(){
        return categoryService.getListOfCategories();
    }


    /**
     * The controller returns dto {@code SuccessCreatedCategory} of created category.
     *
     * @param categoryProfile - Place dto with all parameters for adding new category.
     * @return new {@code SuccessCreatedCategory}.
     */
    @PostMapping("/category")
    public SuccessCreatedCategory addCategory(
            @Valid
            @RequestBody CategoryProfile categoryProfile) {
        return categoryService.addCategory(categoryProfile);
    }

    /**
     * The controller returns dto {@code ...} of deleted category.
     *
     * @param id - put category id.
     * @return new {@code ...}.
     */
    @DeleteMapping("/category/{id}")
    public ResponseEntity<CategoryResponse> deleteCategory(@PathVariable("id") Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categoryService.deleteCategoryById(id));
    }
}
