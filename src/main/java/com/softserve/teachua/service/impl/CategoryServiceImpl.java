package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.category.CategoryProfile;
import com.softserve.teachua.dto.category.CategoryResponse;
import com.softserve.teachua.dto.category.SuccessCreatedCategory;
import com.softserve.teachua.dto.club.ClubResponse;
import com.softserve.teachua.dto.search.SearchPossibleResponse;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.DatabaseRepositoryException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Category;
import com.softserve.teachua.model.Club;
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
    public CategoryServiceImpl(CategoryRepository categoryRepository, DtoConverter dtoConverter, ArchiveService archiveService) {
        this.categoryRepository = categoryRepository;
        this.dtoConverter = dtoConverter;
        this.archiveService = archiveService;
    }

    /**
     * The method returns dto {@code CategoryResponse} of category by id.
     *
     * @param id - put category id.
     * @return new {@code CategoryResponse}.
     */
    @Override
    public CategoryResponse getCategoryProfileById(Long id) {
        return dtoConverter.convertToDto(getCategoryById(id), CategoryResponse.class);
    }

    /**
     * The method returns entity {@code Category} of category by id.
     *
     * @param id - put category id.
     * @return new {@code Category}.
     * @throws NotExistException if category does not exist.
     */
    @Override
    public Category getCategoryById(Long id) {
        Optional<Category> optionalCategory = getOptionalCategoryById(id);
        if (!optionalCategory.isPresent()) {
            throw new NotExistException(String.format(CATEGORY_NOT_FOUND_BY_ID, id));
        }

        Category category = optionalCategory.get();

        log.info("Getting category by id = {}", category);
        return category;
    }

    /**
     * The method returns entity {@code Category} of category by name.
     *
     * @param name - put category name.
     * @return new {@code Category}.
     * @throws NotExistException if category does not exist.
     */
    @Override
    public Category getCategoryByName(String name) {
        Optional<Category> optionalCategory = getOptionalCategoryByName(name);
        if (!optionalCategory.isPresent()) {
            throw new NotExistException(String.format(CATEGORY_NOT_FOUND_BY_NAME, name));
        }

        Category category = optionalCategory.get();

        log.info("Getting category by name = {}", category);
        return category;
    }

    /**
     * The method returns dto {@code SuccessCreatedCategory} if category successfully added.
     *
     * @param categoryProfile - place body of dto {@code CategoryProfile}.
     * @return new {@code SuccessCreatedCategory}.
     * @throws AlreadyExistException if category already exists.
     */
    @Override
    public SuccessCreatedCategory addCategory(CategoryProfile categoryProfile) {
        if (isCategoryExistByName(categoryProfile.getName())) {
            throw new AlreadyExistException(String.format(CATEGORY_ALREADY_EXIST, categoryProfile.getName()));
        }

        Category category = categoryRepository.save(dtoConverter.convertToEntity(categoryProfile, new Category()));
        log.info("Adding new category = {}", category);
        return dtoConverter.convertToDto(category, SuccessCreatedCategory.class);
    }

    /**
     * The method returns list of dto {@code List<CategoryResponse>} of all categories.
     *
     * @return new {@code List<CategoryResponse>}.
     */
    @Override
    public List<CategoryResponse> getAllCategories() {
        List<CategoryResponse> categoryResponses = categoryRepository.findAll()
                .stream()
                .map(category -> (CategoryResponse) dtoConverter.convertToDto(category, CategoryResponse.class))
                .collect(Collectors.toList());

        log.info("Getting list of categories = {}", categoryResponses);
        return categoryResponses;
    }

    /**
     * The method returns page of dto {@code List<CategoryResponse>} of all categories.
     *
     * @return new {@code List<CategoryResponse>}.
     */

    @Override
    public Page<CategoryResponse> getListOfCategories(Pageable pageable) {
        Page<Category> categoryResponses = categoryRepository.findAll(pageable);
        return new PageImpl<>(categoryResponses
                .stream()
                .map(category -> (CategoryResponse) dtoConverter.convertToDto(category, CategoryResponse.class))
                .collect(Collectors.toList()),
                categoryResponses.getPageable(), categoryResponses.getTotalElements());
    }

    /**
     * The method returns dto {@code CategoryResponse} of deleted category by id.
     *
     * @param id - put category id.
     * @return new {@code CategoryResponse}.
     * @throws DatabaseRepositoryException if category contains foreign keys.
     */
    @Override
    public CategoryResponse deleteCategoryById(Long id) {
        Category category = getCategoryById(id);

        archiveService.saveModel(category);

        try {
            categoryRepository.deleteById(id);
        } catch (DataAccessException | ValidationException e) {
            throw new DatabaseRepositoryException(CATEGORY_DELETING_ERROR);
        }

        log.info("Category {} was successfully deleted", category);
        return dtoConverter.convertToDto(category, CategoryResponse.class);
    }

    /**
     * The method returns list of dto {@code List<SearchPossibleResponse>} of 3 random categories by name.
     *
     * @return new {@code List<SearchPossibleResponse>}.
     */
    @Override
    public List<SearchPossibleResponse> getPossibleCategoryByName(String text) {
        return categoryRepository.findRandomTop3ByName(text)
                .stream()
                .map(category -> (SearchPossibleResponse) dtoConverter.convertToDto(category, SearchPossibleResponse.class))
                .collect(Collectors.toList());
    }

    /**
     * The method returns dto {@code CategoryProfile} of updated category.
     *
     * @param categoryProfile - place body of dto {@code CategoryProfile}.
     * @return new {@code CategoryProfile}.
     */
    @Override
    public CategoryProfile updateCategory(Long id, CategoryProfile categoryProfile) {
        Category category = getCategoryById(id);
        Category newCategory = dtoConverter.convertToEntity(categoryProfile, category)
                .withId(id);

        log.info("Updating category by id = {}", newCategory);
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
