package com.softserve.certificate.service;

import com.softserve.certificate.dto.certificate_by_template.CertificateByTemplateSavingResponse;
import com.softserve.certificate.dto.certificate_by_template.CertificateByTemplateTransfer;

public interface CertificateGoogleFormService {
    CertificateByTemplateSavingResponse saveGoogleFormCertificateData(CertificateByTemplateTransfer data);
}
