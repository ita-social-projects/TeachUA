package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.DtoConverter;
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
    private static final String CATEGORY_ALREADY_EXIST = "Category already exist with name: %s";
    private static final String CATEGORY_NOT_FOUND_BY_ID = "Category not found by id: %s";
    private static final String CATEGORY_NOT_FOUND_NAME = "Category not found by name: %s";

    private final CategoryRepository categoryRepository;
    private final DtoConverter dtoConverter;

    @Autowired
    CategoryServiceImpl(CategoryRepository categoryRepository, DtoConverter dtoConverter) {
        this.categoryRepository = categoryRepository;
        this.dtoConverter = dtoConverter;

    }

    @Override
    public CategoryResponse getCategoryProfileById(Long id) {
        return dtoConverter.convertToDto(getCategoryById(id), CategoryResponse.class);
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

        Category category = categoryRepository.save(dtoConverter.convertToEntity(categoryProfile,Category.builder().build()));
        log.info("**/adding new category = " + category);
        return dtoConverter.convertToDto(category, SuccessCreatedCategory.class);
    }

    @Override
    public List<CategoryResponse> getListOfCategories() {
        List<CategoryResponse> categoryResponses = categoryRepository.findAll()
                .stream()
                .map(category -> (CategoryResponse) dtoConverter.convertToDto(category, CategoryResponse.class))
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
