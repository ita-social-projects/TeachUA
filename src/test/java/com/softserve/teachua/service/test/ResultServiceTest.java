package com.softserve.teachua.service.test;

import com.softserve.teachua.dto.test.answer.AnswerResult;
import com.softserve.teachua.dto.test.question.QuestionResult;
import com.softserve.teachua.dto.test.result.CreateResult;
import com.softserve.teachua.dto.test.result.SuccessCreatedResult;
import com.softserve.teachua.dto.test.result.UserResult;
import com.softserve.teachua.dto.test.test.ResultTest;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.User;
import com.softserve.teachua.model.test.*;
import com.softserve.teachua.repository.test.ResultRepository;
import com.softserve.teachua.service.UserService;
import com.softserve.teachua.service.test.impl.ResultServiceImpl;
import com.softserve.teachua.utils.test.AnswerComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static com.softserve.teachua.utils.test.Messages.*;

@ExtendWith(MockitoExtension.class)
public class ResultServiceTest {
    @Mock
    private ResultRepository resultRepository;
    @Mock
    private TestService testService;
    @Mock
    private QuestionService questionService;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private AnswerService answerService;
    @Mock
    private UserService userService;

    @InjectMocks
    private ResultServiceImpl resultService;

    private final Long EXISTING_RESULT_ID = 1L;
    private final Long NOT_EXISTING_RESULT_ID = 100L;

    private final Long USER_ID = 1L;
    private final Long GROUP_ID = 1L;
    private final Long TEST_ID = 1L;

    private final ModelMapper mapper = new ModelMapper();
    private List<Question> questions;
    private List<Answer> selectedAnswers;
    private List<Long> selectedAnswersIds;
    private List<QuestionHistory> questionHistories;
    private com.softserve.teachua.model.test.Test test;
    private UserResult userResult;
    private SuccessCreatedResult successCreatedResult;
    private ResultTest resultTest;
    private CreateResult createResult;
    private Result result;
    private User user;

    @BeforeEach
    void setUp() {
        result = generateResult();
        test = generateTest();
        questions = generateQuestionsList();
        userResult = mapper.map(result, UserResult.class);
        questionHistories = getQuestionHistoriesList();
        selectedAnswers = generateSelectedAnswersList();
        selectedAnswersIds = selectedAnswers.stream()
                .map(Answer::getId)
                .collect(Collectors.toList());
        questionHistories.forEach(result::addQuestionHistory);
        resultTest = generateResultTest();
        successCreatedResult = generateSuccessCreatedResult();
        createResult = generateCreateResult();
        user = generateUser();
    }

    @Test
    void findResultByExistingResultIdShouldReturnResult() {
        when(resultRepository.findById(EXISTING_RESULT_ID)).thenReturn(Optional.of(result));
        assertEquals(result, resultService.findById(EXISTING_RESULT_ID));
    }

    @Test
    void findResultByNotExistingResultIdShouldThrowNotExistException() {
        when(resultRepository.findById(NOT_EXISTING_RESULT_ID)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> resultService.findById(NOT_EXISTING_RESULT_ID))
                .isInstanceOf(NotExistException.class);
    }

