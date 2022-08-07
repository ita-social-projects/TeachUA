package com.softserve.teachua.service.test.impl;

import com.softserve.teachua.controller.test.TestController;
import com.softserve.teachua.dto.test.question.PassingTestQuestion;
import com.softserve.teachua.dto.test.question.QuestionProfile;
import com.softserve.teachua.dto.test.test.*;
import com.softserve.teachua.model.User;
import com.softserve.teachua.model.test.*;
import com.softserve.teachua.repository.test.SubscriptionRepository;
import com.softserve.teachua.repository.test.TestRepository;
import com.softserve.teachua.service.UserService;
import com.softserve.teachua.service.test.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Link;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.softserve.teachua.utils.NullValidator.checkNull;
import static com.softserve.teachua.utils.NullValidator.checkNullIds;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class TestServiceImpl implements TestService {
    private final TestRepository testRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final TopicService topicService;
    private final UserService userService;
    private final QuestionService questionService;
    private final QuestionTestService questionTestService;
    private final QuestionTypeService questionTypeService;
    private final QuestionCategoryService questionCategoryService;
    private final GroupService groupService;
    private final ModelMapper modelMapper;

    @Scheduled(fixedDelay = 1000 * 3600 * 24)
    public void updateTestsStatus(){
        List<Test> tests = testRepository.findAll();
        LocalDate currentDate = LocalDate.now();

        for(Test test : tests) {
            boolean isActive = groupService.findAllByTestId(test.getId())
                    .stream()
                    .anyMatch(x -> {
                        LocalDate startDate = x.getStartDate();
                        LocalDate endDate = x.getEndDate();
                        return currentDate.isAfter(startDate) &&
                               currentDate.isBefore(endDate);
                    });

            test.setActive(isActive);
            testRepository.save(test);
        }
    }

    @Override
    public SuccessCreatedTest addTest(CreateTest testDto) {
        User user = userService.getCurrentUser();
        Test test = modelMapper.map(testDto, Test.class);
        test.setCreator(user);
        test.setDateOfCreation(LocalDate.now());
        test.setTopic(topicService.findByTitle(testDto.getTopicTitle()));
        test = testRepository.save(test);
        int grade = 0;

        for (QuestionProfile questionProfile: testDto.getQuestions()) {
            Question question = modelMapper.map(questionProfile, Question.class);
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
        return modelMapper.map(testDto, SuccessCreatedTest.class);
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
    public List<Test> findAllByGroupId(Long groupId) {
        checkNull(groupId, "Group id");
        return testRepository.findAllByGroupId(groupId);
    }

    @Override
    public Test findById(Long id) {
        checkNull(id, "Test id");
        return testRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        String.format("There's no test with id '%d'", id)));
    }

    @Override
    public void archiveTestById(Long id) {
        checkNull(id, "Test id");
        Test testToArchive = testRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        String.format("There is no test with id '%s'", id)));
        testToArchive.setArchived(true);
    }

    @Override
    public PassTest findPassTestById(Long id) {
        checkNull(id, "Test id");
        Test test = findById(id);
        PassTest passTest = modelMapper.map(test, PassTest.class);
        passTest.setQuestions(getPassingTestQuestions(test));
        return passTest;
    }

    @Override
    public List<TestProfile> findUnarchivedTestProfiles() {
        List<Test> tests = findUnarchivedTests();
        List<TestProfile> testProfiles = new ArrayList<>();
        for(Test t: tests){
            TestProfile testProfile = modelMapper.map(t, TestProfile.class);

            Link viewTestLink = linkTo(methodOn(TestController.class)
                    .viewTest(t.getId()))
                    .withRel("viewTest");
            testProfile.add(viewTestLink);

            testProfiles.add(testProfile);
        }
        return testProfiles;
    }

    @Override
    public ViewTest findViewTestById(Long id) {
        checkNull(id, "Test id");
        Test test = findById(id);
        User user = userService.getCurrentUser();
        ViewTest viewTest = modelMapper.map(test, ViewTest.class);

        Link testGroups = linkTo(methodOn(TestController.class)
                .getGroups(id))
                .withRel("allGroups");
        viewTest.add(testGroups);

        if (test.isActive()) {
            boolean hasSubscription = hasSubscription(user.getId(), test.getId());

            if (hasSubscription) {
                Link passTest = linkTo(methodOn(TestController.class)
                        .passTest(id))
                        .withRel("startTest");
                viewTest.add(passTest);
                viewTest.setAllowed(true);
            }
        }
        return viewTest;
    }

    @Override
    public boolean hasSubscription(Long userId, Long testId) {
        checkNullIds(userId, testId);
        List<Long> userGroupsIds = subscriptionRepository.findAllByUserId(userId).stream()
                .map(Subscription::getGroup)
                .map(Group::getId)
                .collect(Collectors.toList());
        List<Group> testGroups = groupService.findAllByTestId(testId);
        checkNullIds(userId, testId);
        return testGroups.stream()
                .anyMatch(group -> userGroupsIds.contains(group.getId()));
    }

    private List<PassingTestQuestion> getPassingTestQuestions(Test test) {
        List<Question> questions = questionService.findQuestionsByTest(test);
        return questions.stream()
                .map(question -> modelMapper.map(question, PassingTestQuestion.class))
                .collect(Collectors.toList());
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
