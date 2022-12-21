package com.softserve.teachua.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.certificateExcel.CertificateByTemplateExcelParsingResponse;
import com.softserve.teachua.dto.template.CertificateTemplatePreview;
import com.softserve.teachua.dto.certificateByTemplate.CertificateByTemplateTransfer;
import com.softserve.teachua.dto.certificateByTemplate.CertificateTemplateMetadataTransfer;
import com.softserve.teachua.model.CertificateTemplate;
import com.softserve.teachua.service.CertificateDataLoaderService;
import com.softserve.teachua.service.CertificateExcelService;
import com.softserve.teachua.service.CertificateTemplateService;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * This controller is responsible for managing certificates created by template
 */
@RestController
@Slf4j
public class CertificateByTemplateController implements Api {
    private final CertificateExcelService excelService;
    private final CertificateTemplateService certificateTemplateService;
    private final CertificateDataLoaderService loaderService;

    @Autowired
    public CertificateByTemplateController(CertificateExcelService excelService,
                                           CertificateTemplateService certificateTemplateService,
                                           CertificateDataLoaderService loaderService) {
        this.excelService = excelService;
        this.certificateTemplateService = certificateTemplateService;
        this.loaderService = loaderService;
    }

    /**
     * The method processing template and returns {@code CertificateByTemplateTransfer} fields that have to be inputted.
     *
     * @param template - {@code CertificateTemplatePreview} template dto.
     * @return new {@code CertificateByTemplateTransfer}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @PostMapping("/certificate-by-template/pdf")
    public CertificateByTemplateTransfer uploadPdf(@RequestBody CertificateTemplatePreview template)
        throws JsonProcessingException {

        CertificateTemplate certificateTemplate = certificateTemplateService.getTemplateByFilePath(
            template.getFilePath());
        HashMap<String, String> templateProperties =
            new ObjectMapper().readValue(certificateTemplate.getProperties(), HashMap.class);

        List<String> fieldsList = new ArrayList<>();
        for (Map.Entry<String, String> entry : templateProperties.entrySet()) {
            switch (entry.getValue()) {
                case "qrCode":
                    break;
                case "serial_number":
                    if (!templateProperties.containsValue("course_number")) {
                        fieldsList.add("Номер курсу");
                    }
                    break;
                default:
                    fieldsList.add(entry.getKey());
            }
        }
        fieldsList.add("Електронна пошта");
        return CertificateByTemplateTransfer.builder()
            .templateName(template.getFilePath())
            .fieldsList(fieldsList)
            .build();
    }

    /**
     * The method uploads excel file and returns {@code CertificateByTemplateExcelParsingResponse}.
     *
     * @param multipartFile - excel file.
     * @return new {@code CertificateByTemplateExcelParsingResponse}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @PostMapping("/certificate-by-template/excel")
    public CertificateByTemplateExcelParsingResponse uploadExcel(
        @RequestParam("excel-file") MultipartFile multipartFile) {
        return excelService.parseFlexibleExcel(multipartFile);
    }

    /**
     * The method saves data to database.
     *
     * @param data - {@code CertificateByTemplateTransfer} read from form.
     */
    @AllowedRoles(RoleData.ADMIN)
    @PostMapping("/certificate-by-template/load-to-db")
    public void saveCertificate(@RequestBody CertificateByTemplateTransfer data) throws IOException {
        log.info("Save certificate/certificates by template " + data);
        loaderService.saveCertificate(data);
    }

    /**
     * This endpoint is used to save last modification date for the template uploaded earlier.
     *
     * @return {@code String}
     */
    @AllowedRoles(RoleData.ADMIN)
    @PostMapping("/certificate-by-template/load-template-metadata")
    public String saveLastTemplateModifiedDate(@RequestBody CertificateTemplateMetadataTransfer data)
        throws IOException {
        Path source = Paths.get(
            new ClassPathResource("certificates/templates/pdf-templates").getFile().getPath() + "/" + data.getTemplateName());
        String targetName = data.getTemplateLastModifiedDate() + ".pdf";

        if (!(new File(
            new ClassPathResource("certificates/templates/pdf-templates").getFile().getPath() + "/" + targetName).exists())) {
            Files.move(source, source.resolveSibling(targetName));
        } else {
            Files.delete(source);
        }

        return targetName;
    }
}
