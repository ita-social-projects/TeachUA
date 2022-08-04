package com.softserve.teachua.service.test.impl;

import com.softserve.teachua.model.User;
import com.softserve.teachua.model.test.*;
import com.softserve.teachua.repository.test.ResultRepository;
import com.softserve.teachua.service.test.AnswerService;
import com.softserve.teachua.service.test.ResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class ResultServiceImpl implements ResultService {
    private final ResultRepository resultRepository;
    private final AnswerService answerService;

    public List<Result> findResultsByTest(Test test) {
        return resultRepository.findResultsByTest(test);
    }
    public List<Result> findResultsByUser(User user) {
        return resultRepository.findResultsByUser(user);
    }

    @Override
    public Result findById(Long id) {
        return resultRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        String.format("There is no result with id '%s'", id)
                ));
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
    public List<Result> findByUser(User user) {
        return resultRepository.findResultsByUser(user);
    }

    @Override
    public int countGrade(List<Answer> selectedAnswers) {
        int grade = 0;
        Map<Question, List<Answer>> idAnswers = selectedAnswers.stream()
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
}
