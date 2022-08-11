package com.softserve.teachua.service;

import com.softserve.teachua.model.CertificateTemplate;

public interface CertificateTemplateService {
    //TODO

    CertificateTemplate getTemplateById(Integer id);

    CertificateTemplate getTemplateByType(Integer type);
}
