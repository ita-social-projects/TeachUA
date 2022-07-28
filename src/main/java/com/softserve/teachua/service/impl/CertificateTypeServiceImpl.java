package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.softserve.teachua.model.CertificateType;
import com.softserve.teachua.service.ArchiveMark;
import com.softserve.teachua.service.CertificateTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class CertificateTypeServiceImpl implements CertificateTypeService, ArchiveMark<CertificateType> {
    @Override
    public void archiveModel(CertificateType certificateType) {
        //TODO
    }

    @Override
    public void restoreModel(String archiveObject) throws JsonProcessingException {
        //TODO
    }
}
