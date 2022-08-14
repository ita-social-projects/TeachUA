package com.softserve.teachua.service.impl;

import com.softserve.teachua.dto.certificate.CertificateDataRequest;
import com.softserve.teachua.dto.certificate.CertificateDatabaseResponse;
import com.softserve.teachua.dto.certificateExcel.CertificateExcel;
import com.softserve.teachua.model.Certificate;
import com.softserve.teachua.model.CertificateDates;
import com.softserve.teachua.model.CertificateTemplate;
import com.softserve.teachua.repository.CertificateDatesRepository;
import com.softserve.teachua.repository.CertificateRepository;
import com.softserve.teachua.repository.CertificateTemplateRepository;
import com.softserve.teachua.service.CertificateDataLoaderService;
import com.softserve.teachua.service.CertificateDatesService;
import com.softserve.teachua.service.CertificateService;
import com.softserve.teachua.service.CertificateTemplateService;
import com.softserve.teachua.utils.CertificateContentDecorator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CertificateDataLoaderServiceImpl implements CertificateDataLoaderService {
    private final static String UPDATED_EMAIL = "Оновлено електронну адресу учасника %s";
    private final static String ALREADY_EXISTS = "Сертифікат для учасника %s вже згенеровано %s";
    private final static String DATE_FORMAT = "dd.MM.YYYY";
    private final CertificateService certificateService;
    private final CertificateDatesService certificateDatesService;
    private final CertificateTemplateService templateService;
    private final CertificateRepository certificateRepository;
    private final CertificateDatesRepository datesRepository;
    private final CertificateTemplateRepository templateRepository;
    private final CertificateContentDecorator decorator;

    @Autowired
    public CertificateDataLoaderServiceImpl(CertificateService certificateService, CertificateDatesService certificateDatesService, CertificateTemplateService templateService, CertificateRepository certificateRepository, CertificateDatesRepository datesRepository, CertificateTemplateRepository templateRepository, CertificateContentDecorator decorator) {
        this.certificateService = certificateService;
        this.certificateDatesService = certificateDatesService;
        this.templateService = templateService;
        this.certificateRepository = certificateRepository;
        this.datesRepository = datesRepository;
        this.templateRepository = templateRepository;
        this.decorator = decorator;
    }

    @Override
    public List<CertificateDatabaseResponse> saveToDatabase(CertificateDataRequest data) {
        List<CertificateDatabaseResponse> response = new ArrayList<>();
        int index = 0;
        for (CertificateExcel certificateExcel : data.getExcelList()) {
            CertificateDates dates = saveDates(data, index);
            CertificateTemplate template = saveTemplate(data.getType());
            Certificate certificate = Certificate.builder()
                    .userName(certificateExcel.getName())
                    .sendToEmail(certificateExcel.getEmail())
                    .template(template)
                    .dates(dates)
                    .build();
            if (!certificateRepository.existsByUserNameAndDates(certificate.getUserName(), certificate.getDates())) {
                certificateService.addCertificate(certificate);
            } else {
                Certificate certificateFound = certificateService.getByUserNameAndDates(certificate.getUserName(), certificate.getDates());
                if (!certificate.getSendToEmail().equals(certificateFound.getSendToEmail())) {
                    certificate.setSendStatus(false);
                    certificateService.updateCertificateEmail(certificateFound.getId(), certificate);
                    response.add(new CertificateDatabaseResponse(String.format(UPDATED_EMAIL, certificateFound.getUserName())));
                } else if (!certificate.getDates().equals(certificateFound.getDates())) {
                    certificateService.addCertificate(certificate);
                } else {
                    response.add(new CertificateDatabaseResponse(String.format(ALREADY_EXISTS, certificateFound.getUserName(), certificateFound.getDates().getDate())));
                }
            }
            index++;
        }
        return response;
    }

    private CertificateDates saveDates(CertificateDataRequest data, Integer index) {
        CertificateDates dates = CertificateDates.builder()
                .date(data.getExcelList().get(index).getDateIssued().format(DateTimeFormatter.ofPattern(DATE_FORMAT)))
                .hours(data.getHours())
                .duration(decorator.formDates(data.getStartDate(), data.getEndDate()))
                .courseNumber(data.getCourseNumber())
                .build();
        if (!datesRepository.existsByDurationAndAndDate(dates.getDuration(), dates.getDate())) {
            return certificateDatesService.addCertificateDates(dates);
        }
        return certificateDatesService.getCertificateDatesByDurationAndDate(dates.getDuration(), dates.getDate());
    }

    private CertificateTemplate saveTemplate(Integer type) {
        if (!templateRepository.existsBy()) {
            return templateService.addTemplate(CertificateTemplate
                    .builder()
                    .name("Єдині учасник")
                    .certificateType(3)
                    .filePath("/certificates/templates/jedyni_participant_template.jrxml")
                    .build());
        }
        return templateService.getTemplateByType(type);
    }
}
