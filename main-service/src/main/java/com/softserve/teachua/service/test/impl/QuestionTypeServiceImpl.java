package com.softserve.teachua.service.test.impl;

import com.softserve.teachua.dto.test.question_type.QuestionTypeProfile;
import com.softserve.teachua.dto.test.question_type.QuestionTypeResponse;
import com.softserve.commons.exception.AlreadyExistException;
import com.softserve.commons.exception.EntityIsUsedException;
import com.softserve.commons.exception.NotExistException;
import com.softserve.teachua.model.test.QuestionType;
import com.softserve.teachua.repository.test.QuestionRepository;
import com.softserve.teachua.repository.test.QuestionTypeRepository;
import com.softserve.teachua.service.test.QuestionTypeService;
import static com.softserve.teachua.utils.test.Messages.CATEGORY_EXISTS_WITH_TITLE;
import static com.softserve.teachua.utils.test.Messages.NO_ID_MESSAGE;
import static com.softserve.teachua.utils.test.Messages.NO_TITLE_MESSAGE;
import static com.softserve.teachua.utils.test.Messages.TYPE_CAN_NOT_BE_DELETED;
import static com.softserve.teachua.utils.test.validation.NullValidator.checkNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class QuestionTypeServiceImpl implements QuestionTypeService {
    private final QuestionTypeRepository questionTypeRepository;
    private final QuestionRepository questionRepository;
    private final ModelMapper modelMapper;
    private static final String QUESTION_TYPE_EXCEPTION_MESSAGE = "Question type";

    @Override
    @Transactional(readOnly = true)
    public QuestionType findById(Long id) {
        return questionTypeRepository.findById(id)
                .orElseThrow(() -> new NotExistException(
                        String.format(NO_ID_MESSAGE, QUESTION_TYPE_EXCEPTION_MESSAGE, id)
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionType findByTitle(String title) {
        checkNull(title, QUESTION_TYPE_EXCEPTION_MESSAGE);
        return questionTypeRepository.findByTitle(title)
                .orElseThrow(() -> new NotExistException(
                        String.format(NO_TITLE_MESSAGE, QUESTION_TYPE_EXCEPTION_MESSAGE, title)));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QuestionTypeResponse> searchAllQuestionCategoriesPageable(Pageable pageable, String title) {
        return questionTypeRepository.findByTitleContainingIgnoreCase(pageable, title).map(this::mapToDto);
    }

    @Override
    @Transactional
    public QuestionTypeProfile save(QuestionTypeProfile typeProfile) {
        checkNull(typeProfile, QUESTION_TYPE_EXCEPTION_MESSAGE);
        if (questionTypeRepository.existsByTitle(typeProfile.getTitle())) {
            throw new AlreadyExistException(String.format(CATEGORY_EXISTS_WITH_TITLE, typeProfile.getTitle()));
        }
        QuestionType questionType = modelMapper.map(typeProfile, QuestionType.class);
        questionTypeRepository.save(questionType);
        log.info("**/Question type has been created. {}", questionType);
        return typeProfile;
    }

    @Override
    @Transactional
    public QuestionTypeProfile updateById(QuestionTypeProfile typeProfile, Long id) {
        checkNull(typeProfile, QUESTION_TYPE_EXCEPTION_MESSAGE);
        QuestionType questionType = findById(id);
        questionType.setTitle(typeProfile.getTitle());
        questionTypeRepository.save(questionType);
        log.info("**/Question type with id '{}' has been updated. {}", id, questionType);
        return typeProfile;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (questionRepository.existsByQuestionTypeId(id)) {
            throw new EntityIsUsedException(TYPE_CAN_NOT_BE_DELETED);
        }
        questionTypeRepository.deleteById(id);
        log.info("**/Question type with id {} has been deleted.", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionType> findAll() {
        return questionTypeRepository.findAll();
    }

    public QuestionTypeResponse mapToDto(QuestionType type) {
        return modelMapper.map(type, QuestionTypeResponse.class);
    }
}
