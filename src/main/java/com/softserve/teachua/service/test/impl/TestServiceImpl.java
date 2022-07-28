package com.softserve.teachua.service.test.impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.test.question.QuestionProfile;
import com.softserve.teachua.dto.test.test.CreateTest;
import com.softserve.teachua.dto.test.test.SuccessCreatedTest;
import com.softserve.teachua.model.test.*;
import com.softserve.teachua.repository.test.TestRepository;
import com.softserve.teachua.service.UserService;
import com.softserve.teachua.service.test.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class TestServiceImpl implements TestService {
    private final TestRepository testRepository;
    private final TopicService topicService;
    private final UserService userService;
    private final QuestionService questionService;
    private final QuestionTestService questionTestService;
    private final QuestionTypeService questionTypeService;
    private final QuestionCategoryService questionCategoryService;

    private final DtoConverter dtoConverter;

    @Override
    public SuccessCreatedTest addTest(CreateTest testDto) {
        //User user = userService.getCurrentUser();
        Test test = dtoConverter.convertToEntity(testDto, new Test());
        //test.setCreator(user);
        test.setDateOfCreation(LocalDate.now());
        test.setTopic(topicService.findByTitle(testDto.getTopicTitle()));
        test = testRepository.save(test);
        int grade = 0;

        for (QuestionProfile questionProfile: testDto.getQuestions()) {
            Question question = dtoConverter.convertToEntity(questionProfile, new Question());
            QuestionTest questionTest = new QuestionTest();
            grade += questionProfile.getValue();

            if (Objects.isNull(question.getId())) {
                String categoryTitle = questionProfile.getCategoryTitle();
                QuestionCategory category = questionCategoryService.findByTitle(categoryTitle);
                //question.setCreator(user);
                question.setQuestionCategory(category);
                question.setQuestionType(findQuestionType(questionProfile));
                saveAnswers(questionProfile, question);
                question = questionService.save(question);
            }

            questionTest.setTest(test);
            questionTest.setQuestion(question);
            questionTestService.save(questionTest);
        }

        test.setGrade(grade);
        return dtoConverter.convertFromDtoToDto(testDto, new SuccessCreatedTest());
    }

    @Override
    public List<Test> findActiveTests() {
        return testRepository.findActiveTests();
    }

    @Override
    public List<Test> findArchivedTests(){
        return testRepository.findArchivedTests();
    }

    @Override
    public List<Test> findUnarchivedTests(){
        return testRepository.findUnarchivedTests();
    }

    @Override
    public void archiveTestById(Long id) {
        Test testToArchive = testRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        String.format("There is no test with id '%s'", id)
                ));
        testToArchive.setArchived(true);
    }

    private QuestionType findQuestionType(QuestionProfile question) {
        int numberOfCorrectAnswers = question.getCorrectAnswerIndexes().size();

        if (numberOfCorrectAnswers == 0)
            throw new IllegalArgumentException("There must be at least one correct answer");
        else if (numberOfCorrectAnswers == 1) {
            return questionTypeService.findByTitle("radio");
        } else {
            return questionTypeService.findByTitle("checkbox");
        }
    }

    private void saveAnswers(QuestionProfile questionProfile, Question question) {
        List<Integer> correctIndexes = questionProfile.getCorrectAnswerIndexes();
        List<String> answerTitles = questionProfile.getAnswerTitles();
        int answerValue = questionProfile.getValue() / correctIndexes.size();

        for (int i = 0; i < answerTitles.size(); i++) {
            Answer answer = new Answer();
            answer.setValue(answerValue);
            answer.setText(answerTitles.get(i));
            answer.setCorrect(correctIndexes.contains(i));

            question.addAnswer(answer);
        }
    }
}
