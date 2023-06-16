package com.softserve.question.service.impl;

import com.softserve.commons.exception.NotExistException;
import com.google.api.services.forms.v1.Forms;
import com.google.api.services.forms.v1.model.ChoiceQuestion;
import com.google.api.services.forms.v1.model.CorrectAnswer;
import com.google.api.services.forms.v1.model.Form;
import com.google.api.services.forms.v1.model.Grading;
import com.google.api.services.forms.v1.model.Item;
import com.google.api.services.forms.v1.model.Option;
import com.softserve.question.dto.question.QuestionPreview;
import com.softserve.question.dto.question.QuestionResponse;
import com.softserve.question.model.Answer;
import com.softserve.question.model.Question;
import com.softserve.question.model.QuestionCategory;
import com.softserve.question.model.QuestionType;
import com.softserve.question.model.Test;
import com.softserve.question.repository.AnswerRepository;
import com.softserve.question.repository.QuestionCategoryRepository;
import com.softserve.question.repository.QuestionRepository;
import com.softserve.question.repository.QuestionTypeRepository;
import com.softserve.question.service.QuestionCategoryService;
import com.softserve.question.service.QuestionService;
import com.softserve.question.service.QuestionTypeService;
import static com.softserve.question.util.validation.NullValidator.checkNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Transactional
@Service("testQuestionService")
public class QuestionServiceImpl implements QuestionService {
    private static final String TEXT_TYPE = "TEXT";
    private static final String PARAGRAPH_TYPE = "PARAGRAPH";
    private static final String BEGIN_OF_FORM_URI = "https://docs.google.com/forms/d/";
    public static final String TEST_ID = "Test id";
    private final Forms formsService;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final QuestionCategoryService categoryService;
    private final QuestionCategoryRepository categoryRepository;
    private final QuestionTypeRepository typeRepository;
    private final QuestionTypeService typeService;
    private final ModelMapper modelMapper;

