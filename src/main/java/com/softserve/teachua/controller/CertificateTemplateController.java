package com.softserve.teachua.controller;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.Atemplate.CertificateTemplatePreview;
import com.softserve.teachua.dto.Atemplate.CreateCertificateTemplate;
import com.softserve.teachua.dto.Atemplate.SuccessCreatedCertificateTemplate;
import com.softserve.teachua.service.CertificateTemplateService;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class CertificateTemplateController implements Api {
    private final CertificateTemplateService certificateTemplateService;

    public CertificateTemplateController(CertificateTemplateService certificateTemplateService) {
        this.certificateTemplateService = certificateTemplateService;
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
}
