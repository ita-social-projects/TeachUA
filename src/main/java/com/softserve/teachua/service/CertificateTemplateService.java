package com.softserve.teachua.service;

import com.softserve.teachua.dto.certificateTemplate.CertificateTemplateCreationResponse;
import com.softserve.teachua.dto.certificateTemplate.CertificateTemplatePreview;
import com.softserve.teachua.dto.certificateTemplate.CertificateTemplateProfile;
import com.softserve.teachua.dto.certificateTemplate.CertificateTemplateUpdationResponse;
import com.softserve.teachua.dto.certificateTemplate.CreateCertificateTemplate;
import com.softserve.teachua.dto.certificateTemplate.UpdateCertificateTemplate;
import com.softserve.teachua.model.CertificateTemplate;
import java.util.List;

/**
 * This interface contains all needed methods to manage certificate templates.
 */

public interface CertificateTemplateService {
    /**
     * This method returns entity of {@code CertificateTemplate} found by id
     *
     * @param id - put CertificateTemplate id
     * @return new {@code CertificateTemplate}
     */
    CertificateTemplate getTemplateById(Integer id);

    /**
     * This method returns entity of {@code CertificateTemplate} found by type
     *
     * @param type - put CertificateTemplate type
     * @return new {@code CertificateTemplate}
     */
    CertificateTemplate getTemplateByType(Integer type);

    /**
     * This method returns entity of {@code CertificateTemplate} if template successfully added
     *
     * @param certificateTemplate - put body of {@code CertificateTemplate}
     * @return new {@code CertificateTemplate}
     */
    CertificateTemplate addTemplate(CertificateTemplate certificateTemplate);

    /**
     * This method returns {@code CertificateTemplateCreationResponse} if template successfully added
     *
     * @param certificateTemplate - put body of {@code CreateCertificateTemplate}
     * @return new {@code CertificateTemplateCreationResponse}
     */
    CertificateTemplateCreationResponse addTemplate(CreateCertificateTemplate certificateTemplate);

    /**
     * This method returns List of {@code CertificateTemplatePreview} - all templates
     *
     * @return new {@code List<CertificateTemplatePreview>}
     */
    List<CertificateTemplatePreview> getAllTemplates();

    /**
     * This method returns entity of {@code CertificateTemplate} found by file path
     *
     * @param filePath put file path
     * @return new {@code CertificateTemplate}
     */
    CertificateTemplate getTemplateByFilePath(String filePath);

    /**
     * This method updates {@code CertificateTemplate} found by id
     *
     * @param id              put CertificateTemplate id
     * @param updatedTemplate put new data in this dto
     * @return new {@code CertificateTemplateUpdationResponse}
     */
    CertificateTemplateUpdationResponse updateTemplate(Integer id, UpdateCertificateTemplate updatedTemplate);

    /**
     * This method deletes {@code CertificateTemplate} found by id
     *
     * @param id put CertificateTemplate id
     * @return {@code boolean}
     */
    boolean deleteTemplateById(Integer id);

    CertificateTemplateProfile getTemplateProfileById(Integer id);
}