    public QuestionServiceImpl(Forms formsService, QuestionRepository questionRepository,
                               AnswerRepository answerRepository, QuestionCategoryService categoryService,
                               QuestionCategoryRepository categoryRepository, QuestionTypeRepository typeRepository,
                               QuestionTypeService typeService, ModelMapper modelMapper) {
        this.formsService = formsService;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
        this.typeRepository = typeRepository;
        this.typeService = typeService;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QuestionResponse> findAllQuestionsPageable(Pageable pageable) {
        return mapToDtoPage(questionRepository.findAll(pageable));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QuestionResponse> searchAllQuestionsPageable(
            Pageable pageable, String query, String type, String category) {
        if (!isEmpty(type) && !isEmpty(category)) {
            return mapToDtoPage(
                    questionRepository.findByTitleContainingIgnoreCaseAndQuestionTypeAndQuestionCategory(
                            pageable,
                            query,
                            typeService.findByTitle(type),
                            categoryService.findByTitle(category)
                    ));
        }
        if (!isEmpty(type)) {
            return mapToDtoPage(
                    questionRepository.findByTitleContainingIgnoreCaseAndQuestionType(
                            pageable,
                            query,
                            typeService.findByTitle(type)
                    ));
        }
        if (!isEmpty(category)) {
            return mapToDtoPage(questionRepository.findByTitleContainingIgnoreCaseAndQuestionCategory(
                    pageable,
                    query,
                    categoryService.findByTitle(category)
            ));
        }
        return mapToDtoPage(questionRepository.findByTitleContainingIgnoreCase(pageable, query));
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionResponse findQuestionById(Long id) {
        return mapToDto(getQuestionById(id));
    }

    @Override
    @Transactional
    public Question getQuestionById(Long id) {
        return questionRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Question not found for id=" + id)
                );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Question> findQuestionsByTestId(Long testId) {
        checkNull(testId, TEST_ID);
        return questionRepository.findQuestionsByTestId(testId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Question> findQuestionsByTestIdEager(Long testId) {
        checkNull(testId, TEST_ID);
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
        checkNull(id, TEST_ID);
        return mapToDtoList(questionRepository.findQuestionsByTestId(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionResponse> findQuestionResponsesByTest(Test test) {
        checkNull(test, "Test");
        return findQuestionResponsesByTestId(test.getId());
    }

    @Override
    @Transactional
    public Question save(Question question) {
        checkNull(question, "Question");
        log.info("**/Question has been created. {}", question.toString());
        return questionRepository.save(question);
    }

    @Override
    @Transactional
    public Question save(QuestionResponse questionResponse) {
        return save(mapToModel(questionResponse));
    }

    @Override
    @Transactional
    public Question update(QuestionResponse questionResponse) {
        if (!questionRepository.existsById(questionResponse.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Question not found for id=" + questionResponse.getId());
        }
        return save(mapToModel(questionResponse));
    }

    @Override
    @Transactional
    public void delete(long id) {
        questionRepository.delete(getQuestionById(id));
    }

    public Form readFormInfo(String formId) throws IOException {
        return formsService.forms().get(formId).execute();
    }

    @Override
    public void questionsImport(String formUri, Long creatorId) throws IOException {
        String formId = formUri.replace(BEGIN_OF_FORM_URI, "").replace("/edit", "");

        String categoryName = String.format("New Form (%s) from Google", readFormInfo(formId).getInfo().getTitle());
        List<Item> itemList = readFormInfo(formId).getItems();

        if (categoryRepository.findByTitle(categoryName).isEmpty()) {
            categoryRepository.save(QuestionCategory.builder().title(categoryName).build());
        }

        for (Item item : itemList) {
            Question question = new Question();
            question.setCreatorId(creatorId);
            question.setTitle(item.getTitle());
            question.setQuestionCategory(categoryRepository.findByTitle(categoryName)
                    .orElseThrow(NotExistException::new));

            if (item.getDescription() != null) {
                question.setDescription(item.getDescription());
            } else {
                question.setDescription("");
            }

            Grading grading = item.getQuestionItem().getQuestion().getGrading();
            ChoiceQuestion choice = item.getQuestionItem().getQuestion().getChoiceQuestion();
            if (choice != null && grading != null) {
                addNewType(question, choice.getType());
                addOptionsToQuestion(item, grading, question);
            } else if (item.getQuestionItem().getQuestion().getTextQuestion().isEmpty()) {
                addNewType(question, TEXT_TYPE);
            } else if (Boolean.TRUE.equals(item.getQuestionItem().getQuestion().getTextQuestion().getParagraph())) {
                addNewType(question, PARAGRAPH_TYPE);
            }
        }
    }

    private void addNewType(Question question, String nameOfType) {
        if (typeRepository.findByTitle(nameOfType).isEmpty()) {
            QuestionType type = QuestionType.builder().title(nameOfType).build();
            typeRepository.save(type);
        }
        question.setQuestionType(typeService.findByTitle(nameOfType));
        save(question);
    }

    private void addOptionsToQuestion(Item item,
                                      Grading grading,
                                      Question question) {
        List<Option> options = item.getQuestionItem().getQuestion().getChoiceQuestion().getOptions();
        List<CorrectAnswer> correctAnswers = grading.getCorrectAnswers().getAnswers();

        for (Option option : options) {
            Answer answer = new Answer();
            answer.setText(option.getValue());
            answer.setValue(grading.getPointValue());
            answer.setQuestion(question);

            for (CorrectAnswer correct : correctAnswers) {
                if (option.getValue().equals(correct.getValue())) {
                    answer.setCorrect(true);
                    answer.setValue(grading.getPointValue());
                    break;
                } else {
                    answer.setCorrect(false);
                    answer.setValue(0);
                }
            }
            answer = answerRepository.save(answer);
            question.addAnswer(answer);
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

    private QuestionResponse mapToDto(Question question) {
        QuestionResponse questionResponse = modelMapper.map(question, QuestionResponse.class);
        questionResponse.setAnswerTitles(question.getAnswers()
                .stream()
                .map(Answer::getText)
                .toList());
        return questionResponse;
    }

    private Question mapToModel(QuestionResponse questionResponse) {
        Question question = Question.builder()
                .id(questionResponse.getId())
                .title(questionResponse.getTitle())
                .description(questionResponse.getDescription())
                .questionType(typeService.findByTitle(questionResponse.getQuestionTypeTitle()))
                .questionCategory(categoryService.findByTitle(questionResponse.getQuestionCategoryTitle()))
                .answers(questionResponse
                        .getAnswers()
                        .stream()
                        .map(answer -> modelMapper.map(answer, Answer.class))
                        .collect(Collectors.toSet()))
                .build();
        question.getAnswers().forEach(answer -> answer.setQuestion(question));
        return question;
    }

    private List<QuestionResponse> mapToDtoList(List<Question> questions) {
        return questions.stream()
                .map(this::mapToDto)
                .toList();
    }

    private Page<QuestionResponse> mapToDtoPage(Page<Question> questions) {
        return questions.map(this::mapToDto);
    }
}
