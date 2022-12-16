package com.softserve.teachua.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.template.CertificateTemplatePreview;
import com.softserve.teachua.dto.certificateByTemplate.CertificateByTemplateTransfer;
import com.softserve.teachua.dto.certificateByTemplate.CertificateTemplateMetadataTransfer;
import com.softserve.teachua.model.CertificateTemplate;
import com.softserve.teachua.service.CertificateDataLoaderService;
import com.softserve.teachua.service.CertificateExcelService;
import com.softserve.teachua.service.CertificateTemplateService;
import com.softserve.teachua.service.impl.CertificateByTemplateServiceImpl;
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
    private final CertificateByTemplateServiceImpl certificateByTemplateServiceImpl;
    private final CertificateTemplateService certificateTemplateService;
    private final CertificateDataLoaderService loaderService;

    @Autowired
    public CertificateByTemplateController(CertificateExcelService excelService,
                                           CertificateByTemplateServiceImpl certificateByTemplateServiceImpl,
                                           CertificateTemplateService certificateTemplateService,
                                           CertificateDataLoaderService loaderService) {
        this.excelService = excelService;
        this.certificateByTemplateServiceImpl = certificateByTemplateServiceImpl;
        this.certificateTemplateService = certificateTemplateService;
        this.loaderService = loaderService;
    }

    @AllowedRoles(RoleData.ADMIN)
    @PostMapping("/certificate-by-template/pdf")
    public CertificateByTemplateTransfer uploadPdf(@RequestBody CertificateTemplatePreview template) throws JsonProcessingException {

        CertificateTemplate certificateTemplate = certificateTemplateService.getTemplateByFilePath(
            template.getFilePath());
        HashMap<String, String> templateProperties =
            new ObjectMapper().readValue(certificateTemplate.getProperties(), HashMap.class);

        List<String> fieldsList = new ArrayList<>();
        for(Map.Entry<String, String> entry : templateProperties.entrySet()){
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
        CertificateByTemplateTransfer result = new CertificateByTemplateTransfer();
        result.setTemplateName(template.getFilePath());
        result.setFieldsList(fieldsList);
        return result;
    }

    /**
     * The method uploads excel file and returns {@code ExcelParsingResponse}.
     *
     * @param multipartFile - excel file.
     * @return new {@code ExcelParsingResponse}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @PostMapping("/certificate-by-template/excel")
    public List uploadExcel(@RequestParam("excel-file") MultipartFile multipartFile) {
        List<String> stringList = new ArrayList<>();
        stringList.add("name");
        stringList.add("email");
        stringList.add("date");
        List list = new ArrayList();
        list.add(excelService.parseExcel(multipartFile));
        list.add(stringList);
        return list;
    }

    @AllowedRoles(RoleData.ADMIN)
    }

    @AllowedRoles(RoleData.ADMIN)
    @PostMapping("/certificate-by-template/load-to-db")
    public void saveCertificate(@RequestBody CertificateByTemplateTransfer data) throws IOException {
        log.info("Save certificate " + data);
        loaderService.saveCertificate(data);

    }

    @AllowedRoles(RoleData.ADMIN)
    @PostMapping("/certificate-by-template/load-template-metadata")
    public String saveLastTemplateModifiedDate(@RequestBody CertificateTemplateMetadataTransfer data)
        throws IOException {
        Path source = Paths.get("templates/" + data.getTemplateName());
        String targetName = data.getTemplateLastModifiedDate() + ".pdf";

        if (!(new File("templates/" + targetName).exists())) {
            Files.move(source, source.resolveSibling(targetName));
        } else {
            Files.delete(source);
        }

        return targetName;
    }
}
