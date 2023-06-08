package com.softserve.teachua.controller.test;

import com.softserve.commons.constant.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.test.question_category.QuestionCategoryProfile;
import com.softserve.teachua.dto.test.question_category.QuestionCategoryResponse;
import com.softserve.teachua.service.test.QuestionCategoryService;
import java.util.List;
import jakarta.validation.Valid;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import lombok.RequiredArgsConstructor;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is for managing question categories.
 * */

@RequiredArgsConstructor
@RestController
public class QuestionCategoryController implements Api {
    private final QuestionCategoryService questionCategoryService;

    /**
     * Use this endpoint to get question categories pageable and search by title.
     * @param pageable pagination configuration
     * @param query title search query
     * @return {@code Page<QuestionCategoryResponse>}
     */
    @AllowedRoles(RoleData.ADMIN)
    @GetMapping("/questions/categories/search")
    public Page<QuestionCategoryResponse> searchCategoriesPageable(
            @SortDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam String query
    ) {
        return questionCategoryService.searchAllQuestionCategoriesPageable(pageable, query);
    }

    /**
     * Use this endpoint to get all question categories.
     * The controller returns a list of topic DTOs {@code List<QuestionCategoryProfile>}.
     *
     * @return new {@code List<QuestionCategoryProfile>}.
     */
    @GetMapping(path = "/question_categories", produces = APPLICATION_JSON_VALUE)
    public List<QuestionCategoryProfile> getQuestionCategories() {
        return questionCategoryService.findAllCategoryProfiles();
    }

    /**
     * Use this endpoint to create the question category.
     *
     * @param categoryProfile - put information about question category here.
     */
    @AllowedRoles(RoleData.ADMIN)
    @ResponseStatus(value = CREATED)
    @PostMapping(path = "/question_categories", consumes = APPLICATION_JSON_VALUE)
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
    @ResponseStatus(value = NO_CONTENT)
    @PutMapping(path = "/question_categories/{id}", consumes = APPLICATION_JSON_VALUE)
    public QuestionCategoryProfile updateQuestionCategory(@Valid @RequestBody QuestionCategoryProfile categoryProfile,
                                                          @PathVariable Long id) {
        return questionCategoryService.updateById(categoryProfile, id);
    }

    /**
     * Use this endpoint to delete the question category.
     * @param id category id
     */
    @AllowedRoles(RoleData.ADMIN)
    @ResponseStatus(value = NO_CONTENT)
    @DeleteMapping(path = "/question_categories/{id}")
    public void deleteQuestionCategory(@PathVariable Long id) {
        questionCategoryService.deleteById(id);
    }
}
