package com.softserve.teachua.service.test;

import com.softserve.teachua.dto.test.question.QuestionProfile;
import com.softserve.teachua.dto.test.test.*;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.User;
import com.softserve.teachua.model.test.*;
import com.softserve.teachua.repository.test.SubscriptionRepository;
import com.softserve.teachua.repository.test.TestRepository;
import com.softserve.teachua.service.UserService;
import com.softserve.teachua.service.test.impl.TestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestServiceTest {
    @Mock
    private TestRepository testRepository;
    @Mock
    private SubscriptionRepository subscriptionRepository;
    @Mock
    private GroupService groupService;
    @Mock
    private TopicService topicService;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private UserService userService;
    @Mock
    private QuestionService questionService;
    @Mock
    private QuestionCategoryService questionCategoryService;
    @Mock
    private QuestionTypeService questionTypeService;
    @Mock
    private QuestionTestService questionTestService;

    @InjectMocks
    private TestServiceImpl testService;

    private final Long EXISTING_TEST_ID = 1L;
    private final Long NOT_EXISTING_TEST_ID = 100L;

    private final Long USER_ID = 1L;

    private ModelMapper mapper = new ModelMapper();
    private com.softserve.teachua.model.test.Test test;
    private PassTest passTest;
    private Question question;
    private QuestionProfile questionProfile;
    private TestProfile testProfile;
    private ViewTest viewTest;
    private SuccessCreatedTest successCreatedTest;
    private CreateTest createTest;
    private List<Subscription> subscriptionsByUser;
    private List<Group> groupsByTest;

    @BeforeEach
    void setUp() {
        subscriptionsByUser = new ArrayList<>();
        groupsByTest = new ArrayList<>();
        questionProfile = generateQuestion(Arrays.asList(1, 2));
        createTest = generateCreateTest();
        passTest = generatePassTest();
        Subscription firstSubscription = generateSubscription();
        Subscription secondSubscription = generateSubscription();
        firstSubscription.setGroup(generateGroupWithId(1L));
        secondSubscription.setGroup(generateGroupWithId(3L));
        subscriptionsByUser.add(firstSubscription);
        subscriptionsByUser.add(secondSubscription);
        groupsByTest.add(generateGroupWithId(1L));
        groupsByTest.add(generateGroupWithId(2L));
        test = generateTestWithId(EXISTING_TEST_ID);
        testProfile = mapper.map(createTest, TestProfile.class);
        viewTest = mapper.map(createTest, ViewTest.class);
        successCreatedTest = mapper.map(createTest, SuccessCreatedTest.class);
        question = mapper.map(questionProfile, Question.class);
        System.out.println(question);
        System.out.println(questionProfile);
        setQuestionProfiles();
    }

    @Test
    void findTestByExistingIdShouldReturnTest() {
        when(testRepository.findById(EXISTING_TEST_ID)).thenReturn(Optional.of(test));
        assertEquals(test, testService.findById(EXISTING_TEST_ID));
    }

    @Test
    void findTestByNotExistingIdShouldThrowNotExistException() {
        when(testRepository.findById(NOT_EXISTING_TEST_ID)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> testService.findById(NOT_EXISTING_TEST_ID))
                .isInstanceOf(NotExistException.class);
    }

    @Test
    void findTestByTestIdIsNullShouldThrowIllegalArgumentException() {
        assertThatThrownBy(() -> testService.findById(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findAllActiveTestsShouldReturnListOfTests() {
        test.setActive(true);
        when(testRepository.findActiveTests()).thenReturn(Collections.singletonList(test));
        assertEquals(test, testService.findActiveTests().get(0));
    }

    @Test
    void findAllArchivedTestsShouldReturnListOfTests() {
        test.setArchived(true);
        when(testRepository.findArchivedTests()).thenReturn(Collections.singletonList(test));
        assertEquals(test, testService.findArchivedTests().get(0));
    }

    @Test
    void findAllUnarchivedTestsShouldReturnListOfTests() {
        when(testRepository.findUnarchivedTests()).thenReturn(Collections.singletonList(test));
        assertEquals(test, testService.findUnarchivedTests().get(0));
    }

    @Test
    void findAllTestsByExistingGroupIdShouldReturnListOfTests() {
        final Long EXISTING_GROUP_ID = 1L;
        when(testRepository.findAllByGroupId(EXISTING_GROUP_ID)).thenReturn(Collections.singletonList(test));
        assertEquals(test, testService.findAllByGroupId(EXISTING_GROUP_ID).get(0));
    }

    @Test
    void findAllTestsByNotExistingGroupIdShouldReturnListOfTests() {
        final Long NOT_EXISTING_GROUP_ID = 100L;
        when(testRepository.findAllByGroupId(NOT_EXISTING_GROUP_ID)).thenReturn(Collections.emptyList());
        assertEquals(0, testService.findAllByGroupId(NOT_EXISTING_GROUP_ID).size());
    }

    @Test
    void findAllTestsByGroupIdIsNullShouldThrowIllegalArgumentException() {
        assertThatThrownBy(() -> testService.findAllByGroupId(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findPassTestByExistingIdShouldReturnPassTest() {
        when(testRepository.findById(EXISTING_TEST_ID)).thenReturn(Optional.of(test));
        when(modelMapper.map(test, PassTest.class)).thenReturn(passTest);
        when(questionService.findQuestionsByTest(test)).thenReturn(Collections.emptyList());
        PassTest actual = testService.findPassTestById(EXISTING_TEST_ID);
        assertEquals(passTest, actual);
    }

    @Test
    void findAllArchivedTestProfilesShouldReturnListOfTestProfiles() {
        test.setArchived(true);
        testProfile.setArchived(true);
        when(testRepository.findArchivedTests()).thenReturn(Collections.singletonList(test));
        when(modelMapper.map(test, TestProfile.class)).thenReturn(testProfile);
        assertEquals(testProfile, testService.findArchivedTestProfiles().get(0));
    }

    @Test
    void findAllUnarchivedTestProfilesShouldReturnListOfTestProfiles() {
        when(testRepository.findUnarchivedTests()).thenReturn(Collections.singletonList(test));
        when(modelMapper.map(test, TestProfile.class)).thenReturn(testProfile);
        assertEquals(testProfile, testService.findUnarchivedTestProfiles().get(0));
    }

    @Test
    void findViewTestByExistingTestIdShouldReturnViewTest() {
        test.setActive(true);
        when(testRepository.findById(EXISTING_TEST_ID)).thenReturn(Optional.of(test));
        when(userService.getCurrentUser()).thenReturn(generateUser());
        when(modelMapper.map(test, ViewTest.class)).thenReturn(viewTest);
        when(subscriptionRepository.findAllByUserId(USER_ID)).thenReturn(subscriptionsByUser);
        when(groupService.findAllByTestId(EXISTING_TEST_ID)).thenReturn(groupsByTest);

        assertEquals(viewTest, testService.findViewTestById(EXISTING_TEST_ID));
    }

    @Test
    void findViewTestByNotExistingTestIdShouldThrowNotExistException() {
        when(testRepository.findById(NOT_EXISTING_TEST_ID)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> testService.findViewTestById(NOT_EXISTING_TEST_ID))
            .isInstanceOf(NotExistException.class);
    }

    @Test
    void findViewTestByTestIdIsNullShouldThrowNotExistException() {
        assertThatThrownBy(() -> testService.findViewTestById(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void hasActiveSubscriptionByUserIdAndTestIdShouldReturnTrue() {
        when(subscriptionRepository.findAllByUserId(USER_ID)).thenReturn(subscriptionsByUser);
        when(groupService.findAllByTestId(EXISTING_TEST_ID)).thenReturn(groupsByTest);
        assertTrue(testService.hasActiveSubscription(USER_ID, EXISTING_TEST_ID));
    }

    @Test
    void hasActiveSubscriptionByUserIdAndTestIdShouldReturnFalse() {
        subscriptionsByUser.clear();
        when(subscriptionRepository.findAllByUserId(USER_ID)).thenReturn(subscriptionsByUser);
        when(groupService.findAllByTestId(EXISTING_TEST_ID)).thenReturn(groupsByTest);
        assertFalse(testService.hasActiveSubscription(USER_ID, EXISTING_TEST_ID));
    }

    @Test
    void hasActiveSubscriptionByUserIdIsNullAndTestIdIsNullShouldThrowIllegalArgumentException() {
        assertThatThrownBy(() -> testService.hasActiveSubscription(null, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void archiveTestByExistingTestIdShouldReturnTestProfileWithArchivedIsTrue() {
        testProfile.setArchived(true);
        when(testRepository.findById(EXISTING_TEST_ID)).thenReturn(Optional.of(test));
        when(modelMapper.map(test, TestProfile.class)).thenReturn(testProfile);
        assertTrue(testService.archiveTestById(EXISTING_TEST_ID).isArchived());
    }

    @Test
    void archiveTestByNotExistingTestIdShouldThrowNotExistException() {
        when(testRepository.findById(NOT_EXISTING_TEST_ID)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> testService.archiveTestById(NOT_EXISTING_TEST_ID))
                .isInstanceOf(NotExistException.class);
    }

    @Test
    void archiveTestByTestIdIsNullShouldThrowIllegalArgumentException() {
        assertThatThrownBy(() -> testService.archiveTestById(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void restoreTestByExistingTestIdShouldReturnTestProfileWithArchivedIsFalse() {
        when(testRepository.findById(EXISTING_TEST_ID)).thenReturn(Optional.of(test));
        when(modelMapper.map(test, TestProfile.class)).thenReturn(testProfile);
        assertFalse(testService.restoreTestById(EXISTING_TEST_ID).isArchived());
    }

    @Test
    void restoreTestByNotExistingTestIdShouldThrowNotExistException() {
        when(testRepository.findById(NOT_EXISTING_TEST_ID)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> testService.restoreTestById(NOT_EXISTING_TEST_ID))
                .isInstanceOf(NotExistException.class);
    }

    @Test
    void restoreTestByTestIdIsNullShouldThrowIllegalArgumentException() {
        assertThatThrownBy(() -> testService.restoreTestById(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createValidTestShouldReturnSuccessCreatedTest() {
        String topicTitle = createTest.getTopicTitle();
        String categoryTitle = questionProfile.getCategoryTitle();
        Topic topic = generateTopic(topicTitle);
        when(topicService.findByTitle(topicTitle)).thenReturn(topic);
        when(questionCategoryService.findByTitle(categoryTitle))
                .thenReturn(generateQuestionCategory(categoryTitle));
        when(questionService.save(question)).thenReturn(question);
        when(questionTypeService.findByTitle("checkbox")).thenReturn(generateQuestionType("checkbox"));
        when(questionTypeService.findByTitle("radio")).thenReturn(generateQuestionType("radio"));
        when(modelMapper.map(createTest, com.softserve.teachua.model.test.Test.class)).thenReturn(test);
        when(modelMapper.map(createTest, SuccessCreatedTest.class)).thenReturn(successCreatedTest);
        when(modelMapper.map(generateQuestion(Arrays.asList(1, 2)), Question.class)).thenReturn(question);
        when(modelMapper.map(generateQuestion(Collections.singletonList(1)), Question.class)).thenReturn(question);
        assertEquals(successCreatedTest, testService.addTest(createTest));
    }

    @Test
    void createTestWithQuestionWithoutCorrectAnswersShouldThrowIllegalArgumentException() {
        QuestionProfile questionProfile = generateQuestion(Collections.emptyList());
        createTest.getQuestions().add(0, questionProfile);
        String topicTitle = createTest.getTopicTitle();
        Topic topic = generateTopic(topicTitle);
        when(modelMapper.map(createTest, com.softserve.teachua.model.test.Test.class)).thenReturn(test);
        when(topicService.findByTitle(topicTitle)).thenReturn(topic);
        when(modelMapper.map(questionProfile, Question.class)).thenReturn(question);
        assertThatThrownBy(() -> testService.addTest(createTest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createTestByTestDtoIsNullShouldReturnSuccessCreatedTest() {
        assertThatThrownBy(() -> testService.addTest(null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void updateTestsStatusShouldChangeTestActiveStatus() {
        com.softserve.teachua.model.test.Test test = mock(com.softserve.teachua.model.test.Test.class);
        test.setId(EXISTING_TEST_ID);
        when(testRepository.findAll()).thenReturn(Collections.singletonList(test));
        when(groupService.findAllByTestId(0L))
                .thenReturn(Collections.singletonList(generateGroup()));
        doNothing().when(test).setActive(true);
        testService.updateTestsStatus();
        verify(test, times(1)).setActive(true);
    }

    private QuestionCategory generateQuestionCategory(String title) {
        QuestionCategory questionCategory = new QuestionCategory();
        questionCategory.setTitle(title);
        return questionCategory;
    }

    private Topic generateTopic(String title) {
        Topic topic = new Topic();
        topic.setTitle(title);
        return topic;
    }

    private void setQuestionProfiles() {
        for (int i = 0; i < 5; i++) {
            List<Integer> correctAnswerIds = i % 2 == 0 ? Arrays.asList(1, 2)
                    : Collections.singletonList(1);
            QuestionProfile questionProfile = generateQuestion(correctAnswerIds);
            createTest.addQuestion(questionProfile);
        }
    }

    private QuestionProfile generateQuestion(List<Integer> correctAnswersIds) {
        return QuestionProfile.builder()
                .answerTitles(Arrays.asList("first", "second", "third", "fourth"))
                .categoryTitle("questionCategory")
                .description("questionDescription")
                .title("questionTitle")
                .value(5)
                .correctAnswerIndexes(correctAnswersIds)
                .build();
    }

    private Subscription generateSubscription() {
        return Subscription.builder()
                .expirationDate(LocalDate.of(2022, 1, 31))
                .build();
    }

    private Group generateGroupWithId(Long id) {
        Group group = new Group();
        group.setId(id);
        group.setEndDate(LocalDate.of(2022, 1, 31));
        return group;
    }

    private Group generateGroup() {
        Group group = new Group();
        group.setStartDate(LocalDate.now().minusDays(5));
        group.setEndDate(LocalDate.now().plusDays(5));
        return group;
    }

    private CreateTest generateCreateTest() {
        CreateTest createTest = new CreateTest();
        createTest.setDuration(23);
        createTest.setDifficulty(5);
        createTest.setTitle("testTitle");
        createTest.setTopicTitle("cats");
        createTest.setQuestions(new ArrayList<>());
        createTest.setDescription("testDescription");
        return createTest;
    }

    private User generateUser() {
        User user = new User();
        user.setId(USER_ID);
        return user;
    }

    private QuestionType generateQuestionType(String type) {
        QuestionType questionType = new QuestionType();
        questionType.setTitle(type);
        return questionType;
    }

    private com.softserve.teachua.model.test.Test generateTestWithId(Long id) {
        com.softserve.teachua.model.test.Test test = mapper.map(createTest, com.softserve.teachua.model.test.Test.class);
        test.setId(id);
        return test;
    }

    private PassTest generatePassTest() {
        PassTest passTest = new PassTest();
        passTest.setTitle("passTest");
        return passTest;
    }
}
