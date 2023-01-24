package com.softserve.teachua.service.impl;

import com.softserve.teachua.dto.certificateTemplate.CertificateTemplateLastModificationDateSavingResponse;
import com.softserve.teachua.dto.certificateTemplate.CertificateTemplateMetadataTransfer;
import com.softserve.teachua.dto.certificateTemplate.CertificateTemplateUploadResponse;
import com.softserve.teachua.repository.CertificateTemplateRepository;
import com.softserve.teachua.service.CertificateByTemplateService;
import com.softserve.teachua.service.PdfTemplateService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class PdfTemplateServiceImpl implements PdfTemplateService {
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
                    new ClassPathResource("certificates/templates/pdf-templates").getFile().getPath() + "/"
                            + templateName);

            try (OutputStream os = Files.newOutputStream(
                    Paths.get(new ClassPathResource("certificates/templates/pdf-templates").getFile().getPath() + "/"
                            + templateName))) {
                os.write(multipartFile.getBytes());
            }
            return CertificateTemplateUploadResponse.builder()
                    .fieldsList(certificateByTemplateService.getTemplateFields(file.getPath()))
                    .templateName(templateName)
                    .build();
        } catch (IOException e) {
            log.error("Error uploading pdf of template");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public CertificateTemplateLastModificationDateSavingResponse saveLastModifiedDateOfPdf(
            CertificateTemplateMetadataTransfer data) {
        try {
            List<String> messagesList = new ArrayList<>();
            Path source = Paths.get(
                    new ClassPathResource("certificates/templates/pdf-templates").getFile().getPath() + "/"
                            + data.getTemplateName());
            String targetName = data.getTemplateLastModifiedDate() + ".pdf";

            if (!(new File(
                    new ClassPathResource("certificates/templates/pdf-templates").getFile().getPath() + "/"
                            + targetName).exists())) {
                Files.move(source, source.resolveSibling(targetName));
            } else {
                Files.delete(source);
            }

            if (certificateTemplateRepository.existsByFilePath(targetName)) {
                messagesList.add("Завантажений pdf-файл уже використовується іншим шаблоном!");
            }

            return CertificateTemplateLastModificationDateSavingResponse.builder()
                    .messages(messagesList)
                    .filePath(targetName)
                    .build();
        } catch (IOException e) {
            log.error("Error saving pdf of template");
            e.printStackTrace();
        }

        return null;
    }
}
