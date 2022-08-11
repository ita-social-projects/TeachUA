package com.softserve.teachua.service.test.impl;

import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.test.QuestionType;
import com.softserve.teachua.repository.test.QuestionTypeRepository;
import com.softserve.teachua.service.test.QuestionTypeService;
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
public class QuestionTypeServiceImpl implements QuestionTypeService {
    private final QuestionTypeRepository questionTypeRepository;

    @Override
    @Transactional(readOnly = true)
    public QuestionType findByTitle(String title) {
        checkNull(title, "Question type");
        return questionTypeRepository.findByTitle(title)
                .orElseThrow(() -> new NoSuchElementException(
                        String.format(NO_TITLE_MESSAGE, "question type", title)));

    }
}
