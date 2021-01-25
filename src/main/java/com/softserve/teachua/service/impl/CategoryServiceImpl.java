package com.softserve.teachua.service.impl;

import com.softserve.teachua.dto.controller.CategoryResponse;
import com.softserve.teachua.dto.controller.SuccessCreatedCategory;
import com.softserve.teachua.dto.service.CategoryProfile;
import com.softserve.teachua.model.Category;
import com.softserve.teachua.repository.CategoryRepository;
import com.softserve.teachua.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@Transactional
public class CategoryServiceImpl implements CategoryService {

    CategoryRepository categoryRepository;

    @Autowired
    CategoryServiceImpl(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;

    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id);
        return  CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .urlLogo(category.getUrlLogo())
                .build();
    }

    @Override
    public List<CategoryResponse> getListOfCategories() {
        List<CategoryResponse> allCategories = new ArrayList<>();
        for( Category category: categoryRepository.findAll()){
            allCategories.add(CategoryResponse.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .urlLogo(category.getUrlLogo())
                    .build());
        }
        return allCategories;
    }

    @Override
    public SuccessCreatedCategory addCategory(CategoryProfile categoryProfile) {

        Category category = categoryRepository.save(Category.builder()
                .name(categoryProfile.getName())
                .urlLogo(categoryProfile.getUrlLogo())
                .build());

        log.info("User is save");
        return new SuccessCreatedCategory(category.getName());
    }

    @Transactional
    @Override
    public ResponseEntity<CategoryProfile> deleteCategoryById(Long id) {
        categoryRepository.deleteById(id);
        return null;
    }

}
