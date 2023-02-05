package com.softserve.teachua.controller;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.certificate_template.CertificateTemplateLastModificationDateSavingResponse;
import com.softserve.teachua.dto.certificate_template.CertificateTemplateMetadataTransfer;
import com.softserve.teachua.dto.certificate_template.CertificateTemplatePreview;
import com.softserve.teachua.dto.certificate_template.CertificateTemplateProcessingResponse;
import com.softserve.teachua.dto.certificate_template.CertificateTemplateProfile;
import com.softserve.teachua.dto.certificate_template.CertificateTemplateUploadResponse;
import com.softserve.teachua.service.CertificateTemplateService;
import com.softserve.teachua.service.PdfTemplateService;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * This controller is responsible for managing templates.
 */
@RestController
@Slf4j
public class CertificateTemplateController implements Api {
    private final CertificateTemplateService certificateTemplateService;
    private final PdfTemplateService pdfTemplateService;

    public CertificateTemplateController(CertificateTemplateService certificateTemplateService,
                                         PdfTemplateService pdfTemplateService) {
        this.certificateTemplateService = certificateTemplateService;
        this.pdfTemplateService = pdfTemplateService;
    }

    /**
     * The method saves template to database.
     *
     * @param createCertificateTemplate - {@code CreateCertificateTemplate} read from form.
     * @return new {@code CertificateTemplateProcessingResponse}
     */
    @PostMapping("/template")
    @AllowedRoles(RoleData.ADMIN)
    public CertificateTemplateProcessingResponse createTemplate(
            @Valid @RequestBody CertificateTemplateProfile createCertificateTemplate) {
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
     * @return new {@code CertificateTemplateUploadResponse}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @PostMapping("/template/pdf")
    public CertificateTemplateUploadResponse savePdf(@RequestParam("pdf-file") MultipartFile multipartFile) {
        return pdfTemplateService.savePdf(multipartFile);
    }

    /**
     * This endpoint is used to save last modification date for the template uploaded earlier.
     *
     * @return {@code String}
     */
    @AllowedRoles(RoleData.ADMIN)
    @PostMapping("/template/load-metadata")
    public CertificateTemplateLastModificationDateSavingResponse saveLastModifiedDateOfPdf(
            @RequestBody CertificateTemplateMetadataTransfer data) {
        return pdfTemplateService.saveLastModifiedDateOfPdf(data);
    }

    /**
     * This endpoint is used to get certificate template using id.
     *
     * @param id - put template id here.
     * @return {@code CertificateTemplateProfile}
     */
    @AllowedRoles(RoleData.ADMIN)
    @GetMapping("/template/{id}")
    public CertificateTemplatePreview getCertificateTemplate(@PathVariable Integer id) {
        return certificateTemplateService.getTemplateProfileById(id);
    }

    /**
     * Use this endpoint to update some values of template. The controller returns {@code CertificateTemplate}.
     * This feature available only for admins.
     *
     * @param id              - put template id here.
     * @param updatedTemplate - put new parameters here.
     * @return {@code CertificateTemplateProfile} - shows result of updating template.
     */
    @AllowedRoles(RoleData.ADMIN)
    @PutMapping("/template/{id}")
    public CertificateTemplateProcessingResponse updateTemplate(@PathVariable Integer id, @Valid @RequestBody
        CertificateTemplateProfile updatedTemplate) {
        return certificateTemplateService.updateTemplate(id, updatedTemplate);
    }

    /**
     * Use this endpoint to delete template. The controller returns {@code boolean}.
     * This feature available only for admins.
     *
     * @param id - put template id here.
     * @return {@code boolean}
     */
    @AllowedRoles(RoleData.ADMIN)
    @DeleteMapping("/template/{id}")
    public boolean deleteTemplate(@PathVariable Integer id) {
        return certificateTemplateService.deleteTemplateById(id);
    }
}
