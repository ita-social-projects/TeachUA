package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.question.QuestionProfile;
import com.softserve.teachua.dto.question.QuestionResponse;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.DatabaseRepositoryException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Question;
import com.softserve.teachua.repository.QuestionRepository;
import com.softserve.teachua.service.ArchiveService;
import com.softserve.teachua.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
public class QuestionServiceImpl implements QuestionService {
    private static final String QUESTION_NOT_FOUND_BY_ID = "Question not found by id: %s";
    private static final String QUESTION_ALREADY_EXIST = "Question already exist with name: %s";
    private static final String QUESTION_DELETING_ERROR = "Can't delete question cause of relationship";

    private final QuestionRepository questionRepository;
    private final DtoConverter dtoConverter;
    private final ArchiveService archiveService;

    @Autowired
    QuestionServiceImpl(QuestionRepository questionRepository, DtoConverter dtoConverter, ArchiveService archiveService) {
        this.questionRepository = questionRepository;
        this.dtoConverter = dtoConverter;
        this.archiveService = archiveService;
    }

    /**
     * Method find {@link Question}
     *
     * @param id - place question id
     * @return Question
     */
    @Override
    public Question getQuestionById(Long id) {
        Optional<Question> optionalQuestion = getOptionalQuestionById(id);
        if (!optionalQuestion.isPresent()) {
            throw new NotExistException(String.format(QUESTION_NOT_FOUND_BY_ID, id));
        }

        Question question = optionalQuestion.get();
        log.info("get question by id - " + question);
        return question;
    }

    /**
     * The method returns dto {@code QuestionResponse} if question successfully added.
     *
     * @param questionProfile - place dto with all params.
     * @return new {@code QuestionResponse}.
     * @throws AlreadyExistException if question already exists.
     */
    @Override
    public QuestionResponse addQuestion(QuestionProfile questionProfile) {
        if (isQuestionExistByTitle(questionProfile.getTitle())) {
            throw new AlreadyExistException(String.format(QUESTION_ALREADY_EXIST, questionProfile.getTitle()));
        }

        Question question = questionRepository.save(dtoConverter.convertToEntity(questionProfile, new Question()));
        log.info("**/adding new question = " + question);
        return dtoConverter.convertToDto(question, QuestionResponse.class);
    }

    /**
     * The method returns dto {@code QuestionProfile} of updated question.
     *
     * @param id              - put question id
     * @param questionProfile - place body of dto {@code QuestionProfile}.
     * @return new {@code QuestionProfile}.
     */
    @Override
    public QuestionProfile updateQuestionById(Long id, QuestionProfile questionProfile) {
        Question question = getQuestionById(id);
        Question newQuestion = dtoConverter.convertToEntity(questionProfile, question)
                .withId(id);
        log.info("**/updating question by id = " + newQuestion);

        return dtoConverter.convertToDto(questionRepository.save(newQuestion), QuestionProfile.class);
    }

    /**
     * The method returns dto {@code QuestionProfile} of deleted question by id.
     *
     * @param id - put question id
     * @return new {@code QuestionProfile}.
     * @throws DatabaseRepositoryException if question contain foreign keys.
     */
    @Override
    public QuestionProfile deleteQuestionById(Long id) {
        Question deletedQuestion = getQuestionById(id);

        archiveService.saveModel(deletedQuestion);

        try {
            questionRepository.deleteById(id);
            questionRepository.flush();
        } catch (DataAccessException | ValidationException e) {
            throw new DatabaseRepositoryException(QUESTION_DELETING_ERROR);
        }

        log.info("question {} was successfully deleted", deletedQuestion);
        return dtoConverter.convertToDto(deletedQuestion, QuestionProfile.class);
    }

    /**
     * The method returns list of dto {@code List<QuestionResponse>} of all questions.
     *
     * @return new {@code List<QuestionResponse>}.
     */
    @Override
    public List<QuestionResponse> getAllQuestions() {
        List<QuestionResponse> questionResponses = questionRepository.findAll()
                .stream()
                .map(question -> (QuestionResponse) dtoConverter.convertToDto(question, QuestionResponse.class))
                .collect(Collectors.toList());

        log.info("**/getting list of questions = " + questionResponses);

        return questionResponses;
    }

    private Optional<Question> getOptionalQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    private boolean isQuestionExistByTitle(String title) {
        return questionRepository.existsByTitle(title);
    }
}
