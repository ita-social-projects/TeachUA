package com.softserve.question.service.impl;

import com.softserve.commons.exception.NotExistException;
import com.softserve.question.controller.TestController;
import com.softserve.question.dto.question.PassingTestQuestion;
import com.softserve.question.dto.question.QuestionProfile;
import com.softserve.question.dto.test.CreateTest;
import com.softserve.question.dto.test.PassTest;
import com.softserve.question.dto.test.SuccessCreatedTest;
import com.softserve.question.dto.test.TestProfile;
import com.softserve.question.dto.test.ViewTest;
import com.softserve.question.model.Answer;
import com.softserve.question.model.Group;
import com.softserve.question.model.Question;
import com.softserve.question.model.QuestionCategory;
import com.softserve.question.model.QuestionTest;
import com.softserve.question.model.QuestionType;
import com.softserve.question.model.Subscription;
import com.softserve.question.model.Test;
import com.softserve.question.repository.SubscriptionRepository;
import com.softserve.question.repository.TestRepository;
import com.softserve.question.security.UserPrincipal;
import com.softserve.question.service.GroupService;
import com.softserve.question.service.QuestionCategoryService;
import com.softserve.question.service.QuestionService;
import com.softserve.question.service.QuestionTestService;
import com.softserve.question.service.QuestionTypeService;
import com.softserve.question.service.TestService;
import com.softserve.question.service.TopicService;
import static com.softserve.question.util.Messages.NO_CORRECT_ANSWERS_MESSAGE;
import static com.softserve.question.util.Messages.NO_ID_MESSAGE;
import static com.softserve.question.util.validation.NullValidator.checkNull;
import static com.softserve.question.util.validation.NullValidator.checkNullIds;
import com.softserve.question.util.validation.service.TestValidationService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Link;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
public class TestServiceImpl implements TestService {
    private final TestRepository testRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final TopicService topicService;
    private final QuestionService questionService;
    private final QuestionTestService questionTestService;
    private final QuestionTypeService questionTypeService;
    private final QuestionCategoryService questionCategoryService;
    private final GroupService groupService;
    private final ModelMapper modelMapper;
    private final TestValidationService testValidationService;

    public TestServiceImpl(TestRepository testRepository, SubscriptionRepository subscriptionRepository,
                           TopicService topicService, QuestionService questionService,
                           QuestionTestService questionTestService, QuestionTypeService questionTypeService,
                           QuestionCategoryService questionCategoryService, GroupService groupService,
                           ModelMapper modelMapper, TestValidationService testValidationService) {
        this.testRepository = testRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.topicService = topicService;
        this.questionService = questionService;
        this.questionTestService = questionTestService;
        this.questionTypeService = questionTypeService;
        this.questionCategoryService = questionCategoryService;
        this.groupService = groupService;
        this.modelMapper = modelMapper;
        this.testValidationService = testValidationService;
    }

