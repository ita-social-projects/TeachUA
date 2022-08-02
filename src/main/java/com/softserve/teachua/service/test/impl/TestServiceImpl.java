package com.softserve.teachua.service.test.impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.test.answer.PassingTestAnswer;
import com.softserve.teachua.dto.test.question.PassingTestQuestion;
import com.softserve.teachua.dto.test.question.QuestionProfile;
import com.softserve.teachua.dto.test.question.QuestionResult;
import com.softserve.teachua.dto.test.result.CreateResult;
import com.softserve.teachua.dto.test.result.SuccessCreatedResult;
import com.softserve.teachua.dto.test.test.*;
import com.softserve.teachua.model.User;
import com.softserve.teachua.model.test.*;
import com.softserve.teachua.repository.test.TestRepository;
import com.softserve.teachua.service.UserService;
import com.softserve.teachua.service.test.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class TestServiceImpl implements TestService {
    private static final String CORRECT_MESSAGE = "Your answer is correct";
    private static final String INCORRECT_MESSAGE = "Your answer is incorrect";
    private static final String PARTIALLY_CORRECT_MESSAGE = "Your answer is partially correct";

    private final TestRepository testRepository;
    private final ResultService resultService;
    private final TopicService topicService;
    private final UserService userService;
    private final QuestionService questionService;
    private final QuestionTestService questionTestService;
    private final QuestionTypeService questionTypeService;
    private final QuestionCategoryService questionCategoryService;
    private final AnswerService answerService;
    private final DtoConverter dtoConverter;


    @Override
    public SuccessCreatedTest addTest(CreateTest testDto) {
        User user = userService.getCurrentUser();
        Test test = dtoConverter.convertToEntity(testDto, new Test());
        test.setCreator(user);
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
                question.setCreator(user);
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
    public Test findById(Long id) {
        return testRepository.findById(id).orElse(null);
    }

    @Override
    public void archiveTestById(Long id) {
        Test testToArchive = testRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        String.format("There is no test with id '%s'", id)
                ));
        testToArchive.setArchived(true);
    }

    @Override
    public ResultTest getResultTest(Long testId, Long resultId) {
        ResultTest resultTest = new ResultTest();

        Test test = findById(testId);
        Result result = resultService.findById(resultId);
        List<Question> questions = questionService.findQuestionsByTestId(testId);
        Set<QuestionHistory> qHistories = result.getQuestionHistories();
        Map<Long, Set<Long>> questionSelectedAnswers = new HashMap<>();
        questions.forEach(x -> questionSelectedAnswers.put(x.getId(), new HashSet<>()));

        qHistories.stream()
                .map(QuestionHistory::getAnswer)
                .forEach(x -> questionSelectedAnswers.get(x.getQuestion().getId()).add(x.getId()));

        for (Question question : questions) {
            QuestionResult questionResult = new QuestionResult();
            int value = 0;
            int correctAmount = 0;
            int correctSelectedAmount = 0;

            Set<Answer> answers = question.getAnswers();
            Set<Long> selectedAnswerIds = questionSelectedAnswers.get(question.getId());

            for (Answer answer: answers) {
                boolean isCorrect = answer.isCorrect();
                boolean selected = false;

                if (isCorrect)
                    correctAmount++;
                if (selectedAnswerIds.contains(answer.getId())) {
                    selected = true;

                    if (isCorrect) {
                        value += answer.getValue();
                        correctSelectedAmount++;
                    } else {
                        value -= answer.getValue();
                    }
                }
                questionResult.put(answer.getText(), selected);
            }

            questionResult.setTitle(question.getTitle());
            questionResult.setValue(Math.max(value, 0));

            if (correctSelectedAmount == correctAmount) {
                questionResult.setStatus(CORRECT_MESSAGE);
            } else if (correctSelectedAmount == 0) {
                questionResult.setStatus(INCORRECT_MESSAGE);
            } else {
                questionResult.setStatus(PARTIALLY_CORRECT_MESSAGE);
            }

            resultTest.addQuestion(questionResult);
        }
        resultTest.setTitle(test.getTitle());

        return resultTest;
    }

    public SuccessCreatedResult saveResult(CreateResult resultDto) {
        User user = userService.getCurrentUser();
        Result result = new Result();
        result.setUser(user);
        result.setTest(findById(resultDto.getTestId()));
        result.setTestFinishTime(LocalDateTime.now());
        List<Long> answerIds = resultDto.getSelectedAnswersIds();
        // TODO set start time time

        List<Answer> selectedAnswers = answerService.findAllById(answerIds);
        resultService.createResult(result, selectedAnswers);

        SuccessCreatedResult success = new SuccessCreatedResult();
        success.setSelectedAnswersIds(answerIds);
        success.setTestId(resultDto.getTestId());
        success.setUserId(user.getId());
        success.setGrade(result.getGrade());
        return success;
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

    @Override
    public PassTest findPassTestById(Long id) {
        Test test = testRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        String.format("There is no test with id '%s'", id)
                ));
        PassTest passTest = dtoConverter.convertToDto(test, PassTest.class);
        passTest.setQuestions(getPassingTestQuestions(test));
        return passTest;
    }

    @Override
    public TestsContainer findUnarchivedTestProfiles() {
        List<Test> tests = findUnarchivedTests();
        List<TestProfile> testProfiles = new ArrayList<>();
        for(Test t: tests){
            testProfiles.add(dtoConverter.convertToDto(t, TestProfile.class));
        }
        TestsContainer testsContainer = new TestsContainer();
        testsContainer.setTestProfiles(testProfiles);
        return testsContainer;
    }

    private List<PassingTestQuestion> getPassingTestQuestions(Test test) {
        List<PassingTestQuestion> passingTestQuestions = new ArrayList<>();
        for (Question q: questionService.findQuestionsByTest(test)) {
            PassingTestQuestion passingTestQuestion = dtoConverter.convertToDto(q, PassingTestQuestion.class);
            passingTestQuestions.add(passingTestQuestion);
        }
        return passingTestQuestions;
    }
}
