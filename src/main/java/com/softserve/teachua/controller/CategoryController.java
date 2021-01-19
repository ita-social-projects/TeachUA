package com.softserve.teachua.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.dto.controller.CategoryResponse;
import com.softserve.teachua.dto.controller.SuccessCreatedCategory;
import com.softserve.teachua.dto.service.CategoryProfile;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class CategoryController {

    /**
     * The controller returns information {@code CategoryResponse} about category.
     *
     * @param id - put category id.
     * @return new {@code CategoryResponse}.
     */
    @GetMapping("/category/{id}")
    public CategoryResponse getCategory(@PathVariable Long id) {
        return CategoryResponse.builder()
                .id(id)
                .name("My category")
                .build();
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
        return SuccessCreatedCategory.builder()
                .name(categoryProfile.getName())
                .build();
    }

    /**
     * The controller returns dto {@code ...} of deleted category.
     *
     * @param id - put category id.
     * @return new {@code ...}.
     */
    @DeleteMapping("/category")
    public Object deleteCategory(@RequestParam Long id) throws JsonProcessingException {
        return new ObjectMapper().readValue("{ \"id\" : "+ id +" }", Object.class);
    }
}
