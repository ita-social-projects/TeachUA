package com.softserve.teachua.controller.test;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.test.question.ImportProfile;
import com.softserve.teachua.dto.test.question.QuestionDatabaseResponse;
import com.softserve.teachua.dto.test.question.QuestionPreview;
import com.softserve.teachua.dto.test.question.questionExcel.ExcelQuestionParsingResponse;
import com.softserve.teachua.dto.test.question.questionExcel.QuestionDataRequest;
import com.softserve.teachua.security.UserPrincipal;
import com.softserve.teachua.service.test.QuestionDataLoaderService;
import com.softserve.teachua.service.test.QuestionExcelService;
import com.softserve.teachua.service.test.QuestionService;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import java.io.IOException;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * This controller is responsible for managing questions
 */

@RequiredArgsConstructor
@RestController
@Slf4j
public class QuestionsController implements Api {

    private final QuestionService questionService;
    private final QuestionExcelService questionExcelService;
    private final QuestionDataLoaderService loaderService;

    @AllowedRoles(RoleData.ADMIN)
    @PostMapping("/questions-import")
    public void importQuestions(@RequestBody ImportProfile importProfile, Authentication authentication)
        throws IOException {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        questionService.questionsImport(importProfile.getFormId(), userPrincipal.getId());
    }

    /**
     * This endpoint is used to get all questions from DB.
     *
     * @return {@code List<CertificateUserResponse>}
     */
    @AllowedRoles({RoleData.ADMIN, RoleData.MANAGER})
    @GetMapping("/quiz/questions")
    public List<QuestionPreview> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    /**
     * The method uploads excel file and returns {@code ExcelQuestionParsingResponse}.
     *
     * @param multipartFile - excel file.
     * @return new {@code ExcelQuestionParsingResponse}.
     */
    @AllowedRoles({RoleData.ADMIN, RoleData.MANAGER})
    @PostMapping("/questions/excel")
    public ExcelQuestionParsingResponse uploadExcel(@RequestParam("excel-file") MultipartFile multipartFile) {
        return questionExcelService.parseExcel(multipartFile);
    }

    /**
     * The method saves data to database.
     *
     * @param data - {@code QuestionsDataToDatabase} read from form.
     * @return new {@code List<QuestionDatabaseResponse>}
     */
    @AllowedRoles({RoleData.ADMIN, RoleData.MANAGER})
    @PostMapping("/questions/load-to-db")
    public List<QuestionDatabaseResponse> saveExcel(@Valid @RequestBody QuestionDataRequest data, Authentication authentication) {
        log.info("Save excel " + data);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return loaderService.saveToDatabase(data, userPrincipal.getId());
    }

}
