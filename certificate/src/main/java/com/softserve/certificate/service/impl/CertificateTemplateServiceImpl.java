package com.softserve.certificate.service.impl;

import com.softserve.certificate.constants.MessageType;
import com.softserve.certificate.converter.DtoConverter;
import com.softserve.certificate.dto.certificate_template.CertificateTemplatePreview;
import com.softserve.certificate.dto.certificate_template.CertificateTemplateProcessingResponse;
import com.softserve.certificate.dto.certificate_template.CertificateTemplateProfile;
import com.softserve.certificate.model.CertificateTemplate;
import com.softserve.certificate.repository.CertificateRepository;
import com.softserve.certificate.repository.CertificateTemplateRepository;
import static com.softserve.certificate.service.CertificateService.LAST_JRXML_TEMPLATE_ID;
import com.softserve.certificate.service.CertificateTemplateService;
import com.softserve.certificate.service.CertificateTypeService;
import static com.softserve.certificate.service.impl.PdfTemplateServiceImpl.CERTIFICATE_TEMPLATES_FOLDER;
import com.softserve.commons.exception.NotExistException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class CertificateTemplateServiceImpl implements CertificateTemplateService {
    private static final String TEMPLATE_NOT_FOUND_BY_ID = "Certificate template not found by id: %s";
    private static final String TEMPLATE_NOT_FOUND_BY_TYPE = "Certificate template not found by type: %s";
    private static final String NAME_ALREADY_EXISTS_MESSAGE = "Шаблон із такою назвою уже існує!";
    private static final String FILE_ALREADY_EXISTS_MESSAGE =
            "Завантажений pdf-файл уже використовується іншим шаблоном!";
    private final DtoConverter dtoConverter;
    private final CertificateTemplateRepository certificateTemplateRepository;
    private final CertificateRepository certificateRepository;
    private final CertificateTypeService certificateTypeService;
    private String certificateTemplateUrl;

    @Autowired
    public CertificateTemplateServiceImpl(DtoConverter dtoConverter,
                                          CertificateTemplateRepository certificateTemplateRepository,
                                          CertificateRepository certificateRepository,
                                          CertificateTypeService certificateTypeService) throws IOException {
        this.dtoConverter = dtoConverter;
        this.certificateTemplateRepository = certificateTemplateRepository;
        this.certificateRepository = certificateRepository;
        this.certificateTypeService = certificateTypeService;
        setCertificateTemplateUrl();
    }

    private void setCertificateTemplateUrl() throws IOException {
        Files.createDirectories(Paths.get(new ClassPathResource("/").getFile().getPath() + "/certificates/templates"
                + "/pdf-templates"));

        certificateTemplateUrl = new ClassPathResource(CERTIFICATE_TEMPLATES_FOLDER).getFile().getPath();
    }

    @Override
    public CertificateTemplate getTemplateById(Integer id) {
        return certificateTemplateRepository.findById(id)
                .orElseThrow(() -> new NotExistException(String.format(TEMPLATE_NOT_FOUND_BY_ID, id)));
    }

    @Override
    public CertificateTemplatePreview getTemplateProfileById(Integer id) {
        CertificateTemplate template = certificateTemplateRepository.findById(id)
                .orElseThrow(() -> new NotExistException(String.format(TEMPLATE_NOT_FOUND_BY_ID, id)));
        CertificateTemplatePreview templatePreview = new CertificateTemplatePreview();
        BeanUtils.copyProperties(template, templatePreview);
        templatePreview.setUsed(certificateRepository.existsByTemplateId(id));
        return templatePreview;
    }

    @Override
    public CertificateTemplate getTemplateByType(Integer type) {
        return certificateTemplateRepository.findFirstByCertificateTypeId(type)
                .orElseThrow(() -> new NotExistException(String.format(TEMPLATE_NOT_FOUND_BY_TYPE, type)));
    }

    @Override
    public CertificateTemplate addTemplate(CertificateTemplate certificateTemplate) {
        return certificateTemplateRepository.save(certificateTemplate);
    }

    @Override
    public CertificateTemplateProcessingResponse addTemplate(CertificateTemplateProfile createCertificateTemplate) {
        List<Pair<String, MessageType>> messagesList = new ArrayList<>();
        CertificateTemplate savedTemplate = null;
        boolean errors = false;
        if (certificateTemplateRepository.existsByNameIgnoreCase(createCertificateTemplate.getName())) {
            messagesList.add(Pair.of(NAME_ALREADY_EXISTS_MESSAGE, MessageType.ERROR));
            errors = true;
        }
        if (certificateTemplateRepository.existsByFilePath(createCertificateTemplate.getFilePath())) {
            messagesList.add(Pair.of(FILE_ALREADY_EXISTS_MESSAGE, MessageType.ERROR));
            errors = true;
        }

        if (!errors) {
            CertificateTemplate certificateTemplate =
                    dtoConverter.convertToEntity(createCertificateTemplate, new CertificateTemplate());
            certificateTemplate.setCertificateType(certificateTypeService.getCertificateTypeByCodeNumber(
                    createCertificateTemplate.getCertificateType()));
            savedTemplate = certificateTemplateRepository.save(certificateTemplate);
        }

        return CertificateTemplateProcessingResponse.builder().template(savedTemplate).messages(messagesList).build();
    }

    @Override
    public List<CertificateTemplatePreview> getAllTemplates() {
        List<CertificateTemplatePreview> resultList = new LinkedList<>();
        List<CertificateTemplate> list;
        list = certificateTemplateRepository.findByIdGreaterThanOrderByIdDesc(LAST_JRXML_TEMPLATE_ID);
        list.forEach(
                (template -> resultList.add(dtoConverter.convertToDto(template, CertificateTemplatePreview.class))));
        return resultList;
    }

    @Override
    public CertificateTemplate getTemplateByFilePath(String filePath) {
        return certificateTemplateRepository.getCertificateTemplateByFilePath(filePath);
    }

    @Override
    public CertificateTemplateProcessingResponse updateTemplate(Integer id,
                                                                CertificateTemplateProfile updatedTemplate) {
        List<Pair<String, MessageType>> messagesList = new ArrayList<>();
        CertificateTemplate targetTemplate = getTemplateById(id);
        CertificateTemplate finalTemplate = null;
        boolean errors = false;

        if (!targetTemplate.getName().equals(updatedTemplate.getName())
                && certificateTemplateRepository.existsByNameIgnoreCase(updatedTemplate.getName())) {
            messagesList.add(Pair.of(NAME_ALREADY_EXISTS_MESSAGE, MessageType.ERROR));
            errors = true;
        }
        if (!targetTemplate.getFilePath().equals(updatedTemplate.getFilePath())
                && certificateTemplateRepository.existsByFilePath(updatedTemplate.getFilePath())) {
            messagesList.add(Pair.of(FILE_ALREADY_EXISTS_MESSAGE, MessageType.ERROR));
            errors = true;
        }

        if (!errors) {
            BeanUtils.copyProperties(updatedTemplate, targetTemplate);
            targetTemplate.setCertificateType(
                    certificateTypeService.getCertificateTypeByCodeNumber(updatedTemplate.getCertificateType()));
            finalTemplate = certificateTemplateRepository.save(targetTemplate);
        }
        return CertificateTemplateProcessingResponse.builder().messages(messagesList).template(finalTemplate).build();
    }

    @Override
    public boolean deleteTemplateById(Integer id) {
        boolean isDeleted = false;
        if (!certificateRepository.existsByTemplateId(id)) {
            Optional<CertificateTemplate> foundedTemplate = certificateTemplateRepository.findById(id);
            if (foundedTemplate.isPresent()) {
                CertificateTemplate template = foundedTemplate.get();
                Path source = Paths.get(certificateTemplateUrl, template.getFilePath());
                try {
                    isDeleted = Files.deleteIfExists(source);
                    if (isDeleted) {
                        certificateTemplateRepository.deleteById(id);
                    }
                } catch (IOException e) {
                    log.error("Failed to delete certificate template: error while deleting pdf template {}\n{}",
                            template, ExceptionUtils.getStackTrace(e));
                }
            }
        }
        return isDeleted;
    }
}
