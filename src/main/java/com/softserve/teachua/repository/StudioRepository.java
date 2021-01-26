package com.softserve.teachua.repository;

import com.softserve.teachua.model.Studio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudioRepository extends JpaRepository<Studio, Long> {
    Studio findByName(String name);

    Studio getById(Long id);

    boolean existsById(Long id);

    boolean existsByName(String name);
}
