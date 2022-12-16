package com.softserve.teachua.controller;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.template.CertificateTemplatePreview;
import com.softserve.teachua.dto.template.CreateCertificateTemplate;
import com.softserve.teachua.dto.template.SuccessCreatedCertificateTemplate;
import com.softserve.teachua.dto.certificateByTemplate.CertificateTemplateUploadResponse;
import com.softserve.teachua.service.CertificateTemplateService;
import com.softserve.teachua.service.impl.CertificateByTemplateServiceImpl;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.time.Instant;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
public class CertificateTemplateController implements Api {
    private final CertificateTemplateService certificateTemplateService;
    private final CertificateByTemplateServiceImpl certificateByTemplateService;

    public CertificateTemplateController(CertificateTemplateService certificateTemplateService,
                                         CertificateByTemplateServiceImpl certificateByTemplateServiceImpl) {
        this.certificateTemplateService = certificateTemplateService;
        this.certificateByTemplateService = certificateByTemplateServiceImpl;
    }

    @PostMapping("/template")
    @AllowedRoles(RoleData.ADMIN)
    public SuccessCreatedCertificateTemplate createTemplate(
        @Valid @RequestBody CreateCertificateTemplate createCertificateTemplate) {
        return certificateTemplateService.addTemplate(createCertificateTemplate);
    }

    @GetMapping("/templates")
    @AllowedRoles(RoleData.ADMIN)
    public List<CertificateTemplatePreview> getAllTemplates() {
        return certificateTemplateService.getAllTemplates();
    }

    @AllowedRoles(RoleData.ADMIN)
    @PostMapping("/template/pdf")
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
        return new CertificateTemplateUploadResponse(certificateByTemplateService.getTemplateFields(file.getPath()),
            templateName);
    }
}
