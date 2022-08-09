package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.softserve.teachua.model.CertificateDates;
import com.softserve.teachua.service.ArchiveMark;
import com.softserve.teachua.service.CertificateDatesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class CertificateDatesServiceImpl implements CertificateDatesService, ArchiveMark<CertificateDates> {
    @Override
    public void archiveModel(CertificateDates certificateDates) {
        //TODO
    }

    @Override
    public void restoreModel(String archiveObject) throws JsonProcessingException {
        //TODO
    }
}
