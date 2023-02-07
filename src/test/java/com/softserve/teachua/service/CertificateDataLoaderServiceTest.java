package com.softserve.teachua.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.softserve.teachua.TestConstants.FILE_PATH_PDF;
import static com.softserve.teachua.TestUtils.getCertificateTemplate;
import com.softserve.teachua.dto.certificate_by_template.CertificateByTemplateTransfer;
import com.softserve.teachua.model.CertificateDates;
import com.softserve.teachua.model.CertificateTemplate;
import com.softserve.teachua.repository.CertificateDatesRepository;
import com.softserve.teachua.service.impl.CertificateDataLoaderServiceImpl;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
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
    private ObjectMapper objectMapper;

    private CertificateDates certificateDates;
    private CertificateByTemplateTransfer certificateTransfer;
    private CertificateTemplate certificateTemplate;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        certificateTransfer = getCertificateByTemplateTransfer();
        certificateTemplate = getCertificateTemplate();
        certificateTemplate.setFilePath(FILE_PATH_PDF);
    }

    @Test
    void saveCertificate() throws JsonProcessingException {
        ObjectMapper notMockObjectMapper = new ObjectMapper();
        when(templateService.getTemplateByFilePath(certificateTemplate.getFilePath())).thenReturn(certificateTemplate);
        when(objectMapper.readValue(certificateTemplate.getProperties(), HashMap.class)).thenReturn(
                notMockObjectMapper.readValue(certificateTemplate.getProperties(), HashMap.class));
        when(objectMapper.readValue(certificateTransfer.getValues(), HashMap.class)).thenReturn(
                notMockObjectMapper.readValue(certificateTransfer.getValues(), HashMap.class));
        when(certificateService.addCertificate(any())).thenReturn(null);

        certificateDataLoaderService.saveCertificate(certificateTransfer);
        verify(certificateService, times(1)).addCertificate(any());
    }

    private CertificateByTemplateTransfer getCertificateByTemplateTransfer() throws JsonProcessingException {
        String json = "{\"fieldsList\":[\"fullName\",\"learningForm\",\"Номер курсу\",\"countOfHours\",\"issueDate\","
                + "\"Електронна пошта\",\"duration\"],\"fieldPropertiesList\":[\"String\",\"String\",\"int\","
                + "\"int\",\"date\",\"String\",\"String\"],\"templateName\":\"1673724092154.pdf\","
                + "\"values\":\"{\\\"fullName\\\":\\\"\\\",\\\"learningForm\\\":\\\"дистанційна\\\",\\\"Номер"
                + " курсу\\\":\\\"1\\\",\\\"countOfHours\\\":\\\"99\\\",\\\"issueDate\\\":\\\"05.02.2023\\\","
                + "\\\"Електронна пошта\\\":\\\"\\\", \\\"duration\\\":\\\"duration\\\"}\","
                + "\"columnHeadersList\":[\"№ п/п\",\"Прізвище, ім'я, по батькові отримувача\",\"Номер "
                + "сертифіката\",\"Дата видачі\",\"Електронна адреса\",\"Примітки\"],"
                + "\"excelContent\":[[\"1\",\"Денисюк-Стасюк Олександр-Іван\",\"1010000001\",\"30.10.2023\","
                + "\"email@gmail.com\",\"Виданий без нанесення номера\"]],\"excelColumnsOrder\":[\"Прізвище, "
                + "ім'я, по батькові отримувача\",\"№ п/п\",\"Номер сертифіката\",\"Дата видачі\","
                + "\"Примітки\",\"Електронна адреса\", \"\"]}";
        return new ObjectMapper().readValue(json, CertificateByTemplateTransfer.class);
    }
}
