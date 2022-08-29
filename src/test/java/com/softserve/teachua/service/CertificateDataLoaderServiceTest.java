package com.softserve.teachua.service;

import com.softserve.teachua.dto.certificate.CertificateDataRequest;
import com.softserve.teachua.dto.certificate.CertificateDatabaseResponse;
import com.softserve.teachua.dto.certificateExcel.CertificateExcel;
import com.softserve.teachua.model.Certificate;
import com.softserve.teachua.model.CertificateDates;
import com.softserve.teachua.model.CertificateTemplate;
import com.softserve.teachua.repository.CertificateDatesRepository;
import com.softserve.teachua.repository.CertificateRepository;
import com.softserve.teachua.repository.CertificateTemplateRepository;
import com.softserve.teachua.service.impl.CertificateDataLoaderServiceImpl;
import com.softserve.teachua.service.impl.CertificateDatesServiceImpl;
import com.softserve.teachua.service.impl.CertificateServiceImpl;
import com.softserve.teachua.service.impl.CertificateTemplateServiceImpl;
import com.softserve.teachua.utils.CertificateContentDecorator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CertificateDataLoaderServiceTest {

    @Mock
    private CertificateTemplateRepository templateRepository;

    @Mock
    private CertificateDatesRepository datesRepository;

    @Mock
    private CertificateRepository certificateRepository;

    @Mock
    private CertificateTemplateServiceImpl templateService;

    @Mock
    private CertificateDatesServiceImpl datesService;

    @Mock
    private CertificateServiceImpl certificateService;

    @Mock
    private CertificateContentDecorator decorator;

    @InjectMocks
    private CertificateDataLoaderServiceImpl certificateDataLoaderService;

    private CertificateTemplate certificateTemplate;
    private CertificateDates certificateDates;
    private CertificateExcel certificateExcel;
    private CertificateDataRequest dataRequest;
    private CertificateDatabaseResponse databaseResponse;
    private Certificate certificate;

    private final Integer EXISTING_CERTIFICATE_TEMPLATE_TYPE = 1;
    private final String EXISTING_CERTIFICATE_DATES_DATE = "16.07.2022";
    private final Integer EXISTING_CERTIFICATE_DATES_HOURS = 40;
    private final String EXISTING_CERTIFICATE_DATES_DURATION = "Existing Duration";
    private final String EXISTING_CERTIFICATE_DATES_COURSE_NUMBER = "4";
    private final LocalDate EXISTING_CERTIFICATE_START_DATE = LocalDate.of(2022, 01, 13);
    private final LocalDate EXISTING_CERTIFICATE_END_DATE = LocalDate.of(2022, 01, 29);
    private final LocalDate EXISTING_CERTIFICATE_EXCEL_DATE = LocalDate.of(2022, 07, 16);
    private final String EXISTING_CERTIFICATE_NAME = "Existing Name";
    private final String EXISTING_CERTIFICATE_EMAIL = "existing@email.com";

    @BeforeEach
    public void setUp() {
        certificateTemplate = CertificateTemplate.builder()
                .certificateType(EXISTING_CERTIFICATE_TEMPLATE_TYPE)
                .build();
        certificateDates = CertificateDates.builder()
                .date(EXISTING_CERTIFICATE_DATES_DATE)
                .hours(EXISTING_CERTIFICATE_DATES_HOURS)
                .duration(EXISTING_CERTIFICATE_DATES_DURATION)
                .courseNumber(EXISTING_CERTIFICATE_DATES_COURSE_NUMBER)
                .build();
        certificateExcel = CertificateExcel.builder()
                .name(EXISTING_CERTIFICATE_NAME)
                .email(EXISTING_CERTIFICATE_EMAIL)
                .dateIssued(EXISTING_CERTIFICATE_EXCEL_DATE)
                .build();
        certificate = Certificate.builder()
                .userName(EXISTING_CERTIFICATE_NAME)
                .sendToEmail(EXISTING_CERTIFICATE_EMAIL)
                .template(certificateTemplate)
                .dates(certificateDates)
                .build();
        dataRequest = CertificateDataRequest.builder()
                .type(EXISTING_CERTIFICATE_TEMPLATE_TYPE)
                .hours(EXISTING_CERTIFICATE_DATES_HOURS)
                .startDate(EXISTING_CERTIFICATE_START_DATE)
                .endDate(EXISTING_CERTIFICATE_END_DATE)
                .courseNumber(EXISTING_CERTIFICATE_DATES_COURSE_NUMBER)
                .excelList(Collections.singletonList(certificateExcel))
                .build();
    }

    @Test
    public void saveToDatabaseWithNewCorrectDataShouldReturnEmptyList() {
        when(decorator.formDates(EXISTING_CERTIFICATE_START_DATE, EXISTING_CERTIFICATE_END_DATE))
                .thenReturn(EXISTING_CERTIFICATE_DATES_DURATION);
        when(datesRepository.existsByDurationAndAndDate(EXISTING_CERTIFICATE_DATES_DURATION, EXISTING_CERTIFICATE_DATES_DATE))
                .thenReturn(true);
        when(datesService.getCertificateDatesByDurationAndDate(EXISTING_CERTIFICATE_DATES_DURATION, EXISTING_CERTIFICATE_DATES_DATE))
                .thenReturn(certificateDates);
        when(templateRepository.existsBy())
                .thenReturn(true);
        when(templateService.getTemplateByType(EXISTING_CERTIFICATE_TEMPLATE_TYPE))
                .thenReturn(certificateTemplate);
        when(certificateRepository.existsByUserNameAndDates(EXISTING_CERTIFICATE_NAME, certificateDates))
                .thenReturn(false);
        when(certificateService.addCertificate(certificate))
                .thenReturn(certificate);
        assertThat(certificateDataLoaderService.saveToDatabase(dataRequest))
                .isEqualTo(Collections.emptyList());
    }

    @Test
    public void saveToDatabaseWhenTemplateAndDatesNotExistReturnEmptyList() {
        when(decorator.formDates(EXISTING_CERTIFICATE_START_DATE, EXISTING_CERTIFICATE_END_DATE))
                .thenReturn(EXISTING_CERTIFICATE_DATES_DURATION);
        when(datesRepository.existsByDurationAndAndDate(EXISTING_CERTIFICATE_DATES_DURATION, EXISTING_CERTIFICATE_DATES_DATE))
                .thenReturn(false);
        when(datesService.addCertificateDates(certificateDates))
                .thenReturn(certificateDates);
        when(templateRepository.existsBy())
                .thenReturn(false);
        when(templateService.addTemplate(certificateTemplate))
                .thenReturn(certificateTemplate);
        when(certificateRepository.existsByUserNameAndDates(EXISTING_CERTIFICATE_NAME, certificateDates))
                .thenReturn(false);
        when(certificateService.addCertificate(certificate))
                .thenReturn(certificate);
        assertThat(certificateDataLoaderService.saveToDatabase(dataRequest))
                .isEqualTo(Collections.emptyList());
    }

//    @Test
//    public void saveTemplateWhenTableIsEmptyShouldAddNewTemplate() {
//        when(templateRepository.existsBy())
//                .thenReturn(false);
//        when(templateService.addTemplate(certificateTemplate))
//                .thenReturn(certificateTemplate);
//        assertThat(certificateDataLoaderService.save(CERTIFICATE_TEMPLATE_TYPE))
//                .isEqualTo(certificate);
//    }

}
