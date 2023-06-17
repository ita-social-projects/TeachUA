package com.softserve.question.controller;

import com.softserve.commons.constant.RoleData;
import com.softserve.question.controller.marker.Api;
import com.softserve.question.dto.question_category.QuestionCategoryProfile;
import com.softserve.question.dto.question_category.QuestionCategoryResponse;
import com.softserve.question.model.QuestionCategory;
import com.softserve.question.service.QuestionCategoryService;
import com.softserve.question.util.annotation.AllowedRoles;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is for managing question categories.
 */
@RestController
@RequestMapping("/api/v1/question-category")
public class QuestionCategoryController implements Api {
    private final QuestionCategoryService questionCategoryService;

    public QuestionCategoryController(QuestionCategoryService questionCategoryService) {
        this.questionCategoryService = questionCategoryService;
    }

    /**
     * Use this endpoint to get question categories pageable and search by title.
     * @param pageable pagination configuration
     * @param query title search query
     * @return {@code Page<QuestionCategoryResponse>}
     */
    @AllowedRoles(RoleData.ADMIN)
    @GetMapping("/all/search")
    public Page<QuestionCategoryResponse> searchCategoriesPageable(
            @SortDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam String query
    ) {
        return questionCategoryService.searchAllQuestionCategoriesPageable(pageable, query);
    }

    @AllowedRoles(RoleData.ADMIN)
    @GetMapping("/all")
    public List<QuestionCategory> getQuestionCategories() {
        return questionCategoryService.findAll();
    }

    /**
     * Use this endpoint to create the question category.
     *
     * @param categoryProfile - put information about question category here.
     */
    @AllowedRoles(RoleData.ADMIN)
    @PostMapping
    public void createQuestionCategory(@Valid @RequestBody QuestionCategoryProfile categoryProfile) {
        questionCategoryService.save(categoryProfile);
    }

    /**
     * Use this endpoint to update the question category.
     *
     * @param categoryProfile - put information about question category here.
     * @param id              - put question category id here.
     */
    @AllowedRoles(RoleData.ADMIN)
    @PutMapping("/{id}")
    public QuestionCategoryProfile updateQuestionCategory(@Valid @RequestBody QuestionCategoryProfile categoryProfile,
                                                          @PathVariable Long id) {
        return questionCategoryService.updateById(categoryProfile, id);
    }

    /**
     * Use this endpoint to delete the question category.
     * @param id category id
     */
    @AllowedRoles(RoleData.ADMIN)
    @DeleteMapping("/{id}")
    public void deleteQuestionCategory(@PathVariable Long id) {
        questionCategoryService.deleteById(id);
    }
}
