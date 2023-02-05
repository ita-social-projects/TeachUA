package com.softserve.teachua.service;

import com.softserve.teachua.dto.certificateTemplate.CertificateTemplateLastModificationDateSavingResponse;
import com.softserve.teachua.dto.certificateTemplate.CertificateTemplateMetadataTransfer;
import com.softserve.teachua.dto.certificateTemplate.CertificateTemplateUploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface PdfTemplateService {
    CertificateTemplateUploadResponse savePdf(MultipartFile multipartFile);

    CertificateTemplateLastModificationDateSavingResponse saveLastModifiedDateOfPdf(
            CertificateTemplateMetadataTransfer data);
}
