package com.softserve.certificate.service;

import com.softserve.certificate.dto.certificate_template.CertificateTemplateLastModificationDateSavingResponse;
import com.softserve.certificate.dto.certificate_template.CertificateTemplateMetadataTransfer;
import com.softserve.certificate.dto.certificate_template.CertificateTemplateUploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface PdfTemplateService {
    CertificateTemplateUploadResponse savePdf(MultipartFile multipartFile);

    CertificateTemplateLastModificationDateSavingResponse saveLastModifiedDateOfPdf(
            CertificateTemplateMetadataTransfer data);
}
