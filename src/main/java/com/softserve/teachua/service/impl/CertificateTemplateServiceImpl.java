package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.Atemplate.CreateCertificateTemplate;
import com.softserve.teachua.dto.Atemplate.SuccessCreatedCertificateTemplate;
import com.softserve.teachua.dto.Atemplate.CertificateTemplatePreview;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.CertificateTemplate;
import com.softserve.teachua.repository.CertificateTemplateRepository;
import com.softserve.teachua.service.ArchiveMark;
import com.softserve.teachua.service.CertificateTemplateService;
import java.util.LinkedList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public CertificateTemplateServiceImpl(DtoConverter dtoConverter,
                                          CertificateTemplateRepository certificateTemplateRepository) {
        this.dtoConverter = dtoConverter;
        this.certificateTemplateRepository = certificateTemplateRepository;
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
    public CertificateTemplate getTemplateByType(Integer type) {
        return certificateTemplateRepository.findByCertificateType(type)
                .orElseThrow(() -> new NotExistException(String.format(TEMPLATE_NOT_FOUND_BY_TYPE, type)));
    }

    @Override
    public CertificateTemplate addTemplate(CertificateTemplate certificateTemplate) {
        return certificateTemplateRepository.save(certificateTemplate);
    }

    @Override
    public SuccessCreatedCertificateTemplate addTemplate(CreateCertificateTemplate createCertificateTemplate) {
        CertificateTemplate certificateTemplate =
            dtoConverter.convertToEntity(createCertificateTemplate, new CertificateTemplate());
        return dtoConverter.convertToDto(certificateTemplateRepository.save(certificateTemplate),
            SuccessCreatedCertificateTemplate.class);
    }

    @Override
    public List<CertificateTemplatePreview> getAllTemplates() {
        List<CertificateTemplatePreview> resultList = new LinkedList<>();
        List<CertificateTemplate> list;
        list = certificateTemplateRepository.findByIdGreaterThanOrderByIdDesc(3);
        list.forEach((challenge -> resultList.add(dtoConverter.convertToDto(challenge, CertificateTemplatePreview.class))));
        return resultList;
    }
}
