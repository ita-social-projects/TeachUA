package com.softserve.teachua.controller;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.certificate.*;
import com.softserve.teachua.dto.certificateByTemplate.CertificateByTemplateTransfer;
import com.softserve.teachua.dto.certificateByTemplate.CertificateTemplateMetadataTransfer;
import com.softserve.teachua.dto.certificateByTemplate.CertificateTemplateUploadResponse;
import com.softserve.teachua.service.CertificateExcelService;
import com.softserve.teachua.utils.PdfTemplateService;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import javax.servlet.http.HttpServletRequest;
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
    private final PdfTemplateService pdfTemplateService;

    @Autowired
    public CertificateByTemplateController(CertificateExcelService excelService,
                                           PdfTemplateService pdfTemplateService) {
        this.excelService = excelService;
        this.pdfTemplateService = pdfTemplateService;
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
    @PostMapping("/certificate-by-template/pdf")
    public CertificateTemplateUploadResponse uploadPdf(@RequestParam("pdf-file") MultipartFile multipartFile)
        throws IOException {
        //only ONCE
        File directory = new File("templates");
        if (!directory.exists()) {
            directory.mkdir();
        }

        String templateName = Instant.now().toEpochMilli() + ".pdf";
        File file = new File("templates/" + templateName);

        try (OutputStream os = Files.newOutputStream(file.toPath())) {
            os.write(multipartFile.getBytes());
        }
        return new CertificateTemplateUploadResponse(pdfTemplateService.getTemplateFields(file.getPath()),
            templateName);
    }

    @AllowedRoles(RoleData.ADMIN)
    @PostMapping("/certificate-by-template/load-to-db")
    public List<CertificateDatabaseResponse> saveExcel(@RequestBody CertificateByTemplateTransfer data) {
//        log.info("Save excel " + data);
        System.out.println(data);
        return null;
    }

    @AllowedRoles(RoleData.ADMIN)
    @PostMapping("/certificate-by-template/load-last-modified-date")
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
