package com.softserve.teachua.service.test;

import com.google.api.services.forms.v1.Forms;
import com.google.api.services.forms.v1.model.Form;
import com.google.api.services.forms.v1.model.FormResponse;
import com.google.api.services.forms.v1.model.Info;
import com.google.api.services.forms.v1.model.ListFormResponsesResponse;
import com.softserve.teachua.dto.test.result.GoogleFormsInformation;
import com.softserve.teachua.dto.test.result.GoogleFormsQuizResults;
import com.softserve.teachua.dto.test.result.GoogleFormsWrapper;
import com.softserve.teachua.model.User;
import com.softserve.teachua.model.UserFromGoogleForms;
import com.softserve.teachua.model.test.Result;
import com.softserve.teachua.repository.UserRepository;
import com.softserve.teachua.repository.test.ResultRepository;
import com.softserve.teachua.repository.test.TestRepository;
import com.softserve.teachua.repository.test.UserFromGoogleFormsRepository;
import com.softserve.teachua.service.test.impl.GoogleFormsServiceImpl;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GoogleFormsServiceTest {
    public static final String USER_EMAIL = "eblease0@wiley.com";
    public static final String CREATED_TIME = "2014-10-02T15:01:23";
    public static final double TOTAL_SCORE = 10.0;
    public static final String FORM_ID = "1nvXYOmkVTAaiKBeX_k-nLrR57FYqatJf_pX8imETKaQ";
    public static final String TITLE = "Java Quiz";
    public static final String DESCRIPTION = "You can test your Java skills with Quiz.";
    @InjectMocks
    GoogleFormsServiceImpl formsService;
    @Mock
    Forms forms;
    @Mock
    TestRepository testRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    UserFromGoogleFormsRepository userFromGoogleFormsRepository;
    @Mock
    ResultRepository resultRepository;
    Form form;
    GoogleFormsWrapper wrapper;

    @BeforeEach
    void setUp() {
        form = new Form();
        form.setFormId(FORM_ID);
        Info info = new Info();
        info.setTitle(TITLE);
        info.setDescription(DESCRIPTION);
        form.setInfo(info);
        List<GoogleFormsQuizResults> quizResults = new ArrayList<>();
        quizResults.add(GoogleFormsQuizResults.builder()
            .respondentEmail(USER_EMAIL)
            .lastSubmittedTime(CREATED_TIME)
            .totalScore(TOTAL_SCORE)
            .build());
        wrapper = GoogleFormsWrapper.builder()
            .information(GoogleFormsInformation.builder().title(TITLE).description(DESCRIPTION).build())
            .quizResults(quizResults)
            .build();
    }

    @Test
    @DisplayName("Read results from Google Forms")
    void givenGoogleFormsId_whenGetResultsFromGoogleForms_thenReturnGoogleFormsWrapper() throws IOException {
        FormResponse oneFormResponse = new FormResponse();
        oneFormResponse.setRespondentEmail(USER_EMAIL);
        oneFormResponse.setLastSubmittedTime(CREATED_TIME);
        oneFormResponse.setTotalScore(TOTAL_SCORE);
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

        GoogleFormsWrapper actual = formsService.getResultsFromGoogleForms(form.getFormId());
        assertThat(actual).isNotNull();
        assertThat(actual.getInformation()).hasNoNullFieldsOrProperties();
        assertThat(actual.getQuizResults()).hasSize(1);
    }

    @Test
    @DisplayName("Read Information from Google Forms")
    void givenGoogleFormsId_whenGetFormInfoFromGoogleForms_thenReturnGoogleFormsInformation() throws IOException {
        Forms.FormsOperations operations = mock(Forms.FormsOperations.class);
        Forms.FormsOperations.Get get = mock(Forms.FormsOperations.Get.class);

        when(forms.forms()).thenReturn(operations);
        when(operations.get(form.getFormId())).thenReturn(get);
        when(get.execute()).thenReturn(form);

        GoogleFormsInformation actual = formsService.getFormInfoFromGoogleForms(form.getFormId());
        assertThat(actual).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("Save Quiz results with existed User")
    void givenGoogleFormsWrapperWithExistedUser_whenCreateGoogleFormsResponse_thanOkay() {
        when(testRepository.findByTitle(form.getInfo().getTitle()))
            .thenReturn(Optional.of(com.softserve.teachua.model.test.Test.builder().build()));
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(User.builder().email(USER_EMAIL).build()));

        formsService.createGoogleFormsResponse(wrapper);
        verify(resultRepository, times(1)).save(any(Result.class));
    }

    @Test
    @DisplayName("Save Quiz results with User from Google forms table")
    void givenGoogleFormsWrapperWithExistedUserFromGoogleTable_whenCreateGoogleFormsResponse_thanOkay() {
        when(testRepository.findByTitle(form.getInfo().getTitle()))
            .thenReturn(Optional.of(com.softserve.teachua.model.test.Test.builder().build()));
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.empty());
        when(userFromGoogleFormsRepository.findByEmail(anyString()))
            .thenReturn(Optional.of(UserFromGoogleForms.builder().email(USER_EMAIL).build()));

        formsService.createGoogleFormsResponse(wrapper);
        verify(resultRepository, times(1)).save(any(Result.class));
    }

    @Test
    @DisplayName("Add new User to users_from_google_forms table and save Quiz results")
    void givenGoogleFormsWrapperWithNewUser_whenCreateGoogleFormsResponse_thanOkay() {
        when(testRepository.findByTitle(form.getInfo().getTitle()))
            .thenReturn(Optional.of(com.softserve.teachua.model.test.Test.builder().build()));
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.empty());
        when(userFromGoogleFormsRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        formsService.createGoogleFormsResponse(wrapper);
        verify(resultRepository, times(1)).save(any(Result.class));
    }
}