    @Test
    void findResultByResultIdIsNullShouldThrowIllegalArgumentException() {
        assertThatThrownBy(() -> resultService.findById(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findUserResultsByGroupIdAndUserIdShouldReturnListOfUserResults() {
        when(resultRepository.findResultsByUserId(USER_ID)).thenReturn(Collections.singletonList(result));
        when(testService.findAllByGroupId(GROUP_ID)).thenReturn(Collections.singletonList(test));
        when(modelMapper.map(result, UserResult.class)).thenReturn(userResult);

        List<UserResult> actual = resultService
                .findUserResultsByGroupIdAndUserId(USER_ID, GROUP_ID);
        assertEquals(userResult, actual.get(0));
    }

    @Test
    void findUserResultsByGroupIdIsNullAndUserIdIsNullShouldThrowIllegalArgumentException() {
        assertThatThrownBy(() -> resultService
                .findUserResultsByGroupIdAndUserId(null, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findUserResultsByGroupIdAndUserIdAndTestIdShouldReturnListOfUserResults() {
        when(resultRepository.findResultsByUserId(USER_ID)).thenReturn(Collections.singletonList(result));
        when(modelMapper.map(result, UserResult.class)).thenReturn(userResult);

        List<UserResult> actual = resultService
                .findUserResultsByGroupIdAndUserIdAndTestId(GROUP_ID, USER_ID, TEST_ID);
        assertEquals(userResult, actual.get(0));
    }

    @Test
    void findUserResultsByGroupIdIsNullAndUserIdIsNullAndTestIdIsNullShouldThrowIllegalArgumentException() {
        assertThatThrownBy(() -> resultService
                .findUserResultsByGroupIdAndUserIdAndTestId(null, null, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findResultsByUserIdShouldReturnListOfUserResults() {
        when(resultRepository.findResultsByUserId(USER_ID)).thenReturn(Collections.singletonList(result));
        assertEquals(result, resultService.findResultsByUserId(USER_ID).get(0));
    }

    @Test
    void findResultsByUserIdNullShouldThrowIllegalArgumentException() {
        assertThatThrownBy(() -> resultService
                .findResultsByUserId(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getDetailedResultByExistingResultIdShouldReturnResultTest() {
        when(resultRepository.findById(EXISTING_RESULT_ID)).thenReturn(Optional.of(result));
        when(questionService.findQuestionsByTestIdEager(TEST_ID)).thenReturn(questions);

        ResultTest actual = resultService.getDetailedResultById(EXISTING_RESULT_ID);
        assertEquals(resultTest, actual);
    }

    @Test
    void getDetailedResultByNotExistingResultIdShouldThrowNotExistException() {
        when(resultRepository.findById(NOT_EXISTING_RESULT_ID)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> resultService.getDetailedResultById(NOT_EXISTING_RESULT_ID))
                .isInstanceOf(NotExistException.class);
    }

    @Test
    void getDetailedResultByResultIdIsNullIdShouldThrowIllegalArgumentException() {
        assertThatThrownBy(() -> resultService.getDetailedResultById(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void countGradeShouldReturn2() {
        assertEquals(2, resultService.countGrade(selectedAnswers));
    }

    @Test
    void saveCorrectResultShouldReturnSuccessCreatedResult() {
        when(userService.getCurrentUser()).thenReturn(user);
        when(testService.findById(TEST_ID)).thenReturn(test);
        when(answerService.findAllById(selectedAnswersIds)).thenReturn(selectedAnswers);
        when(resultRepository.save(any())).thenReturn(result);
        assertEquals(successCreatedResult, resultService.saveResult(createResult));
    }

    @Test
    void saveResultWithResultDtoIsNullShouldThrowIllegalArgumentException() {
        assertThatThrownBy(() -> resultService.saveResult(null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    private List<Answer> generateSelectedAnswersList() {
        List<Answer> answers = new ArrayList<>();
        questionHistories.forEach(history -> answers.add(history.getAnswer()));
        return answers;
    }

    private List<QuestionHistory> getQuestionHistoriesList() {
        List<QuestionHistory> questionHistories = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            TreeSet<Answer> answers = new TreeSet<>(new AnswerComparator());
            answers.addAll(question.getAnswers());
            Answer answer = i == 0 ? answers.first() : answers.last();
            QuestionHistory questionHistory = generateQuestionHistoryWithId((long) i, answer);
            questionHistories.add(questionHistory);
        }
        return questionHistories;
    }

    private List<Answer> generateAnswersList(Long startId) {
        List<Answer> answers = new ArrayList<>();
        for (long i = startId; i < startId + 3; i++) {
            answers.add(generateAnswerWithId(i, i % 3 == 0));
        }
        return answers;
    }

    private List<Question> generateQuestionsList() {
        List<Question> questions = new ArrayList<>();
        for (long i = 0, k = 1; i < 2; i++, k+=3) {
            String type = i == 1 ? "radio" : "checkbox";
            questions.add(generateQuestionWithId(i, k, type));
        }
        return questions;
    }

    private CreateResult generateCreateResult() {
        CreateResult createResult = new CreateResult();
        createResult.setTestId(TEST_ID);
        createResult.setSelectedAnswersIds(selectedAnswersIds);
        createResult.setStartTime(LocalDateTime.of(2022, 10, 13, 20, 8, 0));
        return createResult;
    }

    private SuccessCreatedResult generateSuccessCreatedResult() {
        SuccessCreatedResult successResult = new SuccessCreatedResult();
        successResult.setSelectedAnswersIds(selectedAnswersIds);
        successResult.setTestId(TEST_ID);
        successResult.setUserId(USER_ID);
        successResult.setGrade(result.getGrade());
        return successResult;
    }

    private com.softserve.teachua.model.test.Test generateTest() {
        com.softserve.teachua.model.test.Test test = new com.softserve.teachua.model.test.Test();
        test.setId(TEST_ID);
        test.setDifficulty(5);
        test.setDuration(23);
        test.setTitle("testTitle");
        test.setDescription("testDescription");
        return test;
    }

    private User generateUser() {
        User user = new User();
        user.setId(USER_ID);
        return user;
    }

    private Result generateResult() {
        Result result = new Result();
        result.setId(EXISTING_RESULT_ID);
        result.setGrade(2);
        result.setUser(new User());
        result.setTest(generateTest());
        result.setTestStartTime(LocalDateTime.of(2022, 10, 13, 20, 8, 0));
        result.setTestFinishTime(LocalDateTime.now());
        return result;
    }

    private ResultTest generateResultTest() {
        ResultTest resultTest = new ResultTest();
        resultTest.setTitle(test.getTitle());

        QuestionResult fQuestionResult = new QuestionResult();
        fQuestionResult.setStatus(INCORRECT_MESSAGE);
        fQuestionResult.setTitle("questionTitle 0");
        fQuestionResult.setValue(0);
        AnswerResult fFirstAnswerResult = new AnswerResult();
        fFirstAnswerResult.setChecked(true);
        fFirstAnswerResult.setCorrect(false);
        fFirstAnswerResult.setTitle("answerTitle 1");
        AnswerResult fSecondAnswerResult = new AnswerResult();
        fSecondAnswerResult.setChecked(false);
        fSecondAnswerResult.setCorrect(false);
        fSecondAnswerResult.setTitle("answerTitle 2");
        AnswerResult fThirdAnswerResult = new AnswerResult();
        fThirdAnswerResult.setChecked(false);
        fThirdAnswerResult.setCorrect(true);
        fThirdAnswerResult.setTitle("answerTitle 3");
        fQuestionResult.add(fThirdAnswerResult);
        fQuestionResult.add(fSecondAnswerResult);
        fQuestionResult.add(fFirstAnswerResult);
        resultTest.addQuestion(fQuestionResult);

        QuestionResult sQuestionResult = new QuestionResult();
        sQuestionResult.setStatus(CORRECT_MESSAGE);
        sQuestionResult.setTitle("questionTitle 1");
        sQuestionResult.setValue(2);
        AnswerResult sFirstAnswerResult = new AnswerResult();
        sFirstAnswerResult.setChecked(false);
        sFirstAnswerResult.setCorrect(false);
        sFirstAnswerResult.setTitle("answerTitle 4");
        AnswerResult sSecondAnswerResult = new AnswerResult();
        sSecondAnswerResult.setChecked(false);
        sSecondAnswerResult.setCorrect(false);
        sSecondAnswerResult.setTitle("answerTitle 5");
        AnswerResult sThirdAnswerResult = new AnswerResult();
        sThirdAnswerResult.setChecked(true);
        sThirdAnswerResult.setCorrect(true);
        sThirdAnswerResult.setTitle("answerTitle 6");
        sQuestionResult.add(sThirdAnswerResult);
        sQuestionResult.add(sSecondAnswerResult);
        sQuestionResult.add(sFirstAnswerResult);
        resultTest.addQuestion(sQuestionResult);
        return resultTest;
    }

    private QuestionHistory generateQuestionHistoryWithId(Long id, Answer answer) {
        QuestionHistory questionHistory = new QuestionHistory();
        questionHistory.setId(id);
        questionHistory.setResult(result);
        questionHistory.setAnswer(answer);
        return questionHistory;
    }

    private Question generateQuestionWithId(Long id, Long startAnswerId, String type) {
        Question question = new Question();
        QuestionType questionType = new QuestionType();
        questionType.setTitle(type);
        question.setQuestionType(questionType);
        question.setId(id);
        question.setTitle(String.format("questionTitle %d", id));
        question.setDescription(String.format("questionDescription %d", id));
        generateAnswersList(startAnswerId).forEach(question::addAnswer);
        return question;
    }

    private Answer generateAnswerWithId(Long id, boolean correct) {
        Answer answer = new Answer();
        answer.setId(id);
        answer.setCorrect(correct);
        answer.setText(String.format("answerTitle %d", id));
        answer.setValue(2);
        return answer;
    }
}
