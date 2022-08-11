package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.CertificateTemplate;
import com.softserve.teachua.repository.CertificateTemplateRepository;
import com.softserve.teachua.service.ArchiveMark;
import com.softserve.teachua.service.CertificateTemplateService;
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

    private final CertificateTemplateRepository certificateTemplateRepository;

    @Autowired
    public CertificateTemplateServiceImpl(CertificateTemplateRepository certificateTemplateRepository) {
        this.certificateTemplateRepository = certificateTemplateRepository;
    }

    @Override
    public void archiveModel(CertificateTemplate certificateType) {
        //TODO
    }

    @Override
    public void restoreModel(String archiveObject) throws JsonProcessingException {
        //TODO
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
}
