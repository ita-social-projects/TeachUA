package com.softserve.teachua.repository.test;

import com.softserve.teachua.model.test.QuestionCategory;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Provides an interface for managing {@link QuestionCategory} model.
 */
@Repository
public interface QuestionCategoryRepository extends PagingAndSortingRepository<QuestionCategory, Long> {
    Optional<QuestionCategory> findByTitle(String title);

    Page<QuestionCategory> findByTitleContainingIgnoreCase(Pageable pageable, String title);

    Boolean existsByTitle(String title);

}
