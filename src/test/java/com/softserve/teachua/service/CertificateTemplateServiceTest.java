package com.softserve.teachua.service;

import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.CertificateTemplate;
import com.softserve.teachua.repository.CertificateTemplateRepository;
import com.softserve.teachua.service.impl.CertificateTemplateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CertificateTemplateServiceTest {

    @Mock
    private CertificateTemplateRepository certificateTemplateRepository;

    @InjectMocks
    private CertificateTemplateServiceImpl certificateTemplateService;

    private CertificateTemplate certificateTemplate;

    private final Integer EXISTING_CERTIFICATE_TEMPLATE_ID = 1;
    private final Integer EXISTING_CERTIFICATE_TEMPLATE_TYPE = 1;
    private final Integer NOT_EXISTING_CERTIFICATE_TEMPLATE_ID = 10;
    private final Integer NOT_EXISTING_CERTIFICATE_TEMPLATE_TYPE = 10;

    @BeforeEach
    public void setUp() {
        certificateTemplate = CertificateTemplate.builder()
                .id(EXISTING_CERTIFICATE_TEMPLATE_ID)
                .certificateType(EXISTING_CERTIFICATE_TEMPLATE_TYPE)
                .build();
    }

    @Test
    void getByExistingIdShouldReturnCertificateTemplate() {
        when(certificateTemplateRepository.findById(EXISTING_CERTIFICATE_TEMPLATE_ID))
                .thenReturn(Optional.of(certificateTemplate));
        assertThat(certificateTemplateService.getTemplateById(EXISTING_CERTIFICATE_TEMPLATE_ID))
                .isEqualTo(certificateTemplate);
    }

    @Test
    void getByNotExistingIdShouldThrowNotExistException() {
        when(certificateTemplateRepository.findById(NOT_EXISTING_CERTIFICATE_TEMPLATE_ID))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> certificateTemplateService.getTemplateById(NOT_EXISTING_CERTIFICATE_TEMPLATE_ID))
                .isInstanceOf(NotExistException.class);
    }

    @Test
    void getByExistingTypeShouldReturnCertificateTemplate() {
        when(certificateTemplateRepository.findByCertificateType(EXISTING_CERTIFICATE_TEMPLATE_TYPE))
                .thenReturn(Optional.of(certificateTemplate));
        assertThat(certificateTemplateService.getTemplateByType(EXISTING_CERTIFICATE_TEMPLATE_TYPE))
                .isEqualTo(certificateTemplate);
    }

    @Test
    void getByNotExistingTypeShouldThrowNotExistException() {
        when(certificateTemplateRepository.findByCertificateType(NOT_EXISTING_CERTIFICATE_TEMPLATE_TYPE))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> certificateTemplateService.getTemplateByType(NOT_EXISTING_CERTIFICATE_TEMPLATE_TYPE))
                .isInstanceOf(NotExistException.class);
    }

    @Test
    void addCertificateTemplateShouldReturnCertificateTemplate() {
        when(certificateTemplateRepository.save(certificateTemplate))
                .thenReturn(certificateTemplate);
        assertEquals(certificateTemplate, certificateTemplateService.addTemplate(certificateTemplate));
    }
}
