package com.softserve.teachua.service;

import com.softserve.teachua.dto.certificate_template.CertificateTemplateMetadataTransfer;
import com.softserve.teachua.dto.certificate_template.CertificateTemplateUploadResponse;
import com.softserve.teachua.dto.certificate_template.CertificateTemplateLastModificationDateSavingResponse;
import org.springframework.web.multipart.MultipartFile;

public interface PdfTemplateService {
    CertificateTemplateUploadResponse savePdf(MultipartFile multipartFile);

    CertificateTemplateLastModificationDateSavingResponse saveLastModifiedDateOfPdf(
            CertificateTemplateMetadataTransfer data);
}
