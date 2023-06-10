package com.softserve.teachua.service.impl;

import com.google.api.services.forms.v1.Forms;
import com.google.api.services.forms.v1.model.Form;
import com.google.api.services.forms.v1.model.Grading;
import com.google.api.services.forms.v1.model.Item;
import com.google.api.services.forms.v1.model.ListFormResponsesResponse;
import com.softserve.teachua.dto.googleapis.GoogleFormsResponse;
import com.softserve.teachua.dto.googleapis.QuizResult;
import com.softserve.teachua.exception.GoogleApisDocumentException;
import com.softserve.teachua.service.GoogleFormsService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GoogleFormsServiceImpl implements GoogleFormsService {
    public static final String ERROR_MESSAGE =
            "Error occurred while retrieving information from Google Forms with formId: %s";

    private final Forms forms;

    public GoogleFormsServiceImpl(Forms forms) {
        this.forms = forms;
    }

    @Override
    public GoogleFormsResponse getResultsFromGoogleForms(String formId) {
        if (StringUtils.isEmpty(formId)) {
            throw new IllegalArgumentException("Google Forms ID can't be null");
        }
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

        int totalPointsValue = form.getItems().stream()
                .map(Item::getQuestionItem)
                .filter(Objects::nonNull)
                .map(questionItem -> questionItem.getQuestion().getGrading())
                .filter(Objects::nonNull)
                .mapToInt(Grading::getPointValue)
                .sum();

        String emailQuestionId = form.getItems().get(0).getQuestionItem().getQuestion().getQuestionId();
        String personalNameQuestionId = form.getItems().get(1).getQuestionItem().getQuestion().getQuestionId();

        List<QuizResult> quizResults = new ArrayList<>();
        if (formResponses.getResponses() != null) {
            quizResults = formResponses.getResponses().stream()
                    .map(resp -> QuizResult.builder()
                            .userEmail(resp.getAnswers().get(emailQuestionId).getTextAnswers().getAnswers()
                                    .get(0).getValue().trim())
                            .fullName(resp.getAnswers().get(personalNameQuestionId).getTextAnswers().getAnswers()
                                    .get(0).getValue().trim())
                            .totalScore(resp.getTotalScore() == null ? 0 : resp.getTotalScore().intValue())
                            .build()
                    ).toList();
        }

        return GoogleFormsResponse.builder()
                .title(form.getInfo().getTitle())
                .totalPoints(totalPointsValue)
                .quizResults(quizResults)
                .build();
    }
}
