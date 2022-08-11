package com.softserve.teachua.service.test.impl;

import com.softserve.teachua.controller.test.ResultController;
import com.softserve.teachua.dto.test.answer.ResultAnswer;
import com.softserve.teachua.dto.test.question.QuestionResult;
import com.softserve.teachua.dto.test.result.CreateResult;
import com.softserve.teachua.dto.test.result.SuccessCreatedResult;
import com.softserve.teachua.dto.test.result.UserResult;
import com.softserve.teachua.dto.test.test.ResultTest;
import com.softserve.teachua.model.User;
import com.softserve.teachua.model.test.*;
import com.softserve.teachua.repository.test.ResultRepository;
import com.softserve.teachua.service.UserService;
import com.softserve.teachua.service.test.AnswerService;
import com.softserve.teachua.service.test.QuestionService;
import com.softserve.teachua.service.test.ResultService;
import com.softserve.teachua.service.test.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import static com.softserve.teachua.utils.test.NullValidator.*;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class ResultServiceImpl implements ResultService {
    private static final String CORRECT_MESSAGE = "Your answer is correct";
    private static final String INCORRECT_MESSAGE = "Your answer is incorrect";
    private static final String PARTIALLY_CORRECT_MESSAGE = "Your answer is partially correct";

    private final ResultRepository resultRepository;
    private final TestService testService;
    private final ModelMapper modelMapper;
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;

    @Override
    @Transactional(readOnly = true)
    public List<Result> findResultsByUserId(Long userId) {
        checkNull(userId, "User");
        return resultRepository.findResultsByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public ResultTest getResultTest(Long resultId) {
        checkNullIds(resultId, "Result id");
        ResultTest resultTest = new ResultTest();
        Result result = findById(resultId);
        Long testId = result.getTest().getId();
        Test test = testService.findById(testId);
        List<Question> questions = questionService.findQuestionsByTestIdEager(testId);
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
                ResultAnswer resultAnswer = new ResultAnswer();
                boolean correct = answer.isCorrect();
                boolean selected = false;
                int answerValue = answer.getValue();

                if (correct)
                    correctAmount++;
                if (selectedAnswerIds.contains(answer.getId())) {
                    selected = true;
                    if (correct) {
                        value += answerValue;
                        correctSelectedAmount++;
                    } else {
                        value -= answerValue;
                    }
                }
                resultAnswer.setTitle(answer.getText());
                resultAnswer.setCorrect(correct);
                resultAnswer.setChecked(selected);
                questionResult.add(resultAnswer);
            }
            questionResult.setTitle(question.getTitle());
            questionResult.setValue(Math.max(value, 0));

            if (correctSelectedAmount == correctAmount
                    && correctAmount == selectedAnswerIds.size()) {
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

    @Override
    public SuccessCreatedResult saveResult(CreateResult resultDto) {
        User user = userService.getCurrentUser();
        Result result = new Result();
        result.setUser(user);
        result.setTest(testService.findById(resultDto.getTestId()));
        result.setTestFinishTime(LocalDateTime.now());
        List<Long> answerIds = resultDto.getSelectedAnswersIds();
        // TODO set start time time

        List<Answer> selectedAnswers = answerService.findAllById(answerIds);
        createResult(result, selectedAnswers);
        result.setGrade(countGrade(selectedAnswers));

        SuccessCreatedResult success = new SuccessCreatedResult();
        success.setSelectedAnswersIds(answerIds);
        success.setTestId(resultDto.getTestId());
        success.setUserId(user.getId());
        success.setGrade(result.getGrade());
        return success;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResult> findUserResultsByGroupIdAndUserId(Long groupId, Long userId) {
        checkNullIds(groupId, userId);
        List<Result> userResults = findResultsByUserId(userId);
        List<Long> groupTestsIds = testService.findAllByGroupId(groupId).stream()
                .map(Test::getId)
                .collect(Collectors.toList());
        List<Result> results = userResults.stream()
                .filter(result -> groupTestsIds.contains(result.getTest().getId()))
                .collect(Collectors.toList());
        List<UserResult> dtoResults = mapToDtoList(results);
        setResultLink(dtoResults);
        return dtoResults;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResult> findUserResultsByGroupIdAndUserIdAndTestId(Long groupId, Long userId, Long testId) {
        checkNullIds(groupId, userId, testId);
        List<Result> userResults = findResultsByUserId(userId);
        List<Result> results = userResults.stream()
                .filter(result -> result.getTest().getId().equals(testId))
                .collect(Collectors.toList());
        List<UserResult> dtoResults = mapToDtoList(results);
        setResultLink(dtoResults);
        return dtoResults;
    }

    @Override
    @Transactional(readOnly = true)
    public Result findById(Long id) {
        checkNull(id, "Result id");
        return resultRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        String.format("There is no result with id '%s'", id)));
    }

    @Override
    public void createResult(Result result, List<Answer> selectedAnswers) {
        for (Answer a : selectedAnswers) {
            QuestionHistory questionHistory = new QuestionHistory();
            questionHistory.setAnswer(a);
            result.addQuestionHistory(questionHistory);
        }
        resultRepository.save(result);
    }

    @Override
    @Transactional(readOnly = true)
    public int countGrade(List<Answer> selectedAnswers) {
        int grade = 0;
        Map<Question, List<Answer>> idAnswers = selectedAnswers
                .stream()
                .collect(Collectors.groupingBy(Answer::getQuestion));

        for (Map.Entry<Question, List<Answer>> pair : idAnswers.entrySet()) {
            Question question = pair.getKey();
            List<Answer> answers = pair.getValue();
            int questionGrade = 0;

            if (question.getQuestionType().getTitle().equals("radio")) {
                for (Answer a : answers) {
                    if (a.isCorrect() && selectedAnswers.contains(a)) {
                        questionGrade += a.getValue();
                    }
                }
            } else if (question.getQuestionType().getTitle().equals("checkbox")) {
                for (Answer a : answers) {
                    boolean answerIsCorrect = a.isCorrect();
                    int value = a.getValue();
                    questionGrade += answerIsCorrect ? value : -value;
                }
            }
            grade += Math.max(questionGrade, 0);
        }
        return grade;
    }

    private void setResultLink(List<UserResult> results) {
        for (UserResult result : results) {
            Link link = linkTo(methodOn(ResultController.class)
                    .getTestResult(result.getId()))
                    .withSelfRel();
            result.add(link);
        }
    }

    private List<UserResult> mapToDtoList(List<Result> results) {
        return results.stream()
                .map(result -> modelMapper.map(result, UserResult.class))
                .collect(Collectors.toList());
    }
}
