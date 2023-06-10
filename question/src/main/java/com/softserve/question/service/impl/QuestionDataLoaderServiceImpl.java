package com.softserve.question.service.impl;

import com.softserve.commons.exception.NotExistException;
import com.softserve.question.dto.answer.answer_excel.AnswerExcel;
import com.softserve.question.dto.question.QuestionDatabaseResponse;
import com.softserve.question.dto.question.question_excel.QuestionDataRequest;
import com.softserve.question.dto.question.question_excel.QuestionExcel;
import com.softserve.question.model.Answer;
import com.softserve.question.model.Question;
import com.softserve.question.model.QuestionCategory;
import com.softserve.question.model.QuestionType;
import com.softserve.question.repository.AnswerRepository;
import com.softserve.question.repository.QuestionCategoryRepository;
import com.softserve.question.repository.QuestionRepository;
import com.softserve.question.repository.QuestionTypeRepository;
import com.softserve.question.service.QuestionDataLoaderService;
import com.softserve.question.service.QuestionService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class QuestionDataLoaderServiceImpl implements QuestionDataLoaderService {
    private static final String QUESTION_ALREADY_EXISTS = "Запитання %s вже було згенеровано";
    private static final String ANSWERS_ALREADY_EXISTS =
            "Варіант відповіді \"%s\" для запитання \"%s\" вже було додано";
    private final QuestionService questionService;
    //private final UserService userService;

    private final QuestionRepository questionRepository;
    private final QuestionCategoryRepository categoryRepository;
    private final AnswerRepository answerRepository;
    private final QuestionTypeRepository typeRepository;

    @Autowired
    public QuestionDataLoaderServiceImpl(QuestionService questionService,
                                         QuestionRepository questionRepository,
                                         QuestionCategoryRepository categoryRepository,
                                         QuestionTypeRepository typeRepository,
                                         AnswerRepository answerRepository) {
        this.questionService = questionService;
        this.questionRepository = questionRepository;
        this.categoryRepository = categoryRepository;
        this.typeRepository = typeRepository;
        this.answerRepository = answerRepository;
    }

    @Override
    public List<QuestionDatabaseResponse> saveToDatabase(QuestionDataRequest data, Long creatorId) {
        List<QuestionDatabaseResponse> responses = new ArrayList<>();
        for (QuestionExcel questionExcel : data.getQuestionsExcelList()) {
            //todo
            Question question = Question.builder()
                    .title(questionExcel.getTitle())
                    .description(questionExcel.getDescription())
                    //.creator(userService.getUserById(creatorId))
                    .build();

            if (categoryRepository.findByTitle(questionExcel.getCategory()).isEmpty()) {
                categoryRepository.save(QuestionCategory.builder().title(questionExcel.getCategory()).build());
            }
            question.setQuestionCategory(categoryRepository.findByTitle(questionExcel.getCategory())
                    .orElseThrow(NotExistException::new));

            if (typeRepository.findByTitle(questionExcel.getType().toUpperCase()).isEmpty()) {
                typeRepository.save(QuestionType.builder().title(questionExcel.getType().toUpperCase()).build());
            }
            question.setQuestionType(typeRepository.findByTitle(questionExcel.getType().toUpperCase())
                    .orElseThrow(NotExistException::new));

            Optional<Question> questionFromDB = questionRepository.findByTitle(question.getTitle());

            if (questionFromDB.filter(q -> q.equals(question)).isPresent()) {
                responses.add(new QuestionDatabaseResponse(String.format(QUESTION_ALREADY_EXISTS, question)));
            } else {
                questionService.save(question);
            }
        }

        for (AnswerExcel answerExcel : data.getAnswersExcelList()) {
            Answer answer = Answer.builder()
                    .text(answerExcel.getText())
                    .correct(Boolean.parseBoolean(answerExcel.getIsCorrect().toLowerCase()))
                    .value(answerExcel.getValue())
                    .build();

            Optional<Question> question = questionRepository.findByTitle(answerExcel.getQuestionTitle().trim());
            question.ifPresent(answer::setQuestion);

            List<Answer> answerList = answerRepository.findAllByQuestionId(
                    question.orElseThrow(NotExistException::new).getId());
            if (answerList.contains(answer)) {
                responses.add(
                        new QuestionDatabaseResponse(
                                String.format(ANSWERS_ALREADY_EXISTS, answer.getText(),
                                        question.orElseThrow(NotExistException::new).getTitle())));
            } else {
                answerRepository.save(answer);
            }
        }
        return responses;
    }
}
