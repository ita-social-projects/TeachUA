package com.softserve.teachua.service.impl;

import com.softserve.teachua.dto.controller.CategoryResponse;
import com.softserve.teachua.dto.controller.SuccessCreatedCategory;
import com.softserve.teachua.dto.service.CategoryProfile;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Category;
import com.softserve.teachua.repository.CategoryRepository;
import com.softserve.teachua.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private static final String CATEGORY_ALREADY_EXIST = "category already exist with name: %s";
    private static final String CATEGORY_NOT_FOUND_BY_ID = "category not found by id: %s";
    private static final String CATEGORY_NOT_FOUND_NAME = "category not found by name: %s";

    private CategoryRepository categoryRepository;

    @Autowired
    CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;

    }

    @Override
    public CategoryResponse getCategoryProfileById(Long id) {
        Category category = getCategoryById(id);
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .urlLogo(category.getUrlLogo())
                .build();
    }

    @Override
    public Category getCategoryById(Long id) {
        if (!isCategoryExistById(id)) {
            String categoryNotFoundById = String.format(CATEGORY_NOT_FOUND_BY_ID, id);
            log.error(categoryNotFoundById);
            throw new NotExistException(categoryNotFoundById);
        }

        Category category = categoryRepository.getById(id);
        log.info("**/getting category by id = " + category);
        return category;
    }

    @Override
    public Category getCategoryByName(String name) {
        if (!isCategoryExistByName(name)) {
            String categoryNotFoundById = String.format(CATEGORY_NOT_FOUND_NAME, name);
            log.error(categoryNotFoundById);
            throw new NotExistException(categoryNotFoundById);
        }

        Category category = categoryRepository.findByName(name);
        log.info("**/getting category by name = " + category);
        return category;
    }

    @Override
    public SuccessCreatedCategory addCategory(CategoryProfile categoryProfile) {
        if (isCategoryExistByName(categoryProfile.getName())) {
            String categoryAlreadyExist = String.format(CATEGORY_ALREADY_EXIST, categoryProfile.getName());
            log.error(categoryAlreadyExist);
            throw new AlreadyExistException(categoryAlreadyExist);
        }

        Category category = categoryRepository.save(Category.builder()
                .name(categoryProfile.getName())
                .urlLogo(categoryProfile.getUrlLogo())
                .build());

        log.info("**/adding new category = " + category);
        return new SuccessCreatedCategory(category.getName());
    }

    @Override
    public List<CategoryResponse> getListOfCategories() {
        List<CategoryResponse> categoryResponses = categoryRepository.findAll()
                .stream()
                .map(category -> new CategoryResponse(category.getId(), category.getName(), category.getUrlLogo()))
                .collect(Collectors.toList());

        log.info("**/getting list of category = " + categoryResponses);
        return categoryResponses;
    }

    @Transactional
    @Override
    public CategoryResponse deleteCategoryById(Long id) {
        CategoryResponse categoryResponse = getCategoryProfileById(id);
        categoryRepository.deleteById(id);
        log.info("**/delete category = " + id);
        return categoryResponse;
    }

    private boolean isCategoryExistById(Long id) {
        return categoryRepository.existsById(id);
    }

    private boolean isCategoryExistByName(String name) {
        return categoryRepository.existsByName(name);
    }
}
