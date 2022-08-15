package com.softserve.teachua.controller.test;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.test.questionCategory.QuestionCategoryProfile;
import com.softserve.teachua.service.test.QuestionCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


/**
 * This controller is for managing question categories.
 * */

@RequiredArgsConstructor
@RestController
public class QuestionCategoryController implements Api {
    private final QuestionCategoryService questionCategoryService;

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
     * @param categoryProfile - put information about question category here.
     */
    @ResponseStatus(value = CREATED)
    @PostMapping(path = "/question_categories", consumes = APPLICATION_JSON_VALUE)
    public void createQuestionCategory(@RequestBody QuestionCategoryProfile categoryProfile) {
        questionCategoryService.save(categoryProfile);
    }

    /**
     * Use this endpoint to update the question category.
     * @param categoryProfile - put information about question category here.
     * @param id - put question category id here.
     */
    @ResponseStatus(value = NO_CONTENT)
    @PutMapping(path = "/question_categories/{id}", consumes = APPLICATION_JSON_VALUE)
    public QuestionCategoryProfile updateQuestionCategory(@RequestBody QuestionCategoryProfile categoryProfile,
                                       @PathVariable Long id) {
        return questionCategoryService.updateById(categoryProfile, id);
    }
}
