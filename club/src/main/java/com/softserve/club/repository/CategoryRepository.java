package com.softserve.club.repository;

import com.softserve.club.model.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findById(Long id);

    boolean existsByName(String name);

    List<Category> findAll();

    Page<Category> findAll(Pageable pageable);

    void deleteById(Long id);

    Optional<Category> findByName(String name);

    @Query("from Category c order by c.sortby")
    List<Category> findInSortedOrder();

    @Query("""
        FROM Category AS c
        WHERE LOWER(c.name) LIKE LOWER('%' || :text || '%')
        ORDER BY RANDOM() LIMIT 3
        """)
    List<Category> findRandomTop3ByName(@Param("text") String enteredText);
}
