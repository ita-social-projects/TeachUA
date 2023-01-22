package com.softserve.teachua.service.test.impl;

import com.google.api.services.forms.v1.Forms;
import com.google.api.services.forms.v1.model.ChoiceQuestion;
import com.google.api.services.forms.v1.model.CorrectAnswer;
import com.google.api.services.forms.v1.model.Form;
import com.google.api.services.forms.v1.model.Grading;
import com.google.api.services.forms.v1.model.Item;
import com.google.api.services.forms.v1.model.Option;
import com.softserve.teachua.dto.test.question.QuestionPreview;
import com.softserve.teachua.dto.test.question.QuestionResponse;
import com.softserve.teachua.model.test.Answer;
import com.softserve.teachua.model.test.Question;
import com.softserve.teachua.model.test.QuestionCategory;
import com.softserve.teachua.model.test.QuestionType;
import com.softserve.teachua.model.test.Test;
import com.softserve.teachua.repository.test.AnswerRepository;
import com.softserve.teachua.repository.test.QuestionCategoryRepository;
import com.softserve.teachua.repository.test.QuestionRepository;
import com.softserve.teachua.repository.test.QuestionTypeRepository;
import com.softserve.teachua.service.UserService;
import com.softserve.teachua.service.test.QuestionCategoryService;
import com.softserve.teachua.service.test.QuestionService;
import com.softserve.teachua.service.test.QuestionTypeService;
import static com.softserve.teachua.utils.test.validation.NullValidator.checkNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service("testQuestionService")
public class QuestionServiceImpl implements QuestionService {
    private final Forms formsService;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final QuestionCategoryService categoryService;
    private final QuestionCategoryRepository categoryRepository;
    private final QuestionTypeRepository typeRepository;
    private final QuestionTypeService typeService;
    private final UserService userService;
    private final ModelMapper modelMapper;

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
        String formId = formUri.replace("https://docs.google.com/forms/d/", "")
                .replace("/edit", "");

        String formName = readFormInfo(formId).getInfo().getTitle();
        List<Item> itemList = readFormInfo(formId).getItems();

        if (!categoryRepository.findByTitle("New From (" + formName + ") from Google").isPresent()) {
            categoryRepository.save(QuestionCategory.builder()
                    .title("New From (" + formName + ") from Google").build());
        }
        for (Item item : itemList) {
            Question question = new Question();

            question.setCreator(userService.getUserById(creatorId));
            question.setTitle(item.getTitle());
            question.setQuestionCategory(
                    categoryRepository.findByTitle("New From (" + formName + ") from Google").get());

            if (item.getDescription() != null) {
                question.setDescription(item.getDescription());
            } else {
                question.setDescription("");
            }

            Grading grading = item.getQuestionItem().getQuestion().getGrading();
            ChoiceQuestion choice = item.getQuestionItem().getQuestion().getChoiceQuestion();
            if (choice != null && grading != null) {
                List<CorrectAnswer> correctAnswers = grading.getCorrectAnswers().getAnswers();

                if (!typeRepository.findByTitle(choice.getType()).isPresent()) {
                    QuestionType type = QuestionType.builder().title(choice.getType()).build();
                    typeRepository.save(type);
                }

                question.setQuestionType(typeService.findByTitle(choice.getType()));
                question = save(question);

                List<Option> options = item.getQuestionItem().getQuestion().getChoiceQuestion().getOptions();

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
                if (!typeRepository.findByTitle("TEXT").isPresent()) {
                    QuestionType type = QuestionType.builder().title("TEXT").build();
                    typeRepository.save(type);
                }
                question.setQuestionType(typeService.findByTitle("TEXT"));
                save(question);
            } else if (item.getQuestionItem().getQuestion().getTextQuestion().getParagraph()) {
                if (!typeRepository.findByTitle("PARAGRAPH").isPresent()) {
                    QuestionType type = QuestionType.builder().title("PARAGRAPH").build();
                    typeRepository.save(type);
                }
                question.setQuestionType(typeService.findByTitle("PARAGRAPH"));
                save(question);
            }
        }
    }

    @Override
    public List<QuestionPreview> getAllQuestions() {
        List<QuestionPreview> previews = new ArrayList<>();
        List<Question> questions = (List<Question>) questionRepository.findAll();

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
                .collect(Collectors.toList()));
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
        question.getAnswers().stream().forEach(answer -> answer.setQuestion(question));
        return question;
    }

    private List<QuestionResponse> mapToDtoList(List<Question> questions) {
        return questions.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private Page<QuestionResponse> mapToDtoPage(Page<Question> questions) {
        return questions.map(this::mapToDto);
    }
}
