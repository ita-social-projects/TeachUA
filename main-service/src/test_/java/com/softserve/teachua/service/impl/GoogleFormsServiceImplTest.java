package com.softserve.teachua.service.impl;

import com.google.api.services.forms.v1.Forms;
import com.google.api.services.forms.v1.model.Answer;
import com.google.api.services.forms.v1.model.Form;
import com.google.api.services.forms.v1.model.FormResponse;
import com.google.api.services.forms.v1.model.Grade;
import com.google.api.services.forms.v1.model.Grading;
import com.google.api.services.forms.v1.model.Info;
import com.google.api.services.forms.v1.model.Item;
import com.google.api.services.forms.v1.model.ListFormResponsesResponse;
import com.google.api.services.forms.v1.model.Question;
import com.google.api.services.forms.v1.model.QuestionItem;
import com.google.api.services.forms.v1.model.TextAnswer;
import com.google.api.services.forms.v1.model.TextAnswers;
import com.softserve.teachua.dto.googleapis.GoogleFormsResponse;
import com.softserve.teachua.exception.GoogleApisDocumentException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
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
    public static final String FORM_ID = "1nvXYOmkVTAaiKBeX_k-nLrR57FYqatJf_pX8imETKaQ";
    public static final String TITLE = "Java Quiz";
    public static final String EMAIL_QUESTION_ID = "fj9df01";
    public static final String PERSONAL_NAME_QUESTION_ID = "fj9df02";
    public static final String EXCEPTION_MESSAGE =
            "Error occurred while retrieving information from Google Forms with formId: %s";

    @InjectMocks
    GoogleFormsServiceImpl formsService;
    @Mock
    Forms forms;
    Form form;
    ListFormResponsesResponse formResponses;

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
        firstQuestion.setQuestionId(EMAIL_QUESTION_ID);
        firtsItem.setQuestionItem(new QuestionItem().setQuestion(firstQuestion));
        Item secondItem = new Item();
        Question secondQuestion = new Question();
        secondQuestion.setGrading(new Grading().setPointValue(5));
        secondQuestion.setQuestionId(PERSONAL_NAME_QUESTION_ID);
        secondItem.setQuestionItem(new QuestionItem().setQuestion(secondQuestion));
        List<Item> items = new ArrayList<>();
        items.add(firtsItem);
        items.add(secondItem);
        form.setItems(items);
        FormResponse firstResponse = new FormResponse();
        firstResponse.setRespondentEmail(USER_EMAIL);
        firstResponse.setTotalScore(TOTAL_SCORE.doubleValue());
        Map<String, Answer> answers = new HashMap<>();
        Answer firstAnswer = new Answer();
        firstAnswer.setQuestionId(EMAIL_QUESTION_ID);
        firstAnswer.setGrade(new Grade().setScore(10.0));
        firstAnswer.setTextAnswers(new TextAnswers().setAnswers(
                Collections.singletonList(new TextAnswer().setValue(USER_EMAIL))));
        Answer secondAnswer = new Answer();
        secondAnswer.setQuestionId(EMAIL_QUESTION_ID);
        secondAnswer.setGrade(new Grade().setScore(10.0));
        secondAnswer.setTextAnswers(new TextAnswers().setAnswers(
                Collections.singletonList(new TextAnswer().setValue(FULL_NAME))));
        answers.put(EMAIL_QUESTION_ID, firstAnswer);
        answers.put(PERSONAL_NAME_QUESTION_ID, secondAnswer);
        firstResponse.setAnswers(answers);
        List<FormResponse> formResponseList = new ArrayList<>();
        formResponseList.add(firstResponse);
        formResponses = new ListFormResponsesResponse();
        formResponses.setResponses(formResponseList);
    }

    @Test
    @DisplayName("Read results from Google Forms")
    void givenGoogleFormsId_whenGetResultsFromGoogleForms_thenReturnGoogleFormsInformation() throws IOException {
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

    @Test
    @DisplayName("Get Exception when cannot read Google Form")
    void givenGoogleFormsId_whenGetForm_thenThrowsException() throws IOException {
        Forms.FormsOperations operations = mock(Forms.FormsOperations.class);
        when(forms.forms()).thenReturn(operations);
        when(operations.get(form.getFormId())).thenThrow(new IOException());

        assertThatThrownBy(() -> formsService.getResultsFromGoogleForms(FORM_ID))
                .isInstanceOf(GoogleApisDocumentException.class)
                .hasMessage(String.format(EXCEPTION_MESSAGE, FORM_ID));
    }

    @Test
    @DisplayName("Get Exception when cannot read Google Form responses")
    void givenGoogleFormsId_whenGetListFormResponses_thenThrowsException() throws IOException {
        Forms.FormsOperations operations = mock(Forms.FormsOperations.class);
        Forms.FormsOperations.Get get = mock(Forms.FormsOperations.Get.class);
        Forms.FormsOperations.Responses responses = mock(Forms.FormsOperations.Responses.class);

        when(forms.forms()).thenReturn(operations);
        when(operations.get(form.getFormId())).thenReturn(get);
        when(get.execute()).thenReturn(form);
        when(operations.responses()).thenReturn(responses);
        when(responses.list(form.getFormId())).thenThrow(new IOException());

        assertThatThrownBy(() -> formsService.getResultsFromGoogleForms(FORM_ID))
                .isInstanceOf(GoogleApisDocumentException.class)
                .hasMessage(String.format(EXCEPTION_MESSAGE, FORM_ID));
    }
}
