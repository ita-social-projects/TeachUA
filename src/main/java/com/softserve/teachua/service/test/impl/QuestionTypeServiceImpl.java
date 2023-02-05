package com.softserve.teachua.service.test.impl;

import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.test.QuestionType;
import com.softserve.teachua.repository.test.QuestionTypeRepository;
import com.softserve.teachua.service.test.QuestionTypeService;
import static com.softserve.teachua.utils.test.Messages.NO_TITLE_MESSAGE;
import static com.softserve.teachua.utils.test.validation.NullValidator.checkNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .orElseThrow(() -> new NotExistException(
                        String.format(NO_TITLE_MESSAGE, "question type", title)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionType> findAll() {
        return questionTypeRepository.findAll();
    }
}
