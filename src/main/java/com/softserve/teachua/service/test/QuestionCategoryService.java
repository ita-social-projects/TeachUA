package com.softserve.teachua.service.test;

import com.softserve.teachua.model.test.QuestionCategory;

public interface QuestionCategoryService {
    QuestionCategory findByTitle(String title);
}
