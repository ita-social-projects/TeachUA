package com.softserve.teachua.service;

import com.softserve.teachua.dto.certificate_template.CertificateTemplatePreview;
import com.softserve.teachua.dto.certificate_template.CertificateTemplateProcessingResponse;
import com.softserve.teachua.dto.certificate_template.CertificateTemplateProfile;
import com.softserve.teachua.model.CertificateTemplate;
import java.util.List;

/**
 * This interface contains all needed methods to manage certificate templates.
 */

public interface CertificateTemplateService {
    /**
     * Get an entity of {@code CertificateTemplate} found by id.
     *
     * @param id - put CertificateTemplate id
     * @return new {@code CertificateTemplate}
     */
    CertificateTemplate getTemplateById(Integer id);

    /**
     * Get an entity of {@code CertificateTemplate} found by type.
     *
     * @param type - put CertificateTemplate type
     * @return new {@code CertificateTemplate}
     */
    CertificateTemplate getTemplateByTypeId(Integer type);

    /**
     * Get an entity of {@code CertificateTemplate} if template successfully added.
     *
     * @param certificateTemplate - put body of {@code CertificateTemplate}
     * @return new {@code CertificateTemplate}
     */
    CertificateTemplate addTemplate(CertificateTemplate certificateTemplate);

    /**
     * Get an {@code CertificateTemplateCreationResponse} if template successfully added.
     *
     * @param certificateTemplate - put body of {@code CreateCertificateTemplate}
     * @return new {@code CertificateTemplateCreationResponse}
     */
    CertificateTemplateProcessingResponse addTemplate(CertificateTemplateProfile certificateTemplate);

    /**
     * Get a List of {@code CertificateTemplatePreview} - all templates.
     *
     * @return new {@code List<CertificateTemplatePreview>}
     */
    List<CertificateTemplatePreview> getAllTemplates();

    /**
     * Get an entity of {@code CertificateTemplate} found by file path.
     *
     * @param filePath put a file path
     * @return new {@code CertificateTemplate}
     */
    CertificateTemplate getTemplateByFilePath(String filePath);

    /**
     * This method updates {@code CertificateTemplate} found by id.
     *
     * @param id              put CertificateTemplate id
     * @param updatedTemplate put new data in this dto
     * @return new {@code CertificateTemplateProcessingResponse}
     */
    CertificateTemplateProcessingResponse updateTemplate(Integer id, CertificateTemplateProfile updatedTemplate);

    /**
     * This method deletes {@code CertificateTemplate} found by id.
     *
     * @param id put CertificateTemplate id
     */
    boolean deleteTemplateById(Integer id);

    CertificateTemplatePreview getTemplateProfileById(Integer id);
}
