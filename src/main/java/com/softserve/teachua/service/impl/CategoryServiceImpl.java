package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.category.CategoryProfile;
import com.softserve.teachua.dto.category.CategoryResponse;
import com.softserve.teachua.dto.category.SuccessCreatedCategory;
import com.softserve.teachua.dto.search.SearchPossibleResponse;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.DatabaseRepositoryException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Category;
import com.softserve.teachua.repository.CategoryRepository;
import com.softserve.teachua.service.ArchiveService;
import com.softserve.teachua.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private static final String CATEGORY_ALREADY_EXIST = "Category already exists with name: %s";
    private static final String CATEGORY_NOT_FOUND_BY_ID = "Category not found by id: %s";
    private static final String CATEGORY_NOT_FOUND_BY_NAME = "Category not found by name: %s";
    private static final String CATEGORY_DELETING_ERROR = "Can't delete category cause of relationship";

    private final CategoryRepository categoryRepository;
    private final DtoConverter dtoConverter;
    private final ArchiveService archiveService;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               DtoConverter dtoConverter, ArchiveService archiveService) {
        this.categoryRepository = categoryRepository;
        this.dtoConverter = dtoConverter;
        this.archiveService = archiveService;
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

        log.debug("Getting category by id = {}", category);
        return category;
    }

    @Override
    public Category getCategoryByName(String name) {
        Optional<Category> optionalCategory = getOptionalCategoryByName(name);
        if (!optionalCategory.isPresent()) {
            throw new NotExistException(String.format(CATEGORY_NOT_FOUND_BY_NAME, name));
        }

        Category category = optionalCategory.get();

        log.debug("Getting category by name = {}", category);
        return category;
    }

    @Override
    public SuccessCreatedCategory addCategory(CategoryProfile categoryProfile) {
        if (isCategoryExistByName(categoryProfile.getName())) {
            throw new AlreadyExistException(String.format(CATEGORY_ALREADY_EXIST, categoryProfile.getName()));
        }
        Category category = categoryRepository.save(dtoConverter.convertToEntity(categoryProfile, new Category()));
        log.debug("Adding new category = {}", category);
        return dtoConverter.convertToDto(category, SuccessCreatedCategory.class);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        List<Category> categoryList = categoryRepository.findInSortedOrder();
        if (categoryList == null) {
            return Collections.emptyList();
        }
        List<CategoryResponse> categoryResponses =
                categoryList.stream()
                        .map(category -> (CategoryResponse) dtoConverter.convertToDto(category, CategoryResponse.class))
                        .collect(Collectors.toList());
        log.debug("Getting list of categories = {}", categoryResponses);
        return categoryResponses;
    }

    @Override
    public Page<CategoryResponse> getListOfCategories(Pageable pageable) {
        Page<Category> categoryResponses = categoryRepository.findAll(pageable);
        if (categoryResponses == null) {
            return Page.empty(pageable);
        }
        return new PageImpl<>(categoryResponses
                .stream()
                .map(category -> (CategoryResponse) dtoConverter.convertToDto(category, CategoryResponse.class))
                .collect(Collectors.toList()),
                categoryResponses.getPageable(), categoryResponses.getTotalElements());
    }

    @Override
    public CategoryResponse deleteCategoryById(Long id) {
        Category category = getCategoryById(id);

        archiveService.saveModel(category);

        try {
            categoryRepository.deleteById(id);
            categoryRepository.flush();
        } catch (DataAccessException | ValidationException e) {
            throw new DatabaseRepositoryException(CATEGORY_DELETING_ERROR);
        }

        log.debug("Category {} was successfully deleted", category);
        return dtoConverter.convertToDto(category, CategoryResponse.class);
    }

    @Override
    public List<SearchPossibleResponse> getPossibleCategoryByName(String text) {
        List<Category> categories = categoryRepository.findRandomTop3ByName(text);
        if (categories == null) {
            return Collections.emptyList();
        }
        return categories
                .stream()
                .map(category ->
                        (SearchPossibleResponse) dtoConverter.convertToDto(category, SearchPossibleResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryProfile updateCategory(Long id, CategoryProfile categoryProfile) {
        Category category = getCategoryById(id);
        Category newCategory = dtoConverter.convertToEntity(categoryProfile, category)
                .withId(id);

        log.debug("Updating category by id = {}", newCategory);
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
