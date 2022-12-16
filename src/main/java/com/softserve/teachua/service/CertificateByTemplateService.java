package com.softserve.teachua.service;

import com.softserve.teachua.dto.certificate.CertificateTransfer;
import com.softserve.teachua.dto.certificateByTemplate.CertificateByTemplateTransfer;
import java.io.IOException;

public interface CertificateByTemplateService {
    String createCertificateByTemplate(CertificateTransfer transfer) throws IOException;
}
