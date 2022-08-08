package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.softserve.teachua.model.CertificateTemplate;
import com.softserve.teachua.service.ArchiveMark;
import com.softserve.teachua.service.CertificateTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class CertificateTemplateServiceImpl implements CertificateTemplateService, ArchiveMark<CertificateTemplate> {
    @Override
    public void archiveModel(CertificateTemplate certificateType) {
        //TODO
    }

    @Override
    public void restoreModel(String archiveObject) throws JsonProcessingException {
        //TODO
    }
}
