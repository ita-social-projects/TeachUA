package com.softserve.teachua.service.test.impl;

import com.softserve.teachua.model.test.QuestionType;
import com.softserve.teachua.repository.test.QuestionTypeRepository;
import com.softserve.teachua.service.test.QuestionTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static com.softserve.teachua.utils.NullValidator.*;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class QuestionTypeServiceImpl implements QuestionTypeService {
    private final QuestionTypeRepository questionTypeRepository;

    @Override
    public QuestionType findByTitle(String title) {
        checkNull(title, "Question type");
        return questionTypeRepository.findByTitle(title)
                .orElseThrow(() -> new NoSuchElementException(
                        String.format("There is no question type with title '%s'", title)));
    }
}
