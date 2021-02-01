package com.softserve.teachua.repository;

import com.softserve.teachua.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    News getById(Long id);
    boolean existsById(Long id);
}
