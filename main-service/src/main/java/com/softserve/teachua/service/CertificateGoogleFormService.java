package com.softserve.teachua.service;

import com.softserve.teachua.dto.certificate_by_template.CertificateByTemplateSavingResponse;
import com.softserve.teachua.dto.certificate_by_template.CertificateByTemplateTransfer;

public interface CertificateGoogleFormService {
    CertificateByTemplateSavingResponse saveGoogleFormCertificateData(CertificateByTemplateTransfer data);
}
