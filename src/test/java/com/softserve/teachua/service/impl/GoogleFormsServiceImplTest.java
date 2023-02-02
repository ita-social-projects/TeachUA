package com.softserve.teachua.service.impl;

import com.google.api.services.forms.v1.Forms;
import com.google.api.services.forms.v1.model.Form;
import com.google.api.services.forms.v1.model.FormResponse;
import com.google.api.services.forms.v1.model.Grading;
import com.google.api.services.forms.v1.model.Info;
import com.google.api.services.forms.v1.model.Item;
import com.google.api.services.forms.v1.model.ListFormResponsesResponse;
import com.google.api.services.forms.v1.model.Question;
import com.google.api.services.forms.v1.model.QuestionItem;
import com.softserve.teachua.dto.googleapis.GoogleFormsResponse;
import com.softserve.teachua.dto.googleapis.QuizResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GoogleFormsServiceImplTest {
    public static final String USER_EMAIL = "eblease0@wiley.com";
    public static final String FULL_NAME = "Шевченко Тарас Григорович";
    public static final Integer TOTAL_SCORE = 10;
    public static final Integer TOTAL_POINTS = 61;
    public static final String FORM_ID = "1nvXYOmkVTAaiKBeX_k-nLrR57FYqatJf_pX8imETKaQ";
    public static final String TITLE = "Java Quiz";

    @InjectMocks
    GoogleFormsServiceImpl formsService;
    @Mock
    Forms forms;
    Form form;
    GoogleFormsResponse formsResponse;

    @BeforeEach
    void setUp() {
        form = new Form();
        form.setFormId(FORM_ID);
        Info info = new Info();
        info.setTitle(TITLE);
        form.setInfo(info);
        Item firtsItem = new Item();
        Question firstQuestion = new Question();
        firstQuestion.setGrading(new Grading().setPointValue(10));
        firstQuestion.setQuestionId("fj9df01");
        firtsItem.setQuestionItem(new QuestionItem().setQuestion(firstQuestion));
        Item secondItem = new Item();
        Question secondQuestion = new Question();
        secondQuestion.setGrading(new Grading().setPointValue(5));
        secondQuestion.setQuestionId("fj9df02");
        secondItem.setQuestionItem(new QuestionItem().setQuestion(secondQuestion));
        List<Item> items = new ArrayList<>();
        items.add(firtsItem);
        items.add(secondItem);
        form.setItems(items);
        List<QuizResult> quizResults = new ArrayList<>();
        quizResults.add(QuizResult.builder()
                .userEmail(USER_EMAIL)
                .totalScore(TOTAL_SCORE)
                .fullName(FULL_NAME)
                .build());
        formsResponse = GoogleFormsResponse.builder()
                .title(TITLE)
                .quizResults(quizResults)
                .totalPoints(TOTAL_POINTS)
                .build();
    }

    @Test
    @DisplayName("Read results from Google Forms")
    @Disabled("Not implemented yet")
    void givenGoogleFormsId_whenGetResultsFromGoogleForms_thenReturnGoogleFormsInformation() throws IOException {
        FormResponse oneFormResponse = new FormResponse();
        oneFormResponse.setRespondentEmail(USER_EMAIL);
        oneFormResponse.setTotalScore(TOTAL_SCORE.doubleValue());
        //Map<String, Answer> answers = new HashMap<>();
        //answers.put("fj9df01", new Answer().setTextAnswers(new TextAnswers().setAnswers()));
        List<FormResponse> formResponseList = new ArrayList<>();
        formResponseList.add(oneFormResponse);
        ListFormResponsesResponse formResponses = new ListFormResponsesResponse();
        formResponses.setResponses(formResponseList);

        Forms.FormsOperations operations = mock(Forms.FormsOperations.class);
        Forms.FormsOperations.Get get = mock(Forms.FormsOperations.Get.class);
        Forms.FormsOperations.Responses responses = mock(Forms.FormsOperations.Responses.class);
        Forms.FormsOperations.Responses.List list = mock(Forms.FormsOperations.Responses.List.class);

        when(forms.forms()).thenReturn(operations);
        when(operations.get(form.getFormId())).thenReturn(get);
        when(get.execute()).thenReturn(form);
        when(operations.responses()).thenReturn(responses);
        when(responses.list(form.getFormId())).thenReturn(list);
        when(list.execute()).thenReturn(formResponses);

        GoogleFormsResponse actual = formsService.getResultsFromGoogleForms(form.getFormId());
        assertThat(actual).isNotNull();
        assertThat(actual.getTitle()).isEqualTo(TITLE);
        assertThat(actual.getQuizResults()).hasSize(1);
    }
}
