package com.softserve.teachua.controller.test;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.test.question.ImportProfile;
import com.softserve.teachua.service.test.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@RestController
public class QuestionsController implements Api {

    private final QuestionService questionService;

    @ResponseStatus(value = CREATED)
    @PostMapping(path = "/questions-import", consumes = APPLICATION_JSON_VALUE)
    public void importQuestions(@RequestBody ImportProfile importProfile) throws IOException {
        questionService.questionsImport(importProfile.getFormUri(), importProfile.getCreatorId());
    }
}
