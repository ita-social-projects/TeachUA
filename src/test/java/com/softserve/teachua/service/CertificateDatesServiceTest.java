package com.softserve.teachua.service;

import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.CertificateDates;
import com.softserve.teachua.repository.CertificateDatesRepository;
import com.softserve.teachua.service.impl.CertificateDatesServiceImpl;
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
public class CertificateDatesServiceTest {

    @Mock
    private CertificateDatesRepository certificateDatesRepository;

    @InjectMocks
    private CertificateDatesServiceImpl certificateDatesService;

    private CertificateDates certificateDates;

    private final Integer EXISTING_CERTIFICATE_DATES_ID = 1;
    private final String EXISTING_CERTIFICATE_DATES_DURATION = "Existing Duration";
    private final String EXISTING_CERTIFICATE_DATES_DATE = "19.01.2022";
    private final Integer NOT_EXISTING_CERTIFICATE_DATES_ID = 10;
    private final String NOT_EXISTING_CERTIFICATE_DATES_DURATION = "Not Existing Duration";
    private final String NOT_EXISTING_CERTIFICATE_DATES_DATE = "25.10.2022";

    @BeforeEach
    public void setUp() {
        certificateDates = CertificateDates.builder()
                .id(EXISTING_CERTIFICATE_DATES_ID)
                .duration(EXISTING_CERTIFICATE_DATES_DURATION)
                .date(EXISTING_CERTIFICATE_DATES_DATE)
                .build();
    }

    @Test
    void getByExistingIdShouldReturnCertificateDates() {
        when(certificateDatesRepository.findById(EXISTING_CERTIFICATE_DATES_ID))
                .thenReturn(Optional.ofNullable(certificateDates));
        assertThat(certificateDatesService.getCertificateDatesById(EXISTING_CERTIFICATE_DATES_ID))
                .isEqualTo(certificateDates);
    }

    @Test
    void getByNotExistingIdShouldThrowNotExistException() {
        when(certificateDatesRepository.findById(NOT_EXISTING_CERTIFICATE_DATES_ID))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> certificateDatesService.getCertificateDatesById(NOT_EXISTING_CERTIFICATE_DATES_ID))
                .isInstanceOf(NotExistException.class);
    }

    @Test
    void getByExistingDurationShouldReturnCertificateDates() {
        when(certificateDatesRepository.findByDuration(EXISTING_CERTIFICATE_DATES_DURATION))
                .thenReturn(Optional.ofNullable(certificateDates));
        assertThat(certificateDatesService.getCertificateDatesByDuration(EXISTING_CERTIFICATE_DATES_DURATION))
                .isEqualTo(certificateDates);
    }

    @Test
    void getByNotExistingDurationShouldThrowNotExistException() {
        when(certificateDatesRepository.findByDuration(NOT_EXISTING_CERTIFICATE_DATES_DURATION))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> certificateDatesService.getCertificateDatesByDuration(NOT_EXISTING_CERTIFICATE_DATES_DURATION))
                .isInstanceOf(NotExistException.class);
    }

    @Test
    void getByExistingDurationAndDateShouldReturnCertificateDates() {
        when(certificateDatesRepository.findByDurationAndDate(EXISTING_CERTIFICATE_DATES_DURATION, EXISTING_CERTIFICATE_DATES_DATE))
                .thenReturn(Optional.ofNullable(certificateDates));
        assertThat(certificateDatesService.getCertificateDatesByDurationAndDate(EXISTING_CERTIFICATE_DATES_DURATION, EXISTING_CERTIFICATE_DATES_DATE))
                .isEqualTo(certificateDates);
    }

    @Test
    void getByNotExistingDurationAndDateShouldThrowNotExistException() {
        when(certificateDatesRepository.findByDurationAndDate(NOT_EXISTING_CERTIFICATE_DATES_DURATION, NOT_EXISTING_CERTIFICATE_DATES_DATE))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> certificateDatesService.getCertificateDatesByDurationAndDate(NOT_EXISTING_CERTIFICATE_DATES_DURATION, NOT_EXISTING_CERTIFICATE_DATES_DATE))
                .isInstanceOf(NotExistException.class);
    }

    @Test
    void addCertificateDatesShouldReturnCertificateDates() {
        when(certificateDatesRepository.save(certificateDates))
                .thenReturn(certificateDates);
        assertEquals(certificateDates, certificateDatesService.addCertificateDates(certificateDates));
    }
}