    @Scheduled(fixedDelay = 1000 * 3600 * 24)
    public void updateTestsStatus() {
        List<Test> tests = testRepository.findAll();
        LocalDate currentDate = LocalDate.now();

        for (Test test : tests) {
            List<Group> groups = groupService.findAllByTestId(test.getId());
            boolean isActive = groups
                    .stream()
                    .anyMatch(group -> {
                        LocalDate startDate = group.getStartDate();
                        LocalDate endDate = group.getEndDate();
                        return currentDate.isAfter(startDate) && currentDate.isBefore(endDate);
                    });
            test.setActive(isActive);
            testRepository.save(test);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Test findById(Long id) {
        checkNull(id, "Test id");
        return testRepository.findById(id)
                .orElseThrow(() -> new NotExistException(
                        String.format(NO_ID_MESSAGE, "test", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Test> findActiveTests() {
        return testRepository.findActiveTests();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Test> findArchivedTests() {
        return testRepository.findArchivedTests();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Test> findUnarchivedTests() {
        return testRepository.findUnarchivedTests();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Test> findAllByGroupId(Long groupId) {
        checkNull(groupId, "Group id");
        return testRepository.findAllByGroupId(groupId);
    }

    @Override
    @Transactional(readOnly = true)
    public PassTest findPassTestById(Long id) {
        Test test = findById(id);
        PassTest passTest = modelMapper.map(test, PassTest.class);
        passTest.setQuestions(getPassingTestQuestions(test));
        return passTest;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TestProfile> findUnarchivedTestProfiles() {
        List<Test> tests = findUnarchivedTests();
        List<TestProfile> testProfiles = new ArrayList<>();
        for (Test test : tests) {
            TestProfile testProfile = modelMapper.map(test, TestProfile.class);
            Link viewTest = linkTo(methodOn(TestController.class)
                    .viewTest(test.getId()))
                    .withRel("viewTest");
            testProfile.add(viewTest);
            testProfiles.add(testProfile);
        }
        return testProfiles;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TestProfile> findArchivedTestProfiles() {
        List<Test> tests = findArchivedTests();
        List<TestProfile> testProfiles = new ArrayList<>();
        for (Test test : tests) {
            TestProfile testProfile = modelMapper.map(test, TestProfile.class);
            Link restoreTest = linkTo(methodOn(TestController.class)
                    .restoreTest(test.getId()))
                    .withRel("restore");
            testProfile.add(restoreTest);
            testProfiles.add(testProfile);
        }
        return testProfiles;
    }

    @Override
    @Transactional(readOnly = true)
    public ViewTest findViewTestById(Long id) {
        checkNull(id, "test id");
        Test test = findById(id);
        Long userId =
                ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        ViewTest viewTest = modelMapper.map(test, ViewTest.class);
        Link testGroups = linkTo(methodOn(TestController.class)
                .getGroups(id))
                .withRel("allGroups");
        viewTest.add(testGroups);

        if (test.isActive()) {
            boolean hasSubscription = hasActiveSubscription(userId, test.getId());

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
    @Transactional(readOnly = true)
    public boolean hasActiveSubscription(Long userId, Long testId) {
        checkNullIds(userId, testId);
        List<Long> userGroupsIds = subscriptionRepository.findAllByUserId(userId).stream()
                .filter(this::isActiveSubscription)
                .map(Subscription::getGroup)
                .map(Group::getId)
                .toList();
        List<Group> testGroups = groupService.findAllByTestId(testId);
        return testGroups.stream()
                .anyMatch(group -> userGroupsIds.contains(group.getId()));
    }

    @Override
    public SuccessCreatedTest addTest(CreateTest testDto) {
        checkNull(testDto, "Test");
        testValidationService.validateTest(testDto);
        Long userId =
                ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        Test test = modelMapper.map(testDto, Test.class);
        test.setCreatorId(userId);
        test.setDateOfCreation(LocalDate.now());
        test.setTopic(topicService.findByTitle(testDto.getTopicTitle()));
        setQuestions(testDto, test);
        testRepository.save(test);
        SuccessCreatedTest successCreatedTest;
        successCreatedTest = modelMapper.map(testDto, SuccessCreatedTest.class);
        log.info("**/Test has been created. {}", successCreatedTest.toString());
        return successCreatedTest;
    }

    private void setQuestions(CreateTest testDto, Test test) {
        int grade = 0;
        for (QuestionProfile questionProfile : testDto.getQuestions()) {
            Question question = modelMapper.map(questionProfile, Question.class);
            QuestionTest questionTest = new QuestionTest();
            grade += questionProfile.getValue();
            if (Objects.isNull(question.getId())) {
                String categoryTitle = questionProfile.getCategoryTitle();
                QuestionCategory category = questionCategoryService.findByTitle(categoryTitle);
                question.setQuestionType(findQuestionType(questionProfile));
                question.setCreatorId(test.getCreatorId());
                question.setQuestionCategory(category);
                saveAnswers(questionProfile, question);
                question = questionService.save(question);
            }
            questionTest.setTest(test);
            questionTest.setQuestion(question);
            questionTestService.save(questionTest);
        }
        test.setGrade(grade);
    }

    @Override
    public TestProfile archiveTestById(Long id) {
        Test testToArchive = findById(id);
        testToArchive.setArchived(true);
        log.info("**/Test with id '{}' has been archived.", id);
        return modelMapper.map(testToArchive, TestProfile.class);
    }

    @Override
    public TestProfile restoreTestById(Long id) {
        Test testToRestore = findById(id);
        testToRestore.setArchived(false);
        log.info("**/Test with id '{}' has been restored.", id);
        return modelMapper.map(testToRestore, TestProfile.class);
    }

    private List<PassingTestQuestion> getPassingTestQuestions(Test test) {
        List<Question> questions = questionService.findQuestionsByTest(test);
        return questions.stream()
                .map(question -> modelMapper.map(question, PassingTestQuestion.class))
                .toList();
    }

    private boolean isActiveSubscription(Subscription subscription) {
        Group group = subscription.getGroup();
        return subscription.getExpirationDate().equals(group.getEndDate());
    }

    private QuestionType findQuestionType(QuestionProfile question) {
        int numberOfCorrectAnswers = question.getCorrectAnswerIndexes().size();

        if (numberOfCorrectAnswers == 0) {
            throw new IllegalArgumentException(NO_CORRECT_ANSWERS_MESSAGE);
        } else if (numberOfCorrectAnswers == 1) {
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
