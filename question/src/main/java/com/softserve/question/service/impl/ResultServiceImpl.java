package com.softserve.question.service.impl;

import com.softserve.commons.exception.NotExistException;
import com.softserve.question.controller.ResultController;
import com.softserve.question.dto.answer.AnswerResult;
import com.softserve.question.dto.question.QuestionResult;
import com.softserve.question.dto.result.CreateResult;
import com.softserve.question.dto.result.SuccessCreatedResult;
import com.softserve.question.dto.result.UserResult;
import com.softserve.question.dto.test.ResultTest;
import com.softserve.question.model.Answer;
import com.softserve.question.model.Question;
import com.softserve.question.model.QuestionHistory;
import com.softserve.question.model.Result;
import com.softserve.question.model.Test;
import com.softserve.question.repository.ResultRepository;
import com.softserve.question.service.AnswerService;
import com.softserve.question.service.QuestionService;
import com.softserve.question.service.ResultService;
import com.softserve.question.service.TestService;
import com.softserve.question.util.containers.AnswerResultContainer;
import com.softserve.question.util.containers.QuestionResultContainer;
import static com.softserve.question.util.validation.NullValidator.checkNull;
import static com.softserve.question.util.validation.NullValidator.checkNullIds;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Link;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class ResultServiceImpl implements ResultService {
    private final ResultRepository resultRepository;
    private final TestService testService;
    private final ModelMapper modelMapper;
    private final QuestionService questionService;
    private final AnswerService answerService;

    @Override
    @Transactional(readOnly = true)
    public Result findById(Long id) {
        checkNull(id, "Result id");
        return resultRepository.findById(id)
                .orElseThrow(() -> new NotExistException(
                        String.format("There is no result with id '%s'", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResult> findUserResultsByGroupIdAndUserId(Long groupId, Long userId) {
        checkNullIds(groupId, userId);
        List<Result> userResults = findResultsByUserId(userId);
        List<Long> groupTestsIds = testService.findAllByGroupId(groupId).stream()
                .map(Test::getId)
                .toList();
        List<Result> results = userResults.stream()
                .filter(result -> groupTestsIds.contains(result.getTest().getId()))
                .toList();
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
                .toList();
        List<UserResult> dtoResults = mapToDtoList(results);
        setResultLink(dtoResults);
        return dtoResults;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Result> findResultsByUserId(Long userId) {
        checkNull(userId, "User");
        return resultRepository.findResultsByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public ResultTest getDetailedResultById(Long resultId) {
        checkNull(resultId, "Result id");
        ResultTest resultTest = new ResultTest();
        Result result = findById(resultId);
        Test test = result.getTest();
        List<Question> questions = questionService.findQuestionsByTestIdEager(test.getId());
        Set<QuestionHistory> questionHistories = result.getQuestionHistories();
        Map<Long, Set<Long>> questionSelectedAnswers = new HashMap<>();
        questions.forEach(x -> questionSelectedAnswers.put(x.getId(), new HashSet<>()));
        questionHistories.stream()
                .map(QuestionHistory::getAnswer)
                .forEach(x -> questionSelectedAnswers.get(x.getQuestion().getId()).add(x.getId()));
        generateResultTestBody(questions, resultTest, questionSelectedAnswers);
        resultTest.setTitle(test.getTitle());
        return resultTest;
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
            int questionGrade = getQuestionGrade(selectedAnswers, question, answers);
            grade += Math.max(questionGrade, 0);
        }
        return grade;
    }

    private int getQuestionGrade(List<Answer> selectedAnswers, Question question, List<Answer> answers) {
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
        return questionGrade;
    }

    @Override
    public SuccessCreatedResult saveResult(CreateResult resultDto) {
        checkNull(resultDto, "Create result dto");
        //todo
        //User user = userService.getAuthenticatedUser();
        Result result = new Result();
        //result.setUser(user);
        result.setTest(testService.findById(resultDto.getTestId()));
        result.setTestStartTime(resultDto.getStartTime());
        result.setTestFinishTime(LocalDateTime.now());
        List<Long> answerIds = resultDto.getSelectedAnswersIds();

        List<Answer> selectedAnswers = answerService.findAllById(answerIds);
        createResult(result, selectedAnswers);
        result.setGrade(countGrade(selectedAnswers));

        SuccessCreatedResult successResult = new SuccessCreatedResult();
        successResult.setSelectedAnswersIds(answerIds);
        successResult.setTestId(resultDto.getTestId());
        //successResult.setUserId(user.getId());
        successResult.setGrade(result.getGrade());
        log.info("**/Result has been saved. {}", successResult);
        return successResult;
    }

    private void createResult(Result result, List<Answer> selectedAnswers) {
        for (Answer a : selectedAnswers) {
            QuestionHistory questionHistory = new QuestionHistory();
            questionHistory.setAnswer(a);
            result.addQuestionHistory(questionHistory);
        }
        resultRepository.save(result);
    }

    private void generateResultTestBody(List<Question> questions, ResultTest resultTest,
                                        Map<Long, Set<Long>> questionSelectedAnswers) {
        for (Question question : questions) {
            QuestionResult questionResult = new QuestionResult();
            Set<Answer> answers = question.getAnswers();
            Set<Long> selectedAnswerIds = questionSelectedAnswers.get(question.getId());
            QuestionResultContainer questionResultContainer = new QuestionResultContainer();

            for (Answer answer : answers) {
                AnswerResult answerResult = new AnswerResult();
                AnswerResultContainer answerResultContainer = new AnswerResultContainer(answer);
                answerResultContainer.calculateGrade(questionResultContainer, selectedAnswerIds);
                answerResult.setTitle(answer.getText());
                answerResult.setCorrect(answerResultContainer.isCorrect());
                answerResult.setChecked(answerResultContainer.isSelected());
                questionResult.add(answerResult);
            }
            questionResult.setTitle(question.getTitle());
            questionResult.setValue(questionResultContainer.getGrade());
            resultTest.addQuestion(questionResult);
            questionResultContainer.setQuestionResultStatus(questionResult, selectedAnswerIds);
        }
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
                .toList();
    }
}
