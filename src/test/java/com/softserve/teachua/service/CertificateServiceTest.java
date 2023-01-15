package com.softserve.teachua.service;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.certificate.CertificateTransfer;
import com.softserve.teachua.dto.certificate.CertificateUserResponse;
import com.softserve.teachua.model.*;
import com.softserve.teachua.repository.CertificateRepository;
import com.softserve.teachua.service.impl.CertificateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CertificateServiceTest {

    @Mock
    private CertificateRepository certificateRepository;

    @Mock
    private DtoConverter dtoConverter;

    @Spy
    @InjectMocks
    private CertificateServiceImpl certificateService;

    private Certificate certificate;

    private CertificateTemplate certificateTemplate;

    private CertificateDates certificateDates;

    private CertificateUserResponse certificateUserResponse;

    private static final Long ID = 1L;

    private static final String USER_EMAIL = "admin@gmail.com";

    private static final String USER_NAME = "Власник Сертифікату";

    private static final String COURSE_DESCRIPTION = "Опис курсу";

    private static final String STRING_DATE = "01.11.2022";

    private static final LocalDate UPDATE_DATE = LocalDate.now();

    private static final Long SERIAL_NUMBER = 3010000001L;

    private static final Integer CERTIFICATE_TYPE = 3;

    @BeforeEach
    void setUp() {
        certificateTemplate = CertificateTemplate.builder()
            .certificateType(CertificateType.builder().codeNumber(CERTIFICATE_TYPE).build())
            .courseDescription(COURSE_DESCRIPTION)
            .build();

        certificateDates = CertificateDates.builder().date(STRING_DATE).build();

        certificate = Certificate.builder()
            .id(ID)
            .sendToEmail(USER_EMAIL)
            .serialNumber(SERIAL_NUMBER)
            .userName(USER_NAME)
            .updateStatus(UPDATE_DATE)
            .sendStatus(true)
            .template(certificateTemplate)
            .dates(certificateDates)
            .build();

        certificateUserResponse = CertificateUserResponse.builder()
            .id(certificate.getId())
            .serialNumber(certificate.getSerialNumber())
            .certificateTypeName(certificate.getTemplate().getCertificateType().getName())
            .courseDescription(certificate.getTemplate().getCourseDescription())
            .date(certificate.getDates().getDate())
            .build();
    }

    @Test
    void getListOfCertificatesByEmailShouldReturnListOfCertificateResponse() {
        when(certificateRepository.findAllForDownload(USER_EMAIL)).thenReturn(Collections.singletonList(certificate));

        assertThat(certificateService.getListOfCertificatesByEmail(USER_EMAIL))
            .isNotEmpty().isEqualTo(Collections.singletonList(certificateUserResponse));
    }

    @Test
    void getPdfOutputForDownloadShouldThrowOnTryingGetNotOwnCertificate() {
        when(certificateRepository.findById(certificate.getId())).thenReturn(Optional.of(certificate));

        assertThrows(AccessDeniedException.class, () -> {
            certificateService.getPdfOutputForDownload("foreign@gmail.com", certificate.getId());
        });
    }

    @Test
    void getPdfOutputForDownloadShouldThrowOnSentCertificateWithSerialNumberNull() {
        certificate.setSerialNumber(null);

        when(certificateRepository.findById(certificate.getId())).thenReturn(Optional.of(certificate));

        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            certificateService.getPdfOutputForDownload(certificate.getSendToEmail(), certificate.getId());
        });
        assertThat(thrown.getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
    }

    @ParameterizedTest
    @MethodSource("provideCertificateForDownload")
    void getPdfOutputForDownloadShouldUpdateSendStatusAndUpdateStatus(Certificate certificate) {
        CertificateTransfer certificateTransfer = CertificateTransfer.builder().build();

        when(certificateRepository.findById(certificate.getId())).thenReturn(Optional.of(certificate));
        when(dtoConverter.convertToDto(certificate, CertificateTransfer.class))
            .thenReturn(certificateTransfer);
        doReturn(new byte[]{}).when(certificateService).getPdfOutput(certificateTransfer);

        certificateService.getPdfOutputForDownload(certificate.getSendToEmail(), certificate.getId());

        assertThat(certificate.getSendStatus()).isTrue();
        assertThat(certificate.getUpdateStatus()).isNotNull();
    }

    private static Stream<Arguments> provideCertificateForDownload() {
        Certificate certificate = Certificate.builder().id(ID).sendToEmail(USER_EMAIL).serialNumber(SERIAL_NUMBER)
            .updateStatus(UPDATE_DATE).sendStatus(true).build();
        return Stream.of(
            Arguments.of(certificate),
            Arguments.of(certificate.withSendStatus(null).withUpdateStatus(null)),
            Arguments.of(certificate.withSendStatus(null).withUpdateStatus(LocalDate.now())),
            Arguments.of(certificate.withSendStatus(false).withUpdateStatus(null)),
            Arguments.of(certificate.withSendStatus(false).withUpdateStatus(LocalDate.now()))
        );
    }
}
