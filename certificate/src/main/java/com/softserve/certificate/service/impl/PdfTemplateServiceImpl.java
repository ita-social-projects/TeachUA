package com.softserve.certificate.service.impl;

import com.softserve.certificate.dto.certificate_template.CertificateTemplateLastModificationDateSavingResponse;
import com.softserve.certificate.dto.certificate_template.CertificateTemplateMetadataTransfer;
import com.softserve.certificate.dto.certificate_template.CertificateTemplateUploadResponse;
import com.softserve.certificate.repository.CertificateTemplateRepository;
import com.softserve.certificate.service.CertificateByTemplateService;
import com.softserve.certificate.service.PdfTemplateService;
import com.softserve.clients.exception.FileUploadException;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@SuppressWarnings("squid:S1075") //Suppressed because of project's business logic.
public class PdfTemplateServiceImpl implements PdfTemplateService {
    public static final String CERTIFICATE_TEMPLATES_FOLDER = "certificates/templates/pdf-templates";
    private final CertificateByTemplateService certificateByTemplateService;
    private final CertificateTemplateRepository certificateTemplateRepository;

    @Autowired
    public PdfTemplateServiceImpl(CertificateByTemplateService certificateByTemplateService,
                                  CertificateTemplateRepository certificateTemplateRepository) {
        this.certificateByTemplateService = certificateByTemplateService;
        this.certificateTemplateRepository = certificateTemplateRepository;
    }

    @Override
    public CertificateTemplateUploadResponse savePdf(MultipartFile multipartFile) {
        try {
            File directory =
                    new File(new ClassPathResource("certificates/templates/").getFile().getPath() + "/pdf-templates");

            if (!directory.exists()) {
                directory.mkdir();
            }

            String templateName = Instant.now().toEpochMilli() + ".pdf";
            File file = new File(
                    new ClassPathResource(CERTIFICATE_TEMPLATES_FOLDER).getFile().getPath() + "/" + templateName);

            try (OutputStream os = Files.newOutputStream(Paths.get(
                    new ClassPathResource(CERTIFICATE_TEMPLATES_FOLDER).getFile().getPath() + "/" + templateName))) {
                os.write(multipartFile.getBytes());
            }
            return CertificateTemplateUploadResponse.builder()
                    .fieldsList(certificateByTemplateService.getTemplateFields(file.getPath()))
                    .templateName(templateName).build();
        } catch (IOException e) {
            log.error("Error uploading pdf of template\n{}", ExceptionUtils.getStackTrace(e));
        }

        throw new FileUploadException();
    }

    @Override
    public CertificateTemplateLastModificationDateSavingResponse saveLastModifiedDateOfPdf(
            CertificateTemplateMetadataTransfer data) {
        try {
            List<String> messagesList = new ArrayList<>();
            Path source = Paths.get(new ClassPathResource(CERTIFICATE_TEMPLATES_FOLDER).getFile().getPath() + "/"
                    + data.getTemplateName());
            String targetName = data.getTemplateLastModifiedDate() + ".pdf";

            if (!(new File(new ClassPathResource(CERTIFICATE_TEMPLATES_FOLDER).getFile().getPath() + "/"
                    + targetName).exists())) {
                Files.move(source, source.resolveSibling(targetName));
            } else {
                Files.delete(source);
            }

            if (certificateTemplateRepository.existsByFilePath(targetName)) {
                messagesList.add("Завантажений pdf-файл уже використовується іншим шаблоном!");
            }

            return CertificateTemplateLastModificationDateSavingResponse.builder().messages(messagesList)
                    .filePath(targetName).build();
        } catch (IOException e) {
            log.error("Error saving pdf of template \n{}", ExceptionUtils.getStackTrace(e));
        }

        throw new FileUploadException();
    }
}
