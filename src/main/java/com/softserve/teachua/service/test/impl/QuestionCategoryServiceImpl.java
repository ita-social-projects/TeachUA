package com.softserve.teachua.service.test.impl;

import com.softserve.teachua.model.test.QuestionCategory;
import com.softserve.teachua.repository.test.QuestionCategoryRepository;
import com.softserve.teachua.service.test.QuestionCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class QuestionCategoryServiceImpl implements QuestionCategoryService {
    private final QuestionCategoryRepository questionCategoryRepository;

    public QuestionCategory findByTitle(String title) {
        return questionCategoryRepository.findByTitle(title)
                .orElseThrow(() -> new NoSuchElementException(
                        String.format("There is no question category with title '%s'", title)
                ));
    }
}
