package com.softserve.teachua.service.test.impl;

import com.google.api.services.forms.v1.Forms;
import com.google.api.services.forms.v1.model.Form;
import com.google.api.services.forms.v1.model.ListFormResponsesResponse;
import com.softserve.teachua.dto.test.result.GoogleFormsInformation;
import com.softserve.teachua.dto.test.result.GoogleFormsQuizResults;
import com.softserve.teachua.dto.test.result.GoogleFormsWrapper;
import com.softserve.teachua.exception.GoogleApisDocumentException;
import com.softserve.teachua.model.User;
import com.softserve.teachua.model.UserFromGoogleForms;
import com.softserve.teachua.model.test.Result;
import com.softserve.teachua.model.test.Test;
import com.softserve.teachua.repository.UserRepository;
import com.softserve.teachua.repository.test.ResultRepository;
import com.softserve.teachua.repository.test.TestRepository;
import com.softserve.teachua.repository.test.UserFromGoogleFormsRepository;
import com.softserve.teachua.service.test.GoogleFormsService;
import static com.softserve.teachua.utils.test.validation.NullValidator.checkNull;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GoogleFormsServiceImpl implements GoogleFormsService {
    public static final String ERROR_MESSAGE =
        "Error occurred while retrieving information from Google Forms with formId: %s";
    private final Forms forms;
    private final ResultRepository resultRepository;
    private final TestRepository testRepository;
    private final UserFromGoogleFormsRepository userFromGoogleFormsRepository;
    private final UserRepository userRepository;

    public GoogleFormsServiceImpl(Forms forms, ResultRepository resultRepository, TestRepository testRepository,
                                  UserFromGoogleFormsRepository userFromGoogleFormsRepository,
                                  UserRepository userRepository) {
        this.forms = forms;
        this.resultRepository = resultRepository;
        this.testRepository = testRepository;
        this.userFromGoogleFormsRepository = userFromGoogleFormsRepository;
        this.userRepository = userRepository;
    }

    public GoogleFormsWrapper getResultsFromGoogleForms(String formId) {
        checkNull(formId, "Google Forms ID");
        Form form;
        ListFormResponsesResponse formResponses;

        try {
            form = forms.forms().get(formId).execute();
            formResponses = forms.forms().responses().list(formId).execute();
        } catch (IOException e) {
            String message = String.format(ERROR_MESSAGE, formId);
            log.error(message);
            throw new GoogleApisDocumentException(message);
        }

        GoogleFormsInformation information = GoogleFormsInformation.builder()
            .title(form.getInfo().getTitle())
            .description(form.getInfo().getDescription())
            .build();

        List<GoogleFormsQuizResults> quizResults = formResponses.getResponses().stream()
            .map(resp ->
                GoogleFormsQuizResults.builder()
                    .respondentEmail(resp.getRespondentEmail())
                    .lastSubmittedTime(resp.getLastSubmittedTime())
                    .totalScore(resp.getTotalScore())
                    .build()
            ).collect(Collectors.toList());

        return GoogleFormsWrapper.builder()
            .information(information)
            .quizResults(quizResults)
            .build();
    }

    public GoogleFormsInformation getFormInfoFromGoogleForms(String formId) {
        checkNull(formId, "Google Forms ID");
        Form form;

        try {
            form = forms.forms().get(formId).execute();
        } catch (IOException e) {
            String message = String.format(ERROR_MESSAGE, formId);
            log.error(message);
            throw new GoogleApisDocumentException(message);
        }

        return GoogleFormsInformation.builder()
            .title(form.getInfo().getTitle())
            .description(form.getInfo().getDescription())
            .build();
    }

    public void createGoogleFormsResponse(GoogleFormsWrapper results) {
        checkNull(results, "Google Forms Wrapper");
        Optional<Test> foundedTest = testRepository.findByTitle(results.getInformation().getTitle());
        Test test = foundedTest.orElseThrow(
            () -> new IllegalArgumentException("GoogleFormsWrapper does not contain Test title."));

        for (GoogleFormsQuizResults quizResults : results.getQuizResults()) {
            Result toSave = new Result();
            Optional<User> existedUser = userRepository.findByEmail(quizResults.getRespondentEmail());
            if (existedUser.isPresent()) {
                toSave.setUser(existedUser.get());
            } else {
                addNewUser(quizResults, toSave);
            }
            toSave.setTest(test);
            toSave.setTestFinishTime(
                LocalDateTime.parse(quizResults.getLastSubmittedTime().substring(0, 19)));
            if (quizResults.getTotalScore() == null) {
                toSave.setGrade(0);
            } else {
                toSave.setGrade(quizResults.getTotalScore().intValue());
            }

            resultRepository.save(toSave);
        }
    }

    private void addNewUser(GoogleFormsQuizResults quizResults, Result toSave) {
        Optional<UserFromGoogleForms> foundedGoogleFormsUser =
            userFromGoogleFormsRepository.findByEmail(quizResults.getRespondentEmail());
        if (foundedGoogleFormsUser.isPresent()) {
            toSave.setUserFromGoogleForms(foundedGoogleFormsUser.get());
        } else {
            UserFromGoogleForms toSaveUser = UserFromGoogleForms.builder()
                .email(quizResults.getRespondentEmail()).build();
            UserFromGoogleForms savedUser = userFromGoogleFormsRepository.save(toSaveUser);
            toSave.setUserFromGoogleForms(savedUser);
        }
    }
}
