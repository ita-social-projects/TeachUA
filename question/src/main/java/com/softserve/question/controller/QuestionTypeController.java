package com.softserve.question.controller;

import com.softserve.commons.constant.RoleData;
import com.softserve.question.controller.marker.Api;
import com.softserve.question.dto.question_type.QuestionTypeProfile;
import com.softserve.question.dto.question_type.QuestionTypeResponse;
import com.softserve.question.model.QuestionType;
import com.softserve.question.service.QuestionTypeService;
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
 * This controller is for managing question types.
 */
@RestController
@RequestMapping("/api/v1/question-type")
public class QuestionTypeController implements Api {
    private final QuestionTypeService questionTypeService;

    public QuestionTypeController(QuestionTypeService questionTypeService) {
        this.questionTypeService = questionTypeService;
    }

    @AllowedRoles(RoleData.ADMIN)
    @GetMapping
    public List<QuestionType> getQuestionTypes() {
        return questionTypeService.findAll();
    }

    /**
     * Use this endpoint to get question types pageable and search by title.
     *
     * @param pageable pagination configuration
     * @param query    title search query
     * @return {@code Page<QuestionTypeResponse>}
     */
    @AllowedRoles(RoleData.ADMIN)
    @GetMapping("/search")
    public Page<QuestionTypeResponse> searchTypesPageable(
            @SortDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam String query
    ) {
        return questionTypeService.searchAllQuestionCategoriesPageable(pageable, query);
    }

    /**
     * Use this endpoint to create the question type.
     *
     * @param typeProfile - put information about question type here.
     */
    @AllowedRoles(RoleData.ADMIN)
    @PostMapping
    public void createQuestionType(@Valid @RequestBody QuestionTypeProfile typeProfile) {
        questionTypeService.save(typeProfile);
    }

    /**
     * Use this endpoint to update the question type.
     *
     * @param typeProfile - put information about question type here.
     * @param id          - put question type id here.
     */
    @AllowedRoles(RoleData.ADMIN)
    @PutMapping("/{id}")
    public QuestionTypeProfile updateQuestionCategory(@Valid @RequestBody QuestionTypeProfile typeProfile,
                                                      @PathVariable Long id) {
        return questionTypeService.updateById(typeProfile, id);
    }

    /**
     * Use this endpoint to delete the question type.
     *
     * @param id type id
     */
    @AllowedRoles(RoleData.ADMIN)
    @DeleteMapping("/{id}")
    public void deleteQuestionCategory(@PathVariable Long id) {
        questionTypeService.deleteById(id);
    }
}
