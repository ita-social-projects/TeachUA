package com.softserve.teachua.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.softserve.teachua.TestConstants.FILE_PATH_PDF;
import static com.softserve.teachua.TestUtils.getCertificateByTemplateTransfer;
import static com.softserve.teachua.TestUtils.getCertificateTemplate;
import com.softserve.teachua.dto.certificate_by_template.CertificateByTemplateTransfer;
import com.softserve.teachua.model.CertificateDates;
import com.softserve.teachua.model.CertificateTemplate;
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
}
