package com.softserve.teachua.repository;

import com.softserve.teachua.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Category findById(Long id);

    List<Category> findAll();

    void deleteById(Long id);
}
