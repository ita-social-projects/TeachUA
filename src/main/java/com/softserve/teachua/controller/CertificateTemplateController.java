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
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * This controller is responsible for managing templates
 */
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

    /**
     * The method saves template to database.
     *
     * @param createCertificateTemplate
     *            - {@code CreateCertificateTemplate} read from form.
     *
     * @return new {@code SuccessCreatedCertificateTemplate}
     */
    @PostMapping("/template")
    @AllowedRoles(RoleData.ADMIN)
    public SuccessCreatedCertificateTemplate createTemplate(
        @Valid @RequestBody CreateCertificateTemplate createCertificateTemplate) {
        return certificateTemplateService.addTemplate(createCertificateTemplate);
    }

    /**
     * This endpoint is used to get all templates.
     *
     * @return {@code List<CertificateTemplatePreview>}
     */
    @GetMapping("/templates")
    @AllowedRoles(RoleData.ADMIN)
    public List<CertificateTemplatePreview> getAllTemplates() {
        return certificateTemplateService.getAllTemplates();
    }

    /**
     * The method uploads pdf template file and returns {@code CertificateTemplateUploadResponse}.
     *
     * @param multipartFile - pdf template file.
     * @return new {@code }.
     */
    @AllowedRoles(RoleData.ADMIN)
    @PostMapping("/template/pdf")
    public CertificateTemplateUploadResponse uploadPdf(@RequestParam("pdf-file") MultipartFile multipartFile)
        throws IOException {
        File directory =
            new File(new ClassPathResource("certificates/templates/").getFile().getPath() + "/pdf-templates");
        if (!directory.exists()) {
            directory.mkdir();
        }
        String templateName = Instant.now().toEpochMilli() + ".pdf";
        File file =
            new File(new ClassPathResource("certificates/templates/pdf-templates").getFile().getPath() + "/" + templateName);

        try (OutputStream os = Files.newOutputStream(
            Paths.get(new ClassPathResource("certificates/templates/pdf-templates").getFile().getPath() + "/" + templateName))) {
            os.write(multipartFile.getBytes());
        }
        return new CertificateTemplateUploadResponse(certificateByTemplateService.getTemplateFields(file.getPath()),
            templateName);
    }
}
