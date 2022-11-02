package com.softserve.teachua.service.impl;

import com.softserve.teachua.model.CertificateDates;
import com.softserve.teachua.model.CertificateTemplate;
import com.softserve.teachua.repository.CertificateTemplateRepository;
import com.softserve.teachua.service.CertificateDataMoverService;
import com.softserve.teachua.service.CertificateDatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CertificateDataMoverServiceImpl implements CertificateDataMoverService {

    private final CertificateDatesService certificateDatesService;

    private final CertificateTemplateRepository certificateTemplateRepository;

    @Autowired
    public CertificateDataMoverServiceImpl(CertificateDatesService certificateDatesService,
                                               CertificateTemplateRepository certificateTemplateRepository) {
        this.certificateDatesService = certificateDatesService;
        this.certificateTemplateRepository = certificateTemplateRepository;
    }

    public List<CertificateTemplate> moveData() {
        CertificateDates certificateDates = certificateDatesService.getCertificateDatesById(1);
        List<CertificateTemplate> certificateTemplateList = certificateTemplateRepository.findAll();
        for (CertificateTemplate certificateTemplate : certificateTemplateList) {
            certificateTemplate.setCourseDescription(certificateDates.getCourseDescription());
            certificateTemplate.setPicturePath(certificateDates.getPicturePath());
            certificateTemplate.setProjectDescription(certificateDates.getProjectDescription());
            certificateTemplateRepository.save(certificateTemplate);
        };
        return certificateTemplateList;
    }

}
