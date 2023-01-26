package com.softserve.teachua.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.certificateByTemplate.CertificateByTemplateTransfer;
import com.softserve.teachua.dto.certificateExcel.CertificateByTemplateExcelParsingResponse;
import com.softserve.teachua.dto.certificateTemplate.CertificateTemplatePreview;
import com.softserve.teachua.model.CertificateTemplate;
import com.softserve.teachua.service.CertificateDataLoaderService;
import com.softserve.teachua.service.CertificateExcelService;
import com.softserve.teachua.service.CertificateTemplateService;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * This controller is responsible for managing certificates created by a template.
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
        List<String> fieldPropertiesList = new ArrayList<>();
        for (Map.Entry<String, String> entry : templateProperties.entrySet()) {
            // @formatter:off
            switch (entry.getValue()) {
              case "date":
                  fieldsList.add(entry.getKey());
                  fieldPropertiesList.add("date");
                  break;
              case "hours":
              case "course_number":
                  fieldsList.add(entry.getKey());
                  fieldPropertiesList.add("int");
                  break;
              case "qrCode_white":
              case "qrCode_black":
                  break;
              case "serial_number":
                  if (!templateProperties.containsValue("course_number")) {
                      fieldsList.add("Номер курсу");
                      fieldPropertiesList.add("int");
                  }
                  break;
              default:
                  fieldsList.add(entry.getKey());
                  fieldPropertiesList.add("String");
            }
            // @formatter:on
        }
        fieldsList.add("Електронна пошта");
        fieldPropertiesList.add("String");
        return CertificateByTemplateTransfer.builder()
                .templateName(template.getFilePath())
                .fieldsList(fieldsList)
                .fieldPropertiesList(fieldPropertiesList)
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
     * The method validates excel file and returns.
     * <p>{@code List<String[]>}, in which one bucket contains two {@code String} classes</p>
     * <p>first String - message;</p>
     * <p>second String - message code(1 - warning; 2 - error; 3 - success);</p>
     *
     * @param data template dto.
     * @return new {@code List<String[]>}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @PostMapping("/certificate-by-template/validate")
    public List<String[]> validateCertificateExcelData(@RequestBody CertificateByTemplateTransfer data)
            throws JsonProcessingException {
        log.info("Validate certificate/certificates by template " + data);
        return excelService.validateCertificateByTemplateExcel(data);
    }
}
