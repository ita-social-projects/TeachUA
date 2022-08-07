package com.softserve.teachua.service.test.impl;

import com.softserve.teachua.dto.test.question.QuestionResponse;
import com.softserve.teachua.model.test.Answer;
import com.softserve.teachua.model.test.Question;
import com.softserve.teachua.model.test.Test;
import com.softserve.teachua.repository.test.QuestionRepository;
import com.softserve.teachua.service.test.AnswerService;
import com.softserve.teachua.service.test.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.softserve.teachua.utils.NullValidator.checkNull;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service("testQuestionService")
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final ModelMapper modelMapper;
    private final AnswerService answerService;

    @Override
    public List<Question> findQuestionsByTestId(Long id) {
        checkNull(id, "Test id");
        return questionRepository.findQuestionsByTestId(id);
    }

    @Override
    public List<Question> findQuestionsByTest(Test test) {
        checkNull(test, "Test");
        return findQuestionsByTestId(test.getId());
    }

    @Override
    public List<QuestionResponse> findQuestionResponsesByTestId(Long id) {
        checkNull(id, "Test id");
        return mapToDtoList(questionRepository.findQuestionsByTestId(id));
    }

    @Override
    public List<QuestionResponse> findQuestionResponsesByTest(Test test) {
        checkNull(test, "Test");
        return findQuestionResponsesByTestId(test.getId());
    }

    @Override
    public Question save(Question question) {
        checkNull(question, "Question");
        return questionRepository.save(question);
    }

    private List<QuestionResponse> mapToDtoList(List<Question> questions) {
        List<QuestionResponse> questionsResponses = questions.stream()
                        .map(question -> modelMapper.map(question, QuestionResponse.class))
                        .collect(Collectors.toList());

        for (int i = 0; i < questions.size(); i++) {
            List<Answer> answers = answerService.findByQuestionId(questions.get(i).getId());
            List<String> answerTitles = answers.stream()
                    .map(Answer::getText)
                    .collect(Collectors.toList());
            questionsResponses.get(i).setAnswerTitles(answerTitles);
        }
        return questionsResponses;
    }
}
