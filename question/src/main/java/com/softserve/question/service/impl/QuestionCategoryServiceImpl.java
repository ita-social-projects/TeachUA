package com.softserve.question.service.impl;

import com.softserve.commons.exception.AlreadyExistException;
import com.softserve.commons.exception.EntityIsUsedException;
import com.softserve.commons.exception.NotExistException;
import com.softserve.question.controller.QuestionCategoryController;
import com.softserve.question.dto.question_category.QuestionCategoryProfile;
import com.softserve.question.dto.question_category.QuestionCategoryResponse;
import com.softserve.question.model.QuestionCategory;
import com.softserve.question.repository.QuestionCategoryRepository;
import com.softserve.question.repository.QuestionRepository;
import com.softserve.question.service.QuestionCategoryService;
import static com.softserve.question.util.Messages.CATEGORY_CAN_NOT_BE_DELETED;
import static com.softserve.question.util.Messages.CATEGORY_EXISTS_WITH_TITLE;
import static com.softserve.question.util.Messages.NO_ID_MESSAGE;
import static com.softserve.question.util.Messages.NO_TITLE_MESSAGE;
import static com.softserve.question.util.validation.NullValidator.checkNull;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
public class QuestionCategoryServiceImpl implements QuestionCategoryService {
    private static final String QUESTION_CATEGORY_ID_EXCEPTION_MESSAGE = "Question category id";
    private static final String QUESTION_CATEGORY_TITLE_EXCEPTION_MESSAGE = "Question category title";
    private static final String QUESTION_CATEGORY_EXCEPTION_MESSAGE = "Question category";
    private final QuestionCategoryRepository questionCategoryRepository;
    private final QuestionRepository questionRepository;
    private final ModelMapper modelMapper;

    public QuestionCategoryServiceImpl(QuestionCategoryRepository questionCategoryRepository,
                                       QuestionRepository questionRepository, ModelMapper modelMapper) {
        this.questionCategoryRepository = questionCategoryRepository;
        this.questionRepository = questionRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<QuestionCategory> findAll() {
        return questionCategoryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionCategory findById(Long id) {
        checkNull(id, QUESTION_CATEGORY_ID_EXCEPTION_MESSAGE);
        return questionCategoryRepository.findById(id)
                .orElseThrow(() -> new NotExistException(
                        String.format(NO_ID_MESSAGE, QUESTION_CATEGORY_EXCEPTION_MESSAGE, id)));
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionCategory findByTitle(String title) {
        checkNull(title, QUESTION_CATEGORY_TITLE_EXCEPTION_MESSAGE);
        return questionCategoryRepository.findByTitle(title)
                .orElseThrow(() -> new NotExistException(
                        String.format(NO_TITLE_MESSAGE, QUESTION_CATEGORY_EXCEPTION_MESSAGE, title)));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QuestionCategoryResponse> searchAllQuestionCategoriesPageable(Pageable pageable, String title) {
        return questionCategoryRepository.findByTitleContainingIgnoreCase(pageable, title).map(this::mapToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionCategoryProfile> findAllCategoryProfiles() {
        return mapToDtoList(questionCategoryRepository.findAll());
    }

    @Override
    @Transactional
    public QuestionCategoryProfile save(QuestionCategoryProfile categoryProfile) {
        checkNull(categoryProfile, QUESTION_CATEGORY_EXCEPTION_MESSAGE);
        if (Boolean.TRUE.equals(questionCategoryRepository.existsByTitle(categoryProfile.getTitle()))) {
            throw new AlreadyExistException(String.format(CATEGORY_EXISTS_WITH_TITLE, categoryProfile.getTitle()));
        }
        QuestionCategory questionCategory = modelMapper.map(categoryProfile, QuestionCategory.class);
        questionCategoryRepository.save(questionCategory);
        log.info("**/Question category has been created. {}", questionCategory);
        return categoryProfile;
    }

    @Override
    public QuestionCategoryProfile updateById(QuestionCategoryProfile categoryProfile, Long id) {
        checkNull(categoryProfile, QUESTION_CATEGORY_EXCEPTION_MESSAGE);
        QuestionCategory questionCategory = findById(id);
        questionCategory.setTitle(categoryProfile.getTitle());
        questionCategoryRepository.save(questionCategory);
        log.info("**/Question category with id '{}' has been updated. {}", id, questionCategory);
        return categoryProfile;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (questionRepository.existsByQuestionCategoryId(id)) {
            throw new EntityIsUsedException(CATEGORY_CAN_NOT_BE_DELETED);
        }
        questionCategoryRepository.deleteById(id);
        log.info("**/Question category with id {} has been deleted.", id);
    }

    public QuestionCategoryResponse mapToDto(QuestionCategory category) {
        return modelMapper.map(category, QuestionCategoryResponse.class);
    }

    public List<QuestionCategoryProfile> mapToDtoList(List<QuestionCategory> questionCategories) {
        List<QuestionCategoryProfile> questionCategoriesProfiles = new ArrayList<>();

        for (QuestionCategory category : questionCategories) {
            QuestionCategoryProfile categoryProfile = modelMapper.map(category, QuestionCategoryProfile.class);
            Link link = linkTo(methodOn(QuestionCategoryController.class)
                    .updateQuestionCategory(categoryProfile, category.getId()))
                    .withRel("update");
            categoryProfile.add(link);
            questionCategoriesProfiles.add(categoryProfile);
        }
        return questionCategoriesProfiles;
    }
}
