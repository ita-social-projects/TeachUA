package com.softserve.certificate.service;

import com.softserve.certificate.dto.certificate.CertificateTransfer;
import java.io.IOException;
import java.util.List;

public interface CertificateByTemplateService {
    List<String> getTemplateFields(String templatePath) throws IOException;

    String createCertificateByTemplate(CertificateTransfer transfer) throws IOException;
}
