package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.softserve.teachua.model.Certificate;
import com.softserve.teachua.service.ArchiveMark;
import com.softserve.teachua.service.CertificateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class CertificateServiceImpl implements CertificateService, ArchiveMark<Certificate> {
    @Override
    public void archiveModel(Certificate certificate) {
        //TODO
    }

    @Override
    public void restoreModel(String archiveObject) throws JsonProcessingException {
        //TODO
    }
    //TODO
}
