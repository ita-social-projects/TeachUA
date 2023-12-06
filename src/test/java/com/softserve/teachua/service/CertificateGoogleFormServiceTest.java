package com.softserve.teachua.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.softserve.teachua.TestConstants.FILE_PATH_PDF;
import static com.softserve.teachua.TestConstants.INVALID_CERTIFICATE_TEMPLATE_PROPERTIES;
import static com.softserve.teachua.TestUtils.getCertificateByTemplateTransfer;
import static com.softserve.teachua.TestUtils.getCertificateTemplate;
import static com.softserve.teachua.TestUtils.getInvalidCertificateByTemplateTransfer;
import static com.softserve.teachua.TestUtils.getInvalidQuizResults;
import static com.softserve.teachua.TestUtils.getQuizResults;
import com.softserve.teachua.dto.certificate_by_template.CertificateByTemplateSavingResponse;
import com.softserve.teachua.dto.certificate_by_template.CertificateByTemplateTransfer;
import com.softserve.teachua.model.CertificateTemplate;
import com.softserve.teachua.service.impl.CertificateGoogleFormServiceImpl;
import com.softserve.teachua.service.impl.CertificateValidatorImpl;
import java.util.ArrayList;
import java.util.HashMap;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CertificateGoogleFormServiceTest {
    private final ObjectMapper notMockObjectMapper = new ObjectMapper();
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private CertificateTemplateService templateService;
    @Mock
    private CertificateDatesService certificateDatesService;
    @Mock
    private CertificateService certificateService;
    @Spy
    @InjectMocks
    private CertificateGoogleFormServiceImpl certificateGoogleFormService;
    @Spy
    @InjectMocks
    private CertificateValidator certificateValidator = new CertificateValidatorImpl();
    private CertificateByTemplateTransfer certificateTransfer;
    private CertificateTemplate certificateTemplate;

    @Test
    void saveGoogleFormCertificateData_IfValid() throws JsonProcessingException {
        certificateTransfer = getCertificateByTemplateTransfer();
        certificateTransfer.setExcelContent(new ArrayList<>());
        certificateTransfer.setGoogleFormResults(getQuizResults());
        certificateTemplate = getCertificateTemplate();
        configurate();

        assertThat(certificateGoogleFormService.saveGoogleFormCertificateData(certificateTransfer).getMessages())
                .hasSize(1);
    }

    @Test
    void saveGoogleFormCertificateData_IfInvalid() throws JsonProcessingException {
        certificateTransfer = getInvalidCertificateByTemplateTransfer();
        certificateTransfer.setExcelContent(new ArrayList<>());
        certificateTransfer.setGoogleFormResults(getInvalidQuizResults());
        certificateTemplate = getCertificateTemplate();
        certificateTemplate.setProperties(INVALID_CERTIFICATE_TEMPLATE_PROPERTIES);
        configurate();

        CertificateByTemplateSavingResponse response =
                certificateGoogleFormService.saveGoogleFormCertificateData(certificateTransfer);

        assertThat(response.getMessages()).hasSizeGreaterThan(1);
        assertThat(response.getInvalidValues()).isNotEmpty();
    }

    private void configurate() throws JsonProcessingException {
        certificateTemplate.setFilePath(FILE_PATH_PDF);
        when(templateService.
                getTemplateByFilePath(certificateTemplate.getFilePath()))
                .thenReturn(certificateTemplate);
        when(objectMapper.readValue(certificateTemplate.getProperties(), HashMap.class)).thenReturn(
                notMockObjectMapper.readValue(certificateTemplate.getProperties(), HashMap.class));
        when(objectMapper.readValue(certificateTransfer.getValues(), HashMap.class)).thenReturn(
                notMockObjectMapper.readValue(certificateTransfer.getValues(), HashMap.class));
    }
}
