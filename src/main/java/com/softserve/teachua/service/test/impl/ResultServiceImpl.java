package com.softserve.teachua.service.test.impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.test.result.CreateResult;
import com.softserve.teachua.dto.test.result.SuccessCreatedResult;
import com.softserve.teachua.model.User;
import com.softserve.teachua.model.test.*;
import com.softserve.teachua.repository.test.ResultRepository;
import com.softserve.teachua.service.UserService;
import com.softserve.teachua.service.test.QuestionService;
import com.softserve.teachua.service.test.ResultService;
import com.softserve.teachua.service.test.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class ResultServiceImpl implements ResultService {
    private final ResultRepository resultRepository;
    private final TestService testService;
    private final UserService userService;
    private final QuestionService questionService;

    public List<Result> findResultsByTest(Test test) {
        return resultRepository.findResultsByTest(test);
    }

    public List<Result> findResultsByUser(User user) {
        return resultRepository.findResultsByUser(user);
    }


    @Override
    public SuccessCreatedResult addResult(CreateResult resultDto) {
        Result result = new Result();
        result.setTest(testService.findById(resultDto.getTestId()));
        result.setUser(userService.getUserById(resultDto.getUserId()));
        result.setGrade(countGrade(resultDto, questionService.findQuestionsByTestId(resultDto.getTestId())));
        // TODO set time
        List<Answer> selectedAnswers = getSelectedAnswers(resultDto);
        createQuestionHistory(result, selectedAnswers);
        return null;
    }

    private List<Answer> getSelectedAnswers(CreateResult resultDto){ // optimize
        List<Answer> selectedAnswers = new ArrayList<>();
        for (Question q: questionService.findQuestionsByTestId(resultDto.getTestId())){
            for(Answer a: q.getAnswers()){
                if(resultDto.getSelectedAnswers().contains(a.getText())){
                    selectedAnswers.add(a);
                }
            }
        }
        return selectedAnswers;
    }

    private void createQuestionHistory(Result result, List<Answer> selectedAnswers) {
        for (Answer a : selectedAnswers) {
            QuestionHistory questionHistory = new QuestionHistory();
            questionHistory.setResult(result);
            questionHistory.setAnswer(a);
        }
    }

    private int countGrade(CreateResult resultDto, List<Question> questions) {
        int grade = 0;
        for (Question q : questions) {
            grade += countGradeForQuestion(q, resultDto);
        }
        return grade;
    }

    private int countGradeForQuestion(Question q, CreateResult resultDto) {
        int gradeForQuestion = 0;
        if (q.getQuestionType().getTitle().equals("radio")) { // ask and fix
            for (Answer a : q.getAnswers()) {
                if (a.isCorrect() && resultDto.getSelectedAnswers().contains(a.getText())) {
                    gradeForQuestion += a.getValue();
                }
            }
        } else if (q.getQuestionType().getTitle().equals("checkbox")) {
            for (Answer a : q.getAnswers()) {
                if (a.isCorrect() && resultDto.getSelectedAnswers().contains(a.getText())) {
                    gradeForQuestion += a.getValue();
                } else if (a.isCorrect() && !resultDto.getSelectedAnswers().contains(a.getText())) {
                    gradeForQuestion -= a.getValue();
                } else if (!a.isCorrect() && resultDto.getSelectedAnswers().contains(a.getText())) {
                    gradeForQuestion -= a.getValue();
                }
            }
        }
        // TODO decide what to do if all options are selected
        if (gradeForQuestion < 0) gradeForQuestion = 0;
        return gradeForQuestion;
    }
}
