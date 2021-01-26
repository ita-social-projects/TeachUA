package com.softserve.teachua.repository;

import com.softserve.teachua.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category getById(Long id);
    Category findByName(String name);
    List<Category> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
    boolean existsByName(String name);
}
