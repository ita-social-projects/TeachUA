package com.softserve.teachua.service.impl;

import com.softserve.teachua.dto.certificate.CertificateDataRequest;
import com.softserve.teachua.dto.certificateExcel.CertificateExcel;
import com.softserve.teachua.model.Certificate;
import com.softserve.teachua.model.CertificateDates;
import com.softserve.teachua.repository.CertificateRepository;
import com.softserve.teachua.service.CertificateDataLoaderService;
import com.softserve.teachua.service.CertificateDatesService;
import com.softserve.teachua.service.CertificateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class CertificateDataLoaderServiceImpl implements CertificateDataLoaderService {
    private final static String DATE_FORMAT = "dd.MM.YYYY";
    private final CertificateService certificateService;
    private final CertificateDatesService certificateDatesService;
    private final CertificateRepository certificateRepository;

    @Autowired
    public CertificateDataLoaderServiceImpl(CertificateService certificateService, CertificateDatesService certificateDatesService, CertificateRepository certificateRepository) {
        this.certificateService = certificateService;
        this.certificateDatesService = certificateDatesService;
        this.certificateRepository = certificateRepository;
    }

    @Override
    public void saveToDatabase(CertificateDataRequest data) {
        CertificateDates dates = CertificateDates.builder()
            .date(data.getExcelList().get(0).getDateIssued().format(DateTimeFormatter.ofPattern(DATE_FORMAT)))
            .hours(data.getHours())
            .duration(data.getDuration())
            .courseNumber(data.getCourseNumber())
            .build();
        saveDates(dates);
        saveCertificates(data, dates);
    }

    private void saveCertificates(CertificateDataRequest data, CertificateDates dates) {
        for (CertificateExcel certificateExcel : data.getExcelList()) {
            Certificate certificate = Certificate.builder()
                    .userName(certificateExcel.getName())
                    .userEmail(certificateExcel.getEmail())
                    .certificateType(data.getTemplate())
                    .certificateType(data.getCertificateType())
                    .dates(dates)
                    .build();
            System.out.println(certificate);
            if (!certificateRepository.existsByUserName(certificate.getUserName())) {
                certificateService.addCertificate(certificate);
            }
            else {
                Certificate certificateFound = certificateService.getCertificateByUserName(certificate.getUserName());
                if (!certificate.getUserEmail().equals(certificateFound.getUserEmail())) {
                    certificate.setSendStatus(false);
                    certificateService.updateCertificateEmail(certificateFound.getId(), certificate);
                }
            }
        }
    }
    private void saveDates(CertificateDates dates) {
        certificateDatesService.addCertificateDates(dates);
    }
}
