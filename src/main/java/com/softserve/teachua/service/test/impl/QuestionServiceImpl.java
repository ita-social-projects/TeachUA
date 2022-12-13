package com.softserve.teachua.service.test.impl;

import com.google.api.services.forms.v1.Forms;
import com.google.api.services.forms.v1.model.*;
import com.softserve.teachua.dto.test.question.QuestionPreview;
import com.softserve.teachua.dto.test.question.QuestionResponse;
import com.softserve.teachua.model.test.Answer;
import com.softserve.teachua.model.test.Question;
import com.softserve.teachua.model.test.QuestionCategory;
import com.softserve.teachua.model.test.Test;
import com.softserve.teachua.repository.test.AnswerRepository;
import com.softserve.teachua.repository.test.QuestionCategoryRepository;
import com.softserve.teachua.repository.test.QuestionRepository;
import com.softserve.teachua.service.UserService;
import com.softserve.teachua.service.test.AnswerService;
import com.softserve.teachua.service.test.QuestionService;
import com.softserve.teachua.service.test.QuestionTypeService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.softserve.teachua.utils.test.validation.NullValidator.checkNull;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service("testQuestionService")
public class QuestionServiceImpl implements QuestionService {

    private final Forms formsService;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final QuestionCategoryRepository categoryRepository;
    private final QuestionTypeService typeService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final AnswerService answerService;

    @Override
    @Transactional(readOnly = true)
    public List<Question> findQuestionsByTestId(Long testId) {
        checkNull(testId, "Test id");
        return questionRepository.findQuestionsByTestId(testId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Question> findQuestionsByTestIdEager(Long testId) {
        checkNull(testId, "Test id");
        return questionRepository.findAllQuestionsByTestIdFetch(testId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Question> findQuestionsByTest(Test test) {
        checkNull(test, "Test");
        return findQuestionsByTestId(test.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionResponse> findQuestionResponsesByTestId(Long id) {
        checkNull(id, "Test id");
        return mapToDtoList(questionRepository.findQuestionsByTestId(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionResponse> findQuestionResponsesByTest(Test test) {
        checkNull(test, "Test");
        return findQuestionResponsesByTestId(test.getId());
    }

    @Override
    public Question save(Question question) {
        checkNull(question, "Question");
        log.info("**/Question has been created. {}", question.toString());
        return questionRepository.save(question);
    }

    public Form readFormInfo(String formId) throws IOException {
        return formsService.forms().get(formId).execute();
    }

    @Override
    public void questionsImport(String formUri, Long creatorId) throws IOException {
        String formId = formUri.replace("https://docs.google.com/forms/d/", "")
            .replace("/edit", "");

        String formName = readFormInfo(formId).getInfo().getTitle();
        List<Item> itemList = readFormInfo(formId).getItems();

        QuestionCategory category = new QuestionCategory();
        category.setTitle("New From (" + formName + ") from Google");
        category = categoryRepository.save(category);

        for (Item item : itemList) {
            Question question = new Question();

            question.setCreator(userService.getUserById(creatorId));
            question.setTitle(item.getTitle());
            question.setQuestionCategory(category);

            if (item.getDescription() != null) {
                question.setDescription(item.getDescription());
            } else {
                question.setDescription("");
            }

            Grading grading = item.getQuestionItem().getQuestion().getGrading();
            ChoiceQuestion choice = item.getQuestionItem().getQuestion().getChoiceQuestion();
            if (choice != null && grading != null) {
                List<Option> options = item.getQuestionItem().getQuestion().getChoiceQuestion().getOptions();
                List<CorrectAnswer> correctAnswers = grading.getCorrectAnswers().getAnswers();


                question.setQuestionType(typeService.findByTitle(choice.getType()));
                question = save(question);

                for (Option option : options) {
                    Answer answer = new Answer();
                    answer.setText(option.getValue());
                    answer.setValue(grading.getPointValue());
                    answer.setQuestion(question);

                    for (CorrectAnswer correct : correctAnswers) {
                        answer.setCorrect(option.getValue().equals(correct.getValue()));
                    }
                    log.info("**/Answer has been created.{}" + answer.toString());
                    answer = answerRepository.save(answer);
                    question.addAnswer(answer);
                }
            } else if (item.getQuestionItem().getQuestion().getTextQuestion().isEmpty()) {
                question.setQuestionType(typeService.findByTitle("TEXT"));
                save(question);
            } else if (item.getQuestionItem().getQuestion().getTextQuestion().getParagraph()) {
                question.setQuestionType(typeService.findByTitle("PARAGRAPH"));
                save(question);
            }
        }
    }

    @Override
    public List<QuestionPreview> getAllQuestions() {
        List<QuestionPreview> previews = new ArrayList<>();
        List<Question> questions = questionRepository.findAll();

        questions.forEach(question -> previews.add(
            new QuestionPreview(
                question.getId(),
                question.getTitle(),
                question.getDescription(),
                question.getQuestionCategory().getTitle())));

        return previews;
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
