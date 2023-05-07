package com.softserve.teachua.service.test.impl;

import com.softserve.teachua.dto.test.answer.answerExcel.AnswerExcel;
import com.softserve.teachua.dto.test.question.QuestionDatabaseResponse;
import com.softserve.teachua.dto.test.question.questionExcel.QuestionDataRequest;
import com.softserve.teachua.dto.test.question.questionExcel.QuestionExcel;
import com.softserve.teachua.model.test.Answer;
import com.softserve.teachua.model.test.Question;
import com.softserve.teachua.model.test.QuestionCategory;
import com.softserve.teachua.model.test.QuestionType;
import com.softserve.teachua.repository.test.AnswerRepository;
import com.softserve.teachua.repository.test.QuestionCategoryRepository;
import com.softserve.teachua.repository.test.QuestionRepository;
import com.softserve.teachua.repository.test.QuestionTypeRepository;
import com.softserve.teachua.service.UserService;
import com.softserve.teachua.service.test.QuestionDataLoaderService;
import com.softserve.teachua.service.test.QuestionService;
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
    private final UserService userService;

    private final QuestionRepository questionRepository;
    private final QuestionCategoryRepository categoryRepository;
    private final AnswerRepository answerRepository;
    private final QuestionTypeRepository typeRepository;

    @Autowired
    public QuestionDataLoaderServiceImpl(QuestionService questionService,
                                         QuestionRepository questionRepository,
                                         QuestionCategoryRepository categoryRepository,
                                         QuestionTypeRepository typeRepository,
                                         UserService userService,
                                         AnswerRepository answerRepository) {
        this.questionService = questionService;

        this.questionRepository = questionRepository;
        this.categoryRepository = categoryRepository;
        this.typeRepository = typeRepository;
        this.userService = userService;
        this.answerRepository = answerRepository;
    }

    @Override
    public List<QuestionDatabaseResponse> saveToDatabase(QuestionDataRequest data, Long creatorId) {
        List<QuestionDatabaseResponse> responses = new ArrayList<>();
        for (QuestionExcel questionExcel : data.getQuestionsExcelList()) {
            Question question = Question.builder()
                    .title(questionExcel.getTitle())
                    .description(questionExcel.getDescription())
                    .creator(userService.getUserById(creatorId)).build();

            if (categoryRepository.findByTitle(questionExcel.getCategory()).isEmpty()) {
                categoryRepository.save(QuestionCategory.builder().title(questionExcel.getCategory()).build());
            }
            question.setQuestionCategory(categoryRepository.findByTitle(questionExcel.getCategory()).get());

            if (typeRepository.findByTitle(questionExcel.getType().toUpperCase()).isEmpty()) {
                typeRepository.save(QuestionType.builder().title(questionExcel.getType().toUpperCase()).build());
            }
            question.setQuestionType(typeRepository.findByTitle(questionExcel.getType().toUpperCase()).get());

            Optional<Question> questionFromDB = questionRepository.findByTitle(question.getTitle());
            if (questionFromDB.isPresent()) {
                if (questionFromDB.get().equals(question)) {
                    responses.add(new QuestionDatabaseResponse(String.format(QUESTION_ALREADY_EXISTS, question)));
                } else {
                    questionService.save(question);
                }
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

            List<Answer> answerList = answerRepository.findAllByQuestionId(question.get().getId());
            if (answerList.contains(answer)) {
                responses.add(
                        new QuestionDatabaseResponse(
                                String.format(ANSWERS_ALREADY_EXISTS, answer.getText(), question.get().getTitle())));
            } else {
                answerRepository.save(answer);
            }
        }
        return responses;
    }
}
