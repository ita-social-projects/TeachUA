package com.softserve.teachua.service.impl;

import com.softserve.teachua.dto.certificate.CertificateDataRequest;
import com.softserve.teachua.dto.certificateExcel.CertificateExcel;
import com.softserve.teachua.model.Certificate;
import com.softserve.teachua.model.CertificateDates;
import com.softserve.teachua.repository.CertificateDatesRepository;
import com.softserve.teachua.repository.CertificateRepository;
import com.softserve.teachua.service.CertificateDataLoaderService;
import com.softserve.teachua.service.CertificateDatesService;
import com.softserve.teachua.service.CertificateService;
import com.softserve.teachua.service.CertificateTemplateService;
import com.softserve.teachua.utils.CertificateContentDecorator;
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
    private final CertificateTemplateService templateService;
    private final CertificateRepository certificateRepository;
    private final CertificateDatesRepository datesRepository;
    private final CertificateContentDecorator decorator;

    @Autowired
    public CertificateDataLoaderServiceImpl(CertificateService certificateService, CertificateDatesService certificateDatesService, CertificateTemplateService templateService, CertificateRepository certificateRepository, CertificateDatesRepository datesRepository, CertificateContentDecorator decorator) {
        this.certificateService = certificateService;
        this.certificateDatesService = certificateDatesService;
        this.templateService = templateService;
        this.certificateRepository = certificateRepository;
        this.datesRepository = datesRepository;
        this.decorator = decorator;
    }

    @Override
    public void saveToDatabase(CertificateDataRequest data) {
        saveCertificates(data);
    }

    private void saveCertificates(CertificateDataRequest data) {
        int index = 0;
        for (CertificateExcel certificateExcel : data.getExcelList()) {
            CertificateDates dates = CertificateDates.builder()
                    .date(data.getExcelList().get(index).getDateIssued().format(DateTimeFormatter.ofPattern(DATE_FORMAT)))
                    .hours(data.getHours())
                    .duration(decorator.fromDates(data.getStartDate(), data.getEndDate()))
                    .courseNumber(data.getCourseNumber())
                    .build();
            Certificate certificate = Certificate.builder()
                    .userName(certificateExcel.getName())
                    .userEmail(certificateExcel.getEmail())
                    .template(templateService.getTemplateById(data.getType()))
                    .dates(saveDates(dates))
                    .build();
            if (!certificateRepository.existsByUserName(certificate.getUserName())) {
                certificateService.addCertificate(certificate);
            }
            else {
                Certificate certificateFound = certificateService.getCertificateByUserName(certificate.getUserName());
                if (!certificate.getUserEmail().equals(certificateFound.getUserEmail())) {
                    certificate.setSendStatus(false);
                    certificateService.updateCertificateEmail(certificateFound.getId(), certificate);
                } else if (!certificate.getDates().equals(certificateFound.getDates())) {
                    certificateService.addCertificate(certificate);
                }
            }
            index++;
        }
    }

    private CertificateDates saveDates(CertificateDates dates) {
        if (!datesRepository.existsByDurationAndAndDate(dates.getDuration(), dates.getDate())) {
            return certificateDatesService.addCertificateDates(dates);
        }
        return certificateDatesService.getCertificateDatesByDurationAndDate(dates.getDuration(), dates.getDate());
    }

//    private void saveCertificates(CertificateDataRequest data, CertificateDates dates) {
//        for (CertificateExcel certificateExcel : data.getExcelList()) {
//            Certificate certificate = Certificate.builder()
//                    .userName(certificateExcel.getName())
//                    .userEmail(certificateExcel.getEmail())
//                    .template(templateService.getTemplateById(data.getType()))
//                    .dates(dates)
//                    .build();
//            System.out.println(certificate);
//            if (!certificateRepository.existsByUserName(certificate.getUserName())) {
//                certificateService.addCertificate(certificate);
//            }
//            else {
//                Certificate certificateFound = certificateService.getCertificateByUserName(certificate.getUserName());
//                if (!certificate.getUserEmail().equals(certificateFound.getUserEmail())) {
//                    certificate.setSendStatus(false);
//                    certificateService.updateCertificateEmail(certificateFound.getId(), certificate);
//                }
//            }
//        }
//    }
}
