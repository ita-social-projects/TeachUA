package com.softserve.teachua.controller;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.category.CategoryProfile;
import com.softserve.teachua.dto.category.CategoryResponse;
import com.softserve.teachua.dto.category.SuccessCreatedCategory;
import com.softserve.teachua.service.CategoryService;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is for managing the categories.
 */

@RestController
@Slf4j
@Tag(name = "category", description = "the Category API")
@SecurityRequirement(name = "api")
public class CategoryController implements Api {
    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Use this endpoint to get information on a specific category. The controller returns {@code CategoryResponse}.
     *
     * @param id
     *            - put category id.
     *
     * @return {@code CategoryResponse}.
     */
    @GetMapping("/category/{id}")
    public CategoryResponse getCategory(@PathVariable Long id) {
        return categoryService.getCategoryProfileById(id);
    }

    /**
     * Use this endpoint to get information on all categories. The controller returns {@code List<CategoryResponse>}.
     *
     * @return {@code CategoryResponse}.
     */
    @GetMapping("/categories")
    public List<CategoryResponse> getAllCategories() {
        return categoryService.getAllCategories();
    }

    /**
     * Use this endpoint to get result of the categories search with pagination. The controller returns
     * {@code Page<CategoryResponse>}.
     *
     * @param pageable
     *            - put pageable parameters as request parameters.
     *
     * @return {@code Page<CategoryResponse>} - all Categories with pagination.
     */
    @GetMapping("/categories/search")
    public Page<CategoryResponse> getListOfCategories(@PageableDefault(value = 4, sort = "id") Pageable pageable) {
        return categoryService.getListOfCategories(pageable);
    }

    /**
     * Use this endpoint to create a category. The controller returns {@code SuccessCreatedCategory}.
     *
     * @param categoryProfile
     *            - Place dto with all parameters for adding new category.
     *
     * @return new {@code SuccessCreatedCategory}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @PostMapping("/category")
    public SuccessCreatedCategory addCategory(@Valid @RequestBody CategoryProfile categoryProfile) {
        return categoryService.addCategory(categoryProfile);
    }

    /**
     * Use this endpoint to update the category by id. The controller returns {@code  CategoryProfile}.
     *
     * @param id
     *            - put Category id here.
     * @param categoryProfile
     *            - put dto with all parameters here.
     *
     * @return {@code CategoryProfile}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @PutMapping("/category/{id}")
    public CategoryProfile updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryProfile categoryProfile) {
        return categoryService.updateCategory(id, categoryProfile);
    }

    /**
     * Use this endpoint to archive a category by id. The controller returns {@code CategoryResponse}.
     *
     * @param id
     *            - put category id.
     *
     * @return {@code CategoryResponse}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @DeleteMapping("/category/{id}")
    public CategoryResponse deleteCategory(@PathVariable("id") Long id) {
        return categoryService.deleteCategoryById(id);
    }
}
