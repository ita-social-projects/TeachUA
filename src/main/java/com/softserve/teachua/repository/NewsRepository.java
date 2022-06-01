package com.softserve.teachua.repository;

import com.softserve.teachua.model.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Provides an interface to manage {@link News} model.
 */
@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    Optional<News> findById(Long id);

    @Query("SELECT distinct n FROM News AS n WHERE n.isActive = true AND  n.date <= CURRENT_DATE ORDER BY n.date desc, n.id desc")
    Page<News> findAll(Pageable pageable);
}
