package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.category.CategoryProfile;
import com.softserve.teachua.dto.category.CategoryResponse;
import com.softserve.teachua.dto.category.SuccessCreatedCategory;
import com.softserve.teachua.dto.search.SearchPossibleResponse;
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
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private static final String CATEGORY_ALREADY_EXIST = "Category already exist with name: %s";
    private static final String CATEGORY_NOT_FOUND_BY_ID = "Category not found by id: %s";
    private static final String CATEGORY_NOT_FOUND_BY_NAME = "Category not found by name: %s";

    private final CategoryRepository categoryRepository;
    private final DtoConverter dtoConverter;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, DtoConverter dtoConverter) {
        this.categoryRepository = categoryRepository;
        this.dtoConverter = dtoConverter;

    }

    @Override
    public CategoryResponse getCategoryProfileById(Long id) {
        return dtoConverter.convertToDto(getCategoryById(id), CategoryResponse.class);
    }

    @Override
    public Category getCategoryById(Long id) {
        Optional<Category> optionalCategory = getOptionalCategoryById(id);
        if (!optionalCategory.isPresent()) {
            throw new NotExistException(String.format(CATEGORY_NOT_FOUND_BY_ID, id));
        }

        Category category = optionalCategory.get();

        log.info("**/getting category by id = " + category);
        return category;
    }

    @Override
    public Category getCategoryByName(String name) {
        Optional<Category> optionalCategory = getOptionalCategoryByName(name);
        if (!optionalCategory.isPresent()) {
            throw new NotExistException(String.format(CATEGORY_NOT_FOUND_BY_NAME, name));
        }

        Category category = optionalCategory.get();

        log.info("**/getting category by name = " + category);
        return category;
    }

    @Override
    public SuccessCreatedCategory addCategory(CategoryProfile categoryProfile) {
        if (isCategoryExistByName(categoryProfile.getName())) {
            throw new AlreadyExistException(String.format(CATEGORY_ALREADY_EXIST, categoryProfile.getName()));
        }

        Category category = categoryRepository.save(dtoConverter.convertToEntity(categoryProfile, new Category()));
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

    @Override
    public List<SearchPossibleResponse> getPossibleCategoryByName(String text) {
        return categoryRepository.findRandomTop3ByName(text)
                .stream()
                .map(category -> (SearchPossibleResponse) dtoConverter.convertToDto(category, SearchPossibleResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryProfile updateCategory(Long id, CategoryProfile categoryProfile) {
        Category category = getCategoryById(id);
        Category newCategory = dtoConverter.convertToEntity(categoryProfile, category)
                .withId(id);

        log.info("**/updating category by id = " + newCategory);
        return dtoConverter.convertToDto(categoryRepository.save(newCategory), CategoryProfile.class);
    }

    private boolean isCategoryExistByName(String name) {
        return categoryRepository.existsByName(name);
    }

    private Optional<Category> getOptionalCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    private Optional<Category> getOptionalCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }
}
