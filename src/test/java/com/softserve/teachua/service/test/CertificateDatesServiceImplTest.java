package com.softserve.teachua.service.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.CertificateDates;
import com.softserve.teachua.model.archivable.CertificateDatesArch;
import com.softserve.teachua.repository.CertificateDatesRepository;
import com.softserve.teachua.service.ArchiveService;
import com.softserve.teachua.service.impl.CertificateDatesServiceImpl;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CertificateDatesServiceImplTest {
    private static final int CORRECT_ID = 1;
    private static final int WRONG_ID = 15;

    private static final int CERTIFICATE_DATES_ID = 1;

    private static final String CERTIFICATE_DATES_DATE = "date sample";
    private static final String CERTIFICATE_DATES_WRONG_DATE = "date sample";

    private static final int CERTIFICATE_DATES_HOURS = 40;
    private static final int CERTIFICATE_DATES_WRONG_HOURS = 50;

    private static final String CERTIFICATE_DATES_DURATION = "duration sample";
    private static final String CERTIFICATE_DATES_WRONG_DURATION = "duration wrong";

    private static final String CERTIFICATE_DATES_COURSE_NUMBER = "Course number sample";

    private static final String CERTIFICATE_DATES_STUDY_FORM = "Study form sample";

    private static final String ARCHIVE_OBJECT = "archiveObject";

    @InjectMocks
    private CertificateDatesServiceImpl certificateDatesService;
    @Mock
    private CertificateDatesRepository certificateDatesRepository;
    @Mock
    private DtoConverter dtoConverter;
    @Mock
    private ArchiveService archiveService;
    @Mock
    private ObjectMapper objectMapper;

    private CertificateDates certificateDates;
    private CertificateDatesArch certificateDatesArch;

    @BeforeEach
    void setUp() {
        certificateDates = CertificateDates.builder()
            .id(CERTIFICATE_DATES_ID)
            .date(CERTIFICATE_DATES_DATE)
            .studyForm(CERTIFICATE_DATES_STUDY_FORM)
            .courseNumber(CERTIFICATE_DATES_COURSE_NUMBER)
            .duration(CERTIFICATE_DATES_DURATION)
            .hours(CERTIFICATE_DATES_HOURS)
            .build();
        certificateDatesArch = CertificateDatesArch.builder()
            .dates(CERTIFICATE_DATES_DATE)
            .studyForm(CERTIFICATE_DATES_STUDY_FORM)
            .courseNumber(CERTIFICATE_DATES_COURSE_NUMBER)
            .duration(CERTIFICATE_DATES_DURATION)
            .hours(CERTIFICATE_DATES_HOURS)
            .build();
    }

    @Test
    void getCertificateDatesByCorrectId() {
        when(certificateDatesRepository.findById(CORRECT_ID)).thenReturn(Optional.ofNullable(certificateDates));
        assertThat(certificateDatesService.getCertificateDatesById(CORRECT_ID)).isEqualTo(certificateDates);
    }


    @Test
    void getCertificateDatesByWrongId() {
        when(certificateDatesRepository.findById(WRONG_ID)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> certificateDatesService.getCertificateDatesById(WRONG_ID))
            .isInstanceOf(NotExistException.class);
    }


    @Test
    void addCertificateDates() {
        when(certificateDatesRepository.save(certificateDates)).thenReturn(certificateDates);
        assertThat(certificateDatesService.addCertificateDates(certificateDates)).isEqualTo(certificateDates);
    }

    @Test
    void getCertificateDatesByCorrectDate() {
        when(certificateDatesRepository.findByDate(CERTIFICATE_DATES_DATE)).thenReturn(
            Optional.ofNullable(certificateDates));
        assertThat(certificateDatesService.getCertificateDatesByDate(CERTIFICATE_DATES_DATE)).isEqualTo(
            certificateDates);
    }

    @Test
    void getCertificateDatesByWrongDate() {
        when(certificateDatesRepository.findByDate(CERTIFICATE_DATES_WRONG_DATE)).thenReturn(
            Optional.empty());
        assertThatThrownBy(() -> certificateDatesService.getCertificateDatesByDate(CERTIFICATE_DATES_WRONG_DATE))
            .isInstanceOf(NotExistException.class);
    }

    @Test
    void getCertificateDatesByCorrectHoursAndDate() {
        when(certificateDatesRepository.findByHoursAndDate(CERTIFICATE_DATES_HOURS, CERTIFICATE_DATES_DATE))
            .thenReturn(Optional.ofNullable(certificateDates));
        assertThat(
            certificateDatesService.getCertificateDatesByHoursAndDate(CERTIFICATE_DATES_HOURS,
                CERTIFICATE_DATES_DATE)).isEqualTo(certificateDates);
    }

    @Test
    void getCertificateDatesByWrongHoursAndDate() {
        when(certificateDatesRepository.findByHoursAndDate(CERTIFICATE_DATES_WRONG_HOURS, CERTIFICATE_DATES_WRONG_DATE))
            .thenReturn(Optional.empty());
        assertThatThrownBy(
            () -> certificateDatesService.getCertificateDatesByHoursAndDate(CERTIFICATE_DATES_WRONG_HOURS,
                CERTIFICATE_DATES_WRONG_DATE))
            .isInstanceOf(NotExistException.class);
    }


    @Test
    void getCertificateDatesByCorrectDuration() {
        when(certificateDatesRepository.findByDuration(CERTIFICATE_DATES_DURATION)).thenReturn(
            Optional.ofNullable(certificateDates));
        assertThat(certificateDatesService.getCertificateDatesByDuration(CERTIFICATE_DATES_DURATION)).isEqualTo(
            certificateDates);
    }

    @Test
    void getCertificateDatesByWrongDuration() {
        when(certificateDatesRepository.findByDuration(CERTIFICATE_DATES_WRONG_DURATION)).thenReturn(
            Optional.empty());

        assertThatThrownBy(
            () -> certificateDatesService.getCertificateDatesByDuration(CERTIFICATE_DATES_WRONG_DURATION))
            .isInstanceOf(NotExistException.class);
    }

    @Test
    void getCertificateDatesByCorrectDurationAndDate() {
        when(certificateDatesRepository.findByDurationAndDate(CERTIFICATE_DATES_DURATION,
            CERTIFICATE_DATES_DATE)).thenReturn(Optional.ofNullable(certificateDates));

        assertThat(certificateDatesService.getCertificateDatesByDurationAndDate(CERTIFICATE_DATES_DURATION,
            CERTIFICATE_DATES_DATE)).isEqualTo(certificateDates);
    }

    @Test
    void getCertificateDatesByWrongDurationAndDate() {
        when(certificateDatesRepository.findByDurationAndDate(CERTIFICATE_DATES_WRONG_DURATION,
            CERTIFICATE_DATES_WRONG_DATE)).thenReturn(Optional.empty());

        assertThatThrownBy(
            () -> certificateDatesService.getCertificateDatesByDurationAndDate(CERTIFICATE_DATES_WRONG_DURATION,
                CERTIFICATE_DATES_WRONG_DATE))
            .isInstanceOf(NotExistException.class);
    }

    @Test
    void archiveModel() {
        when(dtoConverter.convertToDto(certificateDates, CertificateDatesArch.class)).thenReturn(certificateDatesArch);

        certificateDatesService.archiveModel(certificateDates);
        verify(archiveService).saveModel(certificateDatesArch);
    }

    @Test
    void restoreModel() throws JsonProcessingException {
        when(objectMapper.readValue(ARCHIVE_OBJECT, CertificateDatesArch.class)).thenReturn(certificateDatesArch);
        when(dtoConverter.convertToEntity(certificateDatesArch, CertificateDates.builder().build())).thenReturn(
            certificateDates);
        when(certificateDatesRepository.save(certificateDates)).thenReturn(certificateDates);

        certificateDatesService.restoreModel(ARCHIVE_OBJECT);
        verify(certificateDatesRepository).save(certificateDates);
    }

    @Test
    void restoreModelException() throws JsonProcessingException {
        when(objectMapper.readValue(ARCHIVE_OBJECT, CertificateDatesArch.class)).thenThrow(
            JsonProcessingException.class);

        assertThatThrownBy(() -> certificateDatesService.restoreModel(ARCHIVE_OBJECT))
            .isInstanceOf(JsonProcessingException.class);
    }
}
