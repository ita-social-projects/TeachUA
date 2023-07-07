package com.softserve.question.controller;

import com.softserve.commons.constant.RoleData;
import com.softserve.question.controller.marker.Api;
import com.softserve.question.dto.question.ImportProfile;
import com.softserve.question.dto.question.QuestionDatabaseResponse;
import com.softserve.question.dto.question.QuestionPreview;
import com.softserve.question.dto.question.QuestionResponse;
import com.softserve.question.dto.question.question_excel.ExcelQuestionParsingResponse;
import com.softserve.question.dto.question.question_excel.QuestionDataRequest;
import com.softserve.commons.security.UserPrincipal;
import com.softserve.question.service.QuestionDataLoaderService;
import com.softserve.question.service.QuestionExcelService;
import com.softserve.question.service.QuestionService;
import com.softserve.question.util.annotation.AllowedRoles;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * This controller is responsible for managing questions.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/question")
public class QuestionsController implements Api {
    private final QuestionService questionService;
    private final QuestionExcelService questionExcelService;
    private final QuestionDataLoaderService loaderService;

    public QuestionsController(QuestionService questionService,QuestionExcelService questionExcelService,
                               QuestionDataLoaderService loaderService) {
        this.questionService = questionService;
        this.questionExcelService = questionExcelService;
        this.loaderService = loaderService;
    }

    @AllowedRoles(RoleData.ADMIN)
    @GetMapping("/search")
    public Page<QuestionResponse> searchQuestionsPageable(
            @SortDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam String query,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String category
    ) {
        return questionService.searchAllQuestionsPageable(pageable, query, type, category);
    }

    @AllowedRoles(RoleData.ADMIN)
    @GetMapping("/{id}")
    public QuestionResponse getQuestionById(@PathVariable long id) {
        return questionService.findQuestionById(id);
    }

    @AllowedRoles(RoleData.ADMIN)
    @PostMapping("/import")
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
    @GetMapping
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
    @PostMapping(value = "/excel", params = {"excel-file"})
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
    @PostMapping("/excel")
    public List<QuestionDatabaseResponse> saveExcel(@Valid @RequestBody QuestionDataRequest data,
                                                    Authentication authentication) {
        log.info("Save excel " + data);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return loaderService.saveToDatabase(data, userPrincipal.getId());
    }

    @AllowedRoles({RoleData.ADMIN, RoleData.MANAGER})
    @GetMapping("/excel")
    public ResponseEntity<ByteArrayResource> downloadQuestions() {
        byte[] bytes = questionExcelService.exportToExcel();
        ByteArrayResource resource = new ByteArrayResource(bytes);

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=questions_export.xlsx");

        return ResponseEntity.ok().headers(header).contentLength(bytes.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
    }

    @AllowedRoles(RoleData.ADMIN)
    @PostMapping
    public ResponseEntity<Long> createQuestion(@Valid @RequestBody QuestionResponse questionResponse) {
        long id = questionService
                .save(questionResponse)
                .getId();
        return ResponseEntity.ok(id);
    }

    @AllowedRoles(RoleData.ADMIN)
    @PutMapping("/{id}")
    public ResponseEntity<String> updateQuestion(@PathVariable long id,
                                                 @Valid @RequestBody QuestionResponse questionResponse) {
        questionResponse.setId(id);
        questionService.update(questionResponse);
        return ResponseEntity
                .noContent()
                .build();
    }

    @AllowedRoles(RoleData.ADMIN)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteQuestion(@PathVariable long id) {
        questionService.delete(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
