package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.commons.exception.AlreadyExistException;
import com.softserve.commons.exception.DatabaseRepositoryException;
import com.softserve.commons.exception.NotExistException;
import com.softserve.commons.util.converter.DtoConverter;
import com.softserve.teachua.dto.question.QuestionProfile;
import com.softserve.teachua.dto.question.QuestionResponse;
import com.softserve.teachua.model.Question;
import com.softserve.teachua.repository.QuestionRepository;
import com.softserve.teachua.service.ArchiveService;
import com.softserve.teachua.service.QuestionService;
import jakarta.validation.ValidationException;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
public class QuestionServiceImpl implements QuestionService/*, ArchiveMark<Question>*/ {
    private static final String QUESTION_NOT_FOUND_BY_ID = "Question not found by id: %s";
    private static final String QUESTION_ALREADY_EXIST = "Question already exist with name: %s";
    private static final String QUESTION_DELETING_ERROR = "Can't delete question cause of relationship";

    private final QuestionRepository questionRepository;
    private final DtoConverter dtoConverter;
    private final ArchiveService archiveService;
    private final ObjectMapper objectMapper;

    @Autowired
    QuestionServiceImpl(QuestionRepository questionRepository, DtoConverter dtoConverter, ArchiveService archiveService,
            ObjectMapper objectMapper) {
        this.questionRepository = questionRepository;
        this.dtoConverter = dtoConverter;
        this.archiveService = archiveService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Question getQuestionById(Long id) {
        Optional<Question> optionalQuestion = getOptionalQuestionById(id);
        if (optionalQuestion.isEmpty()) {
            throw new NotExistException(String.format(QUESTION_NOT_FOUND_BY_ID, id));
        }

        Question question = optionalQuestion.get();
        log.debug("get question by id - " + question);
        return question;
    }

    @Override
    public QuestionResponse addQuestion(QuestionProfile questionProfile) {
        if (isQuestionExistByTitle(questionProfile.getTitle())) {
            throw new AlreadyExistException(String.format(QUESTION_ALREADY_EXIST, questionProfile.getTitle()));
        }

        Question question = questionRepository.save(dtoConverter.convertToEntity(questionProfile, new Question()));
        log.debug("**/adding new question = " + question);
        return dtoConverter.convertToDto(question, QuestionResponse.class);
    }

    @Override
    public QuestionProfile updateQuestionById(Long id, QuestionProfile questionProfile) {
        Question question = getQuestionById(id);
        Question newQuestion = dtoConverter.convertToEntity(questionProfile, question).withId(id);
        log.debug("**/updating question by id = " + newQuestion);

        return dtoConverter.convertToDto(questionRepository.save(newQuestion), QuestionProfile.class);
    }

    @Override
    public QuestionProfile deleteQuestionById(Long id) {
        Question deletedQuestion = getQuestionById(id);

        try {
            questionRepository.deleteById(id);
            questionRepository.flush();
        } catch (DataAccessException | ValidationException e) {
            throw new DatabaseRepositoryException(QUESTION_DELETING_ERROR);
        }

        //archiveModel(deletedQuestion);

        log.debug("question {} was successfully deleted", deletedQuestion);
        return dtoConverter.convertToDto(deletedQuestion, QuestionProfile.class);
    }

    @Override
    public List<QuestionResponse> getAllQuestions() {
        List<QuestionResponse> questionResponses = questionRepository.findAll().stream()
                .map(question -> (QuestionResponse) dtoConverter.convertToDto(question, QuestionResponse.class))
                .toList();

        log.debug("**/getting list of questions = " + questionResponses);

        return questionResponses;
    }

    private Optional<Question> getOptionalQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    private boolean isQuestionExistByTitle(String title) {
        return questionRepository.existsByTitle(title);
    }

    //todo@
    /*
    @Override
    public void archiveModel(Question question) {
        QuestionArch questionArch = dtoConverter.convertToDto(question, QuestionArch.class);
        archiveService.saveModel(questionArch);
    }

    @Override
    public void restoreModel(String archiveObject) throws JsonProcessingException {
        QuestionArch questionArch = objectMapper.readValue(archiveObject, QuestionArch.class);
        questionRepository.save(dtoConverter.convertToEntity(questionArch, Question.builder().build()));
    }
    */
}
