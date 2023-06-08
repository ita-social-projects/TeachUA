package com.softserve.teachua.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.softserve.teachua.TestConstants.CERTIFICATE_DATES_DURATION;
import static com.softserve.teachua.TestConstants.CERTIFICATE_DATES_END_DATE;
import static com.softserve.teachua.TestConstants.CERTIFICATE_DATES_START_DATE;
import static com.softserve.teachua.TestConstants.FILE_PATH_PDF;
import static com.softserve.teachua.TestUtils.getCertificate;
import static com.softserve.teachua.TestUtils.getCertificateByTemplateTransfer;
import static com.softserve.teachua.TestUtils.getCertificateDataRequest;
import static com.softserve.teachua.TestUtils.getCertificateDates;
import static com.softserve.teachua.TestUtils.getCertificateTemplate;
import com.softserve.teachua.dto.certificate.CertificateDataRequest;
import com.softserve.teachua.dto.certificate_by_template.CertificateByTemplateTransfer;
import com.softserve.teachua.model.Certificate;
import com.softserve.teachua.model.CertificateDates;
import com.softserve.teachua.model.CertificateTemplate;
import com.softserve.teachua.model.CertificateType;
import com.softserve.teachua.repository.CertificateRepository;
import com.softserve.teachua.repository.CertificateTemplateRepository;
import com.softserve.teachua.repository.CertificateTypeRepository;
import com.softserve.teachua.service.impl.CertificateDataLoaderServiceImpl;
import com.softserve.teachua.utils.CertificateContentDecorator;
import java.util.HashMap;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CertificateDataLoaderServiceTest {
    @Spy
    @InjectMocks
    private CertificateDataLoaderServiceImpl certificateDataLoaderService;
    @Mock
    private CertificateDatesService certificateDatesService;
    @Mock
    private CertificateTemplateService templateService;
    @Mock
    private CertificateService certificateService;
    @Mock
    private CertificateRepository certificateRepository;
    @Mock
    private CertificateTypeRepository certificateTypeRepository;
    @Mock
    private CertificateTemplateRepository templateRepository;
    @Mock
    private CertificateTypeService certificateTypeService;
    @Mock
    private CertificateContentDecorator decorator;
    @Mock
    private ObjectMapper objectMapper;
    private Certificate certificate;
    private CertificateDates certificateDates;
    private CertificateByTemplateTransfer certificateByTemplateTransfer;
    private CertificateTemplate certificateTemplate;
    private CertificateDataRequest certificateDataRequest;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        certificate = getCertificate();
        certificateDates = getCertificateDates();
        certificateDates.setId(null);
        certificateDates.setStudyForm(null);
        certificateDataRequest = getCertificateDataRequest();
        certificateByTemplateTransfer = getCertificateByTemplateTransfer();
        certificateTemplate = getCertificateTemplate();
        certificateTemplate.setFilePath(FILE_PATH_PDF);
    }

    @Test
    void saveToDatabase_saveToDatabase_IfCertificateDoesNotExists() {
        mockSaveDates(false);
        mockSaveTemplate();
        when(certificateRepository.existsByUserNameAndDates(certificate.getUserName(), certificateDates)).thenReturn(
                false);

        assertThat(certificateDataLoaderService.saveToDatabase(certificateDataRequest)).isEmpty();
    }

    @Test
    void saveToDatabase_IfCertificateExists() {
        mockSaveDates(true);
        mockSaveTemplate();
        when(certificateRepository.existsByUserNameAndDates(certificate.getUserName(),
                certificate.getDates())).thenReturn(
                true);
        when(certificateService.getByUserNameAndDates(certificate.getUserName(), certificate.getDates())).thenReturn(
                certificate);

        assertThat(certificateDataLoaderService.saveToDatabase(certificateDataRequest)).hasSize(1);
    }

    private void mockSaveDates(boolean isCertificateDateExists) {
        when(decorator.formDates(CERTIFICATE_DATES_START_DATE, CERTIFICATE_DATES_END_DATE)).thenReturn(
                CERTIFICATE_DATES_DURATION);
        if (isCertificateDateExists) {
            when(certificateDatesService.getOrCreateCertificateDates(certificateDates)).thenReturn(
                    certificate.getDates());
        } else {
            when(certificateDatesService.getOrCreateCertificateDates(certificateDates)).thenReturn(certificateDates);
        }
    }

    private void mockSaveTemplate() {
        when(certificateTypeRepository.existsById(any(Integer.class))).thenReturn(false);
        when(certificateTypeService.getCertificateTypeById(any(Integer.class))).thenReturn(
                new CertificateType());
        when(templateRepository.existsCertificateTemplateByCertificateType(any(CertificateType.class))).thenReturn(
                false);
    }

    @Test
    void saveCertificate() throws JsonProcessingException {
        ObjectMapper notMockObjectMapper = new ObjectMapper();
        when(templateService.getTemplateByFilePath(certificateTemplate.getFilePath())).thenReturn(certificateTemplate);
        when(objectMapper.readValue(certificateTemplate.getProperties(), HashMap.class)).thenReturn(
                notMockObjectMapper.readValue(certificateTemplate.getProperties(), HashMap.class));
        when(objectMapper.readValue(certificateByTemplateTransfer.getValues(), HashMap.class)).thenReturn(
                notMockObjectMapper.readValue(certificateByTemplateTransfer.getValues(), HashMap.class));
        when(certificateService.addCertificate(any())).thenReturn(null);

        Certificate expectedCertificate = notMockObjectMapper.readValue("{\"id"
                + "\":null,\"serialNumber\":null,\"user\":null,\"userName\":\"Денисюк-Стасюк Олександр-Іван\","
                + "\"sendToEmail\":\"email@gmail.com\",\"messengerUserName\":null,\"messenger\":null,"
                + "\"sendStatus\":null,\"updateStatus\":null,\"dates\":{\"id\":43,\"date\":\"05.02.2023\","
                + "\"hours\":99,\"duration\":null,\"courseNumber\":\"1\",\"studyForm\":\"дистанційна\"},"
                + "\"values\":null}", Certificate.class);

        certificateDataLoaderService.saveCertificate(certificateByTemplateTransfer);
        verify(certificateService, times(1)).addCertificate(expectedCertificate);
    }
}
