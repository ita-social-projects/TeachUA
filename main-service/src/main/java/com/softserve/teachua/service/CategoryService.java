package com.softserve.teachua.service;

import com.softserve.teachua.dto.category.CategoryProfile;
import com.softserve.teachua.dto.category.CategoryResponse;
import com.softserve.teachua.dto.category.SuccessCreatedCategory;
import com.softserve.teachua.dto.search.SearchPossibleResponse;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.DatabaseRepositoryException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Category;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * This interface contains all needed methods to manage about us items using AboutUsItemRepository.
 */

public interface CategoryService {
    /**
     * The method returns dto {@code CategoryResponse} of category by id.
     *
     * @param id
     *            - put category id.
     *
     * @return new {@code CategoryResponse}.
     */
    CategoryResponse getCategoryProfileById(Long id);

    /**
     * The method returns entity {@code Category} of category by id.
     *
     * @param id
     *            - put category id.
     *
     * @return new {@code Category}.
     *
     * @throws NotExistException
     *             if category does not exist.
     */
    Category getCategoryById(Long id);

    /**
     * The method returns entity {@code Category} of category by name.
     *
     * @param name
     *            - put category name.
     *
     * @return new {@code Category}.
     *
     * @throws NotExistException
     *             if category does not exist.
     */
    Category getCategoryByName(String name);

    /**
     * The method returns dto {@code SuccessCreatedCategory} if category successfully added.
     *
     * @param categoryProfile
     *            - place body of dto {@code CategoryProfile}.
     *
     * @return new {@code SuccessCreatedCategory}.
     *
     * @throws AlreadyExistException
     *             if category already exists.
     */
    SuccessCreatedCategory addCategory(CategoryProfile categoryProfile);

    /**
     * The method returns list of dto {@code List<CategoryResponse>} of all categories.
     *
     * @return new {@code List<CategoryResponse>}.
     */
    List<CategoryResponse> getAllCategories();

    /**
     * The method returns page of dto {@code Page<CategoryResponse>} of all categories.
     *
     * @return new {@code Page<CategoryResponse>}.
     */
    Page<CategoryResponse> getListOfCategories(Pageable pageable);

    /**
     * The method returns dto {@code CategoryResponse} of deleted category by id.
     *
     * @param id
     *            - put category id.
     *
     * @return new {@code CategoryResponse}.
     *
     * @throws DatabaseRepositoryException
     *             if category contains foreign keys.
     */
    CategoryResponse deleteCategoryById(Long id);

    /**
     * The method returns list of dto {@code List<SearchPossibleResponse>} of 3 random categories by name.
     *
     * @return new {@code List<SearchPossibleResponse>}.
     */
    List<SearchPossibleResponse> getPossibleCategoryByName(String text);

    /**
     * The method returns dto {@code CategoryProfile} of updated category.
     *
     * @param categoryProfile
     *            - place body of dto {@code CategoryProfile}.
     *
     * @return new {@code CategoryProfile}.
     */
    CategoryProfile updateCategory(Long id, CategoryProfile categoryProfile);
}
