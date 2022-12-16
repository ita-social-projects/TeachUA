package com.softserve.teachua.service;

import com.softserve.teachua.dto.template.CreateCertificateTemplate;
import com.softserve.teachua.dto.template.SuccessCreatedCertificateTemplate;
import com.softserve.teachua.dto.template.CertificateTemplatePreview;
import com.softserve.teachua.model.CertificateTemplate;
import java.util.List;

/**
 * This interface contains all needed methods to manage certificate templates.
 */

public interface CertificateTemplateService {

    /**
     * This method returns entity of {@code CertificateTemplate} found by id
     *
     * @param id
     *            - put CertificateTemplate id
     *
     * @return new {@code CertificateTemplate}
     */
    CertificateTemplate getTemplateById(Integer id);

    /**
     * This method returns entity of {@code CertificateTemplate} found by type
     *
     * @param type
     *            - put CertificateTemplate type
     *
     * @return new {@code CertificateTemplate}
     */
    CertificateTemplate getTemplateByType(Integer type);

    /**
     * This method returns entity of {@code CertificateTemplate} if template successfully added
     *
     * @param certificateTemplate
     *            - put body of {@code CertificateTemplate}
     *
     * @return new {@code CertificateTemplate}
     */
    CertificateTemplate addTemplate(CertificateTemplate certificateTemplate);
    SuccessCreatedCertificateTemplate addTemplate(CreateCertificateTemplate certificateTemplate);
    List<CertificateTemplatePreview> getAllTemplates();
    CertificateTemplate getTemplateByFilePath(String filePath);

}
