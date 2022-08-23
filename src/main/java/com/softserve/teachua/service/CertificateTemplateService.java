package com.softserve.teachua.service;

import com.softserve.teachua.model.CertificateTemplate;

/**
 * This interface contains all needed methods to manage certificate templates.
 */

public interface CertificateTemplateService {

    /**
     * The method returns {@link CertificateTemplate} by id
     *
     * @param id - put template id
     * @return new {@code CertificateTemplate}
     */
    CertificateTemplate getTemplateById(Integer id);

    /**
     * The method returns {@link CertificateTemplate} by type
     *
     * @param type - put template type
     * @return new {@code Certificate}
     */
    CertificateTemplate getTemplateByType(Integer type);

    /**
     * The method returns {@link CertificateTemplate} if template successfully added
     *
     * @param certificateTemplate - put body of {@code CertificateTemplate}
     * @return new {@code Certificate}
     */
    CertificateTemplate addTemplate(CertificateTemplate certificateTemplate);
}
