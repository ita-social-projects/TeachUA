package com.softserve.teachua.service;

import com.softserve.teachua.dto.certificate.CertificateTransfer;
import java.io.IOException;
import java.util.List;

public interface CertificateByTemplateService {
    List<String> getTemplateFields(String templatePath) throws IOException;
    String createCertificateByTemplate(CertificateTransfer transfer) throws IOException;
}
