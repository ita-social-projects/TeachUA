package com.softserve.teachua.service;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.certificate.CertificatePreview;
import com.softserve.teachua.dto.certificate.CertificateTransfer;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Certificate;
import com.softserve.teachua.model.CertificateDates;
import com.softserve.teachua.repository.CertificateRepository;
import com.softserve.teachua.service.impl.CertificateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CertificateServiceTest {

    @Mock
    private CertificateRepository certificateRepository;

    @Mock
    private DtoConverter dtoConverter;

    @InjectMocks
    private CertificateServiceImpl certificateService;

    private Certificate certificate;
    private CertificateTransfer certificateTransfer;
    private CertificatePreview certificatePreview;
    private CertificateDates certificateDates;

    private final Long EXISTING_CERTIFICATE_ID = 1L;
    private final Long NOT_EXISTING_CERTIFICATE_ID = 5L;
    private final String EXISTING_CERTIFICATE_USERNAME = "Existing Certificate";
    private final String EXISTING_CERTIFICATE_EMAIL = "existing@emailcom";
    private final String NOT_EXISTING_CERTIFICATE_USERNAME = "Not Existing Certificate";
    private final Integer EXISTING_CERTIFICATE_DATES_ID = 1;
    private final String EXISTING_CERTIFICATE_DATES_DATE = "2022-06-17";
    private final String UPDATE_CERTIFICATE_EMAIL = "updated@emailcom";

    @BeforeEach
    public void setUp() {
        certificateDates = CertificateDates.builder()
                .id(EXISTING_CERTIFICATE_DATES_ID)
                .date(EXISTING_CERTIFICATE_DATES_DATE)
                .build();
        certificate = Certificate.builder()
                .id(EXISTING_CERTIFICATE_ID)
                .userName(EXISTING_CERTIFICATE_USERNAME)
                .sendToEmail(EXISTING_CERTIFICATE_EMAIL)
                .sendStatus(true)
                .dates(certificateDates)
                .build();
        certificateTransfer = CertificateTransfer.builder()
                .id(EXISTING_CERTIFICATE_ID)
                .userName(EXISTING_CERTIFICATE_USERNAME)
                .sendToEmail(EXISTING_CERTIFICATE_EMAIL)
                .build();
        certificatePreview = CertificatePreview.builder()
                .id(EXISTING_CERTIFICATE_ID)
                .userName(EXISTING_CERTIFICATE_USERNAME)
                .sendToEmail(EXISTING_CERTIFICATE_EMAIL)
                .build();
    }

    @Test
    void getListOfCertificatesShouldReturnListOfCertificateTransfer() {
        when(certificateRepository.findAll())
                .thenReturn(Collections.singletonList(certificate));
        when(dtoConverter.convertToDto(certificate, CertificateTransfer.class))
                .thenReturn(certificateTransfer);
        assertThat(certificateService.getListOfCertificates())
                .isEqualTo(Collections.singletonList(certificateTransfer));
    }

    @Test
    void getListOfCertificatePreviewShouldReturnListOfCertificatePreview() {
        when(certificateRepository.findAllByOrderByIdAsc())
                .thenReturn(Collections.singletonList(certificate));
        when(dtoConverter.convertToDto(certificate, CertificatePreview.class))
                .thenReturn(certificatePreview);
        assertThat(certificateService.getListOfCertificatesPreview())
                .isEqualTo(Collections.singletonList(certificatePreview));
    }

    @Test
    void getByExistingUsernameShouldReturnListOfCertificates() {
        when(certificateRepository.findByUserName(EXISTING_CERTIFICATE_USERNAME))
                .thenReturn(Collections.singletonList(certificate));
        assertThat(certificateService.getCertificatesByUserName(EXISTING_CERTIFICATE_USERNAME))
                .isEqualTo(Collections.singletonList(certificate));
    }

    @Test
    void getByNotExistingUsernameShouldReturnEmptyList() {
        when(certificateRepository.findByUserName(NOT_EXISTING_CERTIFICATE_USERNAME))
                .thenReturn(Collections.emptyList());
        assertThat(certificateService.getCertificatesByUserName(NOT_EXISTING_CERTIFICATE_USERNAME))
                .isEqualTo(Collections.emptyList());
    }

    @Test
    void getByExistingUsernameAndDatesShouldReturnCertificate() {
        when(certificateRepository.findByUserNameAndDates(EXISTING_CERTIFICATE_USERNAME, certificateDates))
                .thenReturn(Optional.of(certificate));
        assertThat(certificateService.getByUserNameAndDates(EXISTING_CERTIFICATE_USERNAME, certificateDates))
                .isEqualTo(certificate);
    }

    @Test
    void addCertificateShouldReturnCertificate() {
        when(certificateRepository.save(certificate))
                .thenReturn(certificate);
        assertEquals(certificate, certificateService.addCertificate(certificate));
    }

    @Test
    void updateCertificateEmailWithExistingIdShouldReturnCertificate() {
        Certificate updatesCertificate = Certificate.builder()
                .sendStatus(false)
                .sendToEmail(UPDATE_CERTIFICATE_EMAIL)
                .build();
        when(certificateRepository.findById(EXISTING_CERTIFICATE_ID))
                .thenReturn(Optional.of(certificate));
        when(certificateRepository.save(certificate))
                .thenReturn(certificate);
        assertEquals(certificate, certificateService.updateCertificateEmail(EXISTING_CERTIFICATE_ID, updatesCertificate));
    }

    @Test
    void updateCertificateEmailWithNotExistingIdShouldThrowNotExistException() {
        when(certificateRepository.findById(NOT_EXISTING_CERTIFICATE_ID))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> certificateService.updateCertificateEmail(NOT_EXISTING_CERTIFICATE_ID, certificate))
                .isInstanceOf(NotExistException.class);
    }

    @Test
    void updateCertificatePreviewWithExistingIdShouldReturnCertificatePreview() {
        CertificatePreview updateCertificatePreview = CertificatePreview.builder()
                .sendStatus(false)
                .sendToEmail(UPDATE_CERTIFICATE_EMAIL)
                .build();
        when(certificateRepository.findById(EXISTING_CERTIFICATE_ID))
                .thenReturn(Optional.of(certificate));
        when(certificateRepository.save(certificate))
                .thenReturn(certificate);
        when(dtoConverter.convertToDto(certificate, CertificatePreview.class))
                .thenReturn(updateCertificatePreview);
        assertEquals(updateCertificatePreview, certificateService.updateCertificatePreview(EXISTING_CERTIFICATE_ID, updateCertificatePreview));
    }

    @Test
    void updateCertificatePreviewWithNewEmailShouldChangeSendStatus() {
        CertificatePreview certificatePreview = CertificatePreview.builder()
                .sendStatus(true)
                .sendToEmail(UPDATE_CERTIFICATE_EMAIL)
                .build();
        CertificatePreview expectedCertificatePreview = CertificatePreview.builder()
                .sendStatus(false)
                .sendToEmail(UPDATE_CERTIFICATE_EMAIL)
                .build();
        when(certificateRepository.findById(EXISTING_CERTIFICATE_ID))
                .thenReturn(Optional.of(certificate));
        when(certificateRepository.save(certificate))
                .thenReturn(certificate);
        when(dtoConverter.convertToDto(certificate, CertificatePreview.class))
                .thenReturn(expectedCertificatePreview);
        CertificatePreview updatedCertificatePreview = certificateService.updateCertificatePreview(EXISTING_CERTIFICATE_ID, certificatePreview);
        assertEquals(false, updatedCertificatePreview.getSendStatus());
    }

    @Test
    void updateCertificatePreviewWithSameEmailShouldChangeOnlySendStatus() {
        CertificatePreview certificatePreview = CertificatePreview.builder()
                .sendStatus(true)
                .sendToEmail(EXISTING_CERTIFICATE_EMAIL)
                .build();
        CertificatePreview expectedCertificatePreview = CertificatePreview.builder()
                .sendStatus(false)
                .sendToEmail(EXISTING_CERTIFICATE_EMAIL)
                .build();
        when(certificateRepository.findById(EXISTING_CERTIFICATE_ID))
                .thenReturn(Optional.of(certificate));
        when(certificateRepository.save(certificate))
                .thenReturn(certificate);
        when(dtoConverter.convertToDto(certificate, CertificatePreview.class))
                .thenReturn(expectedCertificatePreview);
        CertificatePreview updatedCertificatePreview = certificateService.updateCertificatePreview(EXISTING_CERTIFICATE_ID, certificatePreview);
        assertEquals(false, updatedCertificatePreview.getSendStatus());
    }

    @Test
    void updateCertificatePreviewWithNotExistingIdShouldThrowNotExistException() {
        when(certificateRepository.findById(NOT_EXISTING_CERTIFICATE_ID))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> certificateService.updateCertificatePreview(NOT_EXISTING_CERTIFICATE_ID, certificatePreview))
                .isInstanceOf(NotExistException.class);
    }
}
