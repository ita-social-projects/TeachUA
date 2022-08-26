package com.softserve.teachua.service.test.impl;

import com.softserve.teachua.controller.test.QuestionCategoryController;
import com.softserve.teachua.dto.test.questionCategory.QuestionCategoryProfile;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.test.QuestionCategory;
import com.softserve.teachua.repository.test.QuestionCategoryRepository;
import com.softserve.teachua.service.test.QuestionCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.softserve.teachua.utils.test.Messages.*;
import static com.softserve.teachua.utils.test.validation.NullValidator.checkNull;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class QuestionCategoryServiceImpl implements QuestionCategoryService {
    private final QuestionCategoryRepository questionCategoryRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public QuestionCategory findById(Long id) {
        checkNull(id, "Question category id");
        return questionCategoryRepository.findById(id)
                .orElseThrow(() -> new NotExistException(
                        String.format(NO_ID_MESSAGE, "question", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionCategory findByTitle(String title) {
        checkNull(title, "Question category");
        return questionCategoryRepository.findByTitle(title)
                .orElseThrow(() -> new NotExistException(
                        String.format(NO_TITLE_MESSAGE, "question category", title)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionCategoryProfile> findAllCategoryProfiles() {
        return mapToDtoList(questionCategoryRepository.findAll());
    }

    @Override
    public QuestionCategoryProfile save(QuestionCategoryProfile categoryProfile) {
        checkNull(categoryProfile, "Question category");
        checkIfExists(categoryProfile.getTitle());
        QuestionCategory questionCategory = modelMapper.map(categoryProfile, QuestionCategory.class);
        questionCategoryRepository.save(questionCategory);
        log.info("**/Question category has been created. {}", questionCategory);
        return categoryProfile;
    }

    @Override
    public QuestionCategoryProfile updateById(QuestionCategoryProfile categoryProfile, Long id) {
        checkNull(categoryProfile, "Question category");
        checkIfExists(categoryProfile.getTitle());
        QuestionCategory questionCategory = findById(id);
        questionCategory.setTitle(categoryProfile.getTitle());
        questionCategoryRepository.save(questionCategory);
        log.info("**/Question category with id '{}' has been updated. {}", id, questionCategory);
        return categoryProfile;
    }

    private void checkIfExists(String title){
        if(questionCategoryRepository.existsByTitle(title)){
            throw new AlreadyExistException(String.format(CATEGORY_EXISTS_WITH_TITLE, title));
        }
    }

    private List<QuestionCategoryProfile> mapToDtoList(List<QuestionCategory> questionCategories) {
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
