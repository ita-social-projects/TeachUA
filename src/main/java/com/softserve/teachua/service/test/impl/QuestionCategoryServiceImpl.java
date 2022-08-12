package com.softserve.teachua.service.test.impl;

import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.test.QuestionCategory;
import com.softserve.teachua.repository.test.QuestionCategoryRepository;
import com.softserve.teachua.service.test.QuestionCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static com.softserve.teachua.utils.test.NullValidator.*;
import static com.softserve.teachua.utils.test.Messages.*;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class QuestionCategoryServiceImpl implements QuestionCategoryService {
    private final QuestionCategoryRepository questionCategoryRepository;

    @Override
    @Transactional(readOnly = true)
    public QuestionCategory findByTitle(String title) {
        checkNull(title, "Question category");
        return questionCategoryRepository.findByTitle(title)
                .orElseThrow(() -> new NotExistException(
                        String.format(NO_TITLE_MESSAGE, "question category", title)));
    }
}
