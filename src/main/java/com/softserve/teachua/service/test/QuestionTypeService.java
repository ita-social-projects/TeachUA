package com.softserve.teachua.service.test;

import com.softserve.teachua.model.test.QuestionType;

public interface QuestionTypeService {
    QuestionType findByTitle(String title);
}
