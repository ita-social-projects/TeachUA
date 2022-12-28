package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.certificateTemplate.*;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.CertificateTemplate;
import com.softserve.teachua.repository.CertificateRepository;
import com.softserve.teachua.repository.CertificateTemplateRepository;
import com.softserve.teachua.service.ArchiveMark;
import com.softserve.teachua.service.CertificateTemplateService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class CertificateTemplateServiceImpl implements CertificateTemplateService, ArchiveMark<CertificateTemplate> {
    private static final String TEMPLATE_NOT_FOUND_BY_ID = "Certificate template not found by id: %s";
    private static final String TEMPLATE_NOT_FOUND_BY_TYPE = "Certificate template not found by type: %s";
    private final DtoConverter dtoConverter;

    private final CertificateTemplateRepository certificateTemplateRepository;
    private final CertificateRepository certificateRepository;

    @Autowired
    public CertificateTemplateServiceImpl(DtoConverter dtoConverter,
                                          CertificateTemplateRepository certificateTemplateRepository,
                                          CertificateRepository certificateRepository) {
        this.dtoConverter = dtoConverter;
        this.certificateTemplateRepository = certificateTemplateRepository;
        this.certificateRepository = certificateRepository;
    }

    @Override
    public void archiveModel(CertificateTemplate certificateType) {
        // TODO
    }

    @Override
    public void restoreModel(String archiveObject) throws JsonProcessingException {
        // TODO
    }

    @Override
    public CertificateTemplate getTemplateById(Integer id) {
        return certificateTemplateRepository.findById(id)
            .orElseThrow(() -> new NotExistException(String.format(TEMPLATE_NOT_FOUND_BY_ID, id)));
    }

    @Override
    public CertificateTemplateProfile getTemplateProfileById(Integer id) {
        CertificateTemplate template = certificateTemplateRepository.findById(id)
            .orElseThrow(() -> new NotExistException(String.format(TEMPLATE_NOT_FOUND_BY_ID, id)));
        CertificateTemplateProfile templateProfile = new CertificateTemplateProfile();
        BeanUtils.copyProperties(template, templateProfile);
        templateProfile.setUsed(certificateRepository.existsByTemplateId(id));
        return templateProfile;
    }

    @Override
    public CertificateTemplate getTemplateByType(Integer type) {
        int targetId = type;
        if (type == 1) {
            targetId = 2;
        } else if (type == 2) {
            targetId = 3;
        } else if (type == 3) {
            targetId = 1;
        }
        return certificateTemplateRepository.findById(targetId)
            .orElseThrow(() -> new NotExistException(String.format(TEMPLATE_NOT_FOUND_BY_TYPE, type)));
    }

    @Override
    public CertificateTemplate addTemplate(CertificateTemplate certificateTemplate) {
        return certificateTemplateRepository.save(certificateTemplate);
    }

    @Override
    public CertificateTemplateCreationResponse addTemplate(CreateCertificateTemplate createCertificateTemplate) {
        List<String> messagesList = new ArrayList<>();
        if (certificateTemplateRepository.existsByName(createCertificateTemplate.getName())) {
            messagesList.add("Шаблон із такою назвою уже існує!");
        }
        if (certificateTemplateRepository.existsByFilePath(createCertificateTemplate.getFilePath())) {
            messagesList.add("Завантажений pdf-файл уже використовується іншим шаблоном!");
        }
        if (!messagesList.isEmpty()) {
            return CertificateTemplateCreationResponse.builder()
                .isValid(false)
                .messages(messagesList)
                .build();
        }
        CertificateTemplate certificateTemplate =
            dtoConverter.convertToEntity(createCertificateTemplate, new CertificateTemplate());
        return CertificateTemplateCreationResponse.builder()
            .isValid(true)
            .template(dtoConverter.convertToDto(certificateTemplateRepository.save(certificateTemplate),
                SuccessCreatedCertificateTemplate.class))
            .build();
    }

    @Override
    public List<CertificateTemplatePreview> getAllTemplates() {
        List<CertificateTemplatePreview> resultList = new LinkedList<>();
        List<CertificateTemplate> list;
        list = certificateTemplateRepository.findByIdGreaterThanOrderByIdDesc(3);
        list.forEach(
            (challenge -> resultList.add(dtoConverter.convertToDto(challenge, CertificateTemplatePreview.class))));
        return resultList;
    }

    @Override
    public CertificateTemplate getTemplateByFilePath(String filePath) {
        return certificateTemplateRepository.getCertificateTemplateByFilePath(filePath);
    }

    @Override
    public CertificateTemplateUpdationResponse updateTemplate(Integer id, UpdateCertificateTemplate updatedTemplate) {
        List<String> messagesList = new ArrayList<>();
        CertificateTemplate template = getTemplateById(id);

        if (!template.getName().equals(updatedTemplate.getName()) &&
            certificateTemplateRepository.existsByName(updatedTemplate.getName())) {
            messagesList.add("Шаблон із такою назвою уже існує!");
            return CertificateTemplateUpdationResponse.builder().isUpdated(false).messages(messagesList).build();
        }

        BeanUtils.copyProperties(updatedTemplate, template);

        return CertificateTemplateUpdationResponse.builder()
            .isUpdated(true)
            .template(certificateTemplateRepository.save(template))
            .build();
    }

    @Override
    public boolean deleteTemplateById(Integer id) {
        if (!certificateRepository.existsByTemplateId(id)) {
            Optional<CertificateTemplate> templateOptional = certificateTemplateRepository.findById(id);
            CertificateTemplate template;
            if (templateOptional.isPresent()) {
                template = templateOptional.get();
                try {
                    Path source = Paths.get(
                        new ClassPathResource("certificates/templates/pdf-templates").getFile().getPath() + "/" +
                            template.getFilePath());
                    Files.delete(source);
                } catch (IOException e) {
                    log.error("Failed to delete certificate template: error while deleting pdf template " + template);
                    e.printStackTrace();
                    return false;
                }
                certificateTemplateRepository.deleteById(id);
                return true;
            }
        }
        return false;
    }

}
