package com.softserve.teachua.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.model.archivable.DurationEntityArch;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.duration_entity.DurationEntityResponse;
import com.softserve.teachua.model.DurationEntity;
import com.softserve.teachua.repository.DurationEntityRepository;
import com.softserve.teachua.service.impl.DurationEntityServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static com.softserve.teachua.TestUtils.getDurationEntityList;
import static com.softserve.teachua.TestUtils.getDurationEntityResponseListWithRepeats;
import static com.softserve.teachua.TestUtils.getDurationEntitySetWithoutZeroId;
import static com.softserve.teachua.TestUtils.getDurationEntitySet;
import static com.softserve.teachua.TestUtils.getDurationEntity;
import static com.softserve.teachua.TestUtils.getDurationEntityArch;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static com.softserve.teachua.TestConstants.DURATION_ENTITY_ID;
import static com.softserve.teachua.TestConstants.START_DATE;
import static com.softserve.teachua.TestConstants.END_DATE;
import static com.softserve.teachua.TestConstants.ONE_TIMES_VERIFY;
import static com.softserve.teachua.TestConstants.TWO_TIMES_VERIFY;
import static com.softserve.teachua.TestConstants.NUM_OF_GENERATED_ELEMENTS;
import static com.softserve.teachua.TestConstants.DURATION_ENTITY_ALREADY_EXIST;

@ExtendWith(MockitoExtension.class)
class DurationEntityServiceTest {
    @InjectMocks
    private DurationEntityServiceImpl underTest;
    @Mock
    private DurationEntityRepository durationEntityRepository;
    @Mock
    private ArchiveService archiveService;
    @Mock
    private DtoConverter dtoConverter;
    @Mock
    private ObjectMapper objectMapper;
    private List<DurationEntity> durationEntityList;
    private List<DurationEntityResponse> durationEntityResponseList;
    private Set<DurationEntity> durationEntitySet;
    private DurationEntity durationEntity;
    private DurationEntityArch durationEntityArch;

    @Test
    void getListDurationEntity() {
        getPreparedDataForGetListDurationEntity(
            NUM_OF_GENERATED_ELEMENTS, DURATION_ENTITY_ID, START_DATE, END_DATE);
        when(durationEntityRepository.findAll()).thenReturn(durationEntityList);
        assertThat(underTest.getListDurationEntity())
            .isNotNull()
            .isNotEmpty()
            .isEqualTo(durationEntityList);
        verify(durationEntityRepository, times(ONE_TIMES_VERIFY)).findAll();
    }

    @Test
    void isDurationEntityExistsByDatesTrue() {
        when(durationEntityRepository.existsDurationEntityByStartDateAndEndDate(START_DATE, END_DATE))
            .thenReturn(true);
        assertThat(underTest.isDurationEntityExistsByDates(
            START_DATE, END_DATE).isDurationEntityExist()).isTrue();
        verify(durationEntityRepository, times(ONE_TIMES_VERIFY))
            .existsDurationEntityByStartDateAndEndDate(START_DATE, END_DATE);
    }

    @Test
    void isDurationEntityExistsByDatesFalse() {
        when(durationEntityRepository.existsDurationEntityByStartDateAndEndDate(START_DATE, END_DATE))
            .thenReturn(false);
        assertThat(underTest.isDurationEntityExistsByDates(
            START_DATE, END_DATE).isDurationEntityExist()).isFalse();
        verify(durationEntityRepository, times(ONE_TIMES_VERIFY))
            .existsDurationEntityByStartDateAndEndDate(START_DATE, END_DATE);
    }

    @Test
    void mapDurationResponseListToDurationEntity() {
        getPreparedDataForMapDurationResponseListToDurationEntity(
            NUM_OF_GENERATED_ELEMENTS, START_DATE, END_DATE);
        assertThat(underTest.mapDurationResponseListToDurationEntity(durationEntityResponseList))
            .isEqualTo(durationEntitySet);
    }

    @Test
    public void filterReceivedDurationForNewDurationTestEmptyRequest() {
        getPreparedDataForFilterReceivedDurationForNewDurationTestEmptyRequest(
            NUM_OF_GENERATED_ELEMENTS, DURATION_ENTITY_ID, START_DATE, END_DATE);
        when(durationEntityRepository.findAll()).thenReturn(durationEntityList);
        assertThat(underTest.filterReceivedDurationForNewDuration(durationEntityResponseList))
            .isEqualTo(durationEntitySet);
        verify(durationEntityRepository, times(ONE_TIMES_VERIFY)).findAll();
    }

    @Test
    public void filterReceivedDurationForNewDurationTestEmptyResponse() {
        getPreparedDataForFilterReceivedDurationForNewDurationTestEmptyResponse(
            NUM_OF_GENERATED_ELEMENTS, DURATION_ENTITY_ID, START_DATE, END_DATE);
        when(durationEntityRepository.findAll()).thenReturn(durationEntityList);
        assertThat(underTest.filterReceivedDurationForNewDuration(durationEntityResponseList))
            .isEqualTo(durationEntitySet);
        verify(durationEntityRepository, times(ONE_TIMES_VERIFY)).findAll();
    }

    @Test
    public void filterDurationEntityFromResponseThatAddedTest() {
        getPreparedDataForFilterDurationEntityFromResponseThatAddedTest(
            NUM_OF_GENERATED_ELEMENTS, DURATION_ENTITY_ID, START_DATE, END_DATE);
        when(durationEntityRepository.findAll()).thenReturn(durationEntityList);
        assertThat(underTest.filterDurationEntityFromResponseThatAdded(durationEntityResponseList))
            .isEqualTo(durationEntitySet);
        verify(durationEntityRepository, times(ONE_TIMES_VERIFY)).findAll();
    }

    @Test
    void createAllDurationEntityFromResponseList() {
        getPreparedDataForCreateAllDurationEntityFromResponseList(
            NUM_OF_GENERATED_ELEMENTS, DURATION_ENTITY_ID, START_DATE, END_DATE);
        when(durationEntityRepository.findAll()).thenReturn(durationEntityList);
        assertThat(underTest.createAllDurationEntityFromResponseList(durationEntityResponseList))
            .isEqualTo(durationEntitySet);
        verify(durationEntityRepository, times(TWO_TIMES_VERIFY)).findAll();
    }

    @Test
    void createDurationEntitySuccess() {
        getPreparedDataForCreateDurationEntity(NUM_OF_GENERATED_ELEMENTS, START_DATE, END_DATE);
        when(durationEntityRepository.save(durationEntity)).thenReturn(durationEntity);
        assertThat(underTest.createDurationEntity(durationEntity)).isEqualTo(durationEntity);
        verify(durationEntityRepository, times(ONE_TIMES_VERIFY)).save(durationEntity);
    }

    @Test
    void createDurationEntityException() {
        getPreparedDataForCreateDurationEntity(DURATION_ENTITY_ID, START_DATE, END_DATE);
        when(durationEntityRepository.existsDurationEntityByStartDateAndEndDate(START_DATE, END_DATE)).thenReturn(true);
        assertThatThrownBy(() -> underTest.createDurationEntity(durationEntity))
            .isInstanceOf(AlreadyExistException.class)
            .hasMessage(String.format(DURATION_ENTITY_ALREADY_EXIST, START_DATE, END_DATE));
        verify(durationEntityRepository, times(ONE_TIMES_VERIFY))
            .existsDurationEntityByStartDateAndEndDate(START_DATE, END_DATE);
    }

    @Test
    void archiveModel() {
        getPreparedDataForArchiveRestoreModel(DURATION_ENTITY_ID, START_DATE, END_DATE);
        when(dtoConverter.convertToDto(durationEntity, DurationEntityArch.class))
            .thenReturn(durationEntityArch);
        underTest.archiveModel(durationEntity);
        verify(archiveService, times(ONE_TIMES_VERIFY)).saveModel(durationEntityArch);
    }

    @Test
    void restoreModel() throws JsonProcessingException {
        getPreparedDataForArchiveRestoreModel(DURATION_ENTITY_ID, START_DATE, END_DATE);
        when(objectMapper.readValue("string", DurationEntityArch.class))
            .thenReturn(durationEntityArch);
        when(dtoConverter.convertToEntity(durationEntityArch, DurationEntity.builder().build()))
            .thenReturn(durationEntity);
        underTest.restoreModel("string");
        verify(durationEntityRepository, times(ONE_TIMES_VERIFY)).save(durationEntity);
    }

    public void getPreparedDataForGetListDurationEntity(
        int numOfGeneratedElements, long firstId, LocalDate firstStartDate, LocalDate firstEndDate) {
        durationEntityList =
            getDurationEntityList(numOfGeneratedElements, firstId, firstStartDate, firstEndDate);
    }

    public void getPreparedDataForMapDurationResponseListToDurationEntity(
        int numOfGeneratedElements, LocalDate firstStartDate, LocalDate firstEndDate) {
        durationEntityResponseList =
            getDurationEntityResponseListWithRepeats(numOfGeneratedElements, firstStartDate, firstEndDate);
        durationEntitySet =
            getDurationEntitySetWithoutZeroId(numOfGeneratedElements, firstStartDate,firstEndDate);
    }

    public void getPreparedDataForFilterReceivedDurationForNewDurationTestEmptyRequest(
        int numOfGeneratedElements, long firstId, LocalDate firstStartDate, LocalDate firstEndDate) {
        durationEntityList =
            Collections.emptyList();
        getDurationEntityList(numOfGeneratedElements, firstId , firstStartDate, firstEndDate);
        durationEntityResponseList =
            getDurationEntityResponseListWithRepeats(numOfGeneratedElements, firstStartDate, firstEndDate);
        durationEntitySet =
            getDurationEntitySetWithoutZeroId(numOfGeneratedElements, firstStartDate, firstEndDate);
    }

    public void getPreparedDataForFilterReceivedDurationForNewDurationTestEmptyResponse(
        int numOfGeneratedElements, long firstId, LocalDate firstStartDate, LocalDate firstEndDate) {
        durationEntityList =
            getDurationEntityList(numOfGeneratedElements, firstId , firstStartDate, firstEndDate);
        durationEntityResponseList =
            getDurationEntityResponseListWithRepeats(numOfGeneratedElements, firstStartDate, firstEndDate);
        durationEntitySet = Collections.emptySet();
    }

    public void getPreparedDataForFilterDurationEntityFromResponseThatAddedTest(
        int numOfGeneratedElements, long firstId, LocalDate firstStartDate, LocalDate firstEndDate) {
        durationEntityList =
            getDurationEntityList(numOfGeneratedElements, firstId , firstStartDate, firstEndDate);
        durationEntityResponseList =
            getDurationEntityResponseListWithRepeats(numOfGeneratedElements, firstStartDate, firstEndDate);
        durationEntitySet = getDurationEntitySet(numOfGeneratedElements, firstId , firstStartDate, firstEndDate);
    }

    public void getPreparedDataForCreateAllDurationEntityFromResponseList(
        int numOfGeneratedElements, long firstId, LocalDate firstStartDate, LocalDate firstEndDate) {
        durationEntityList =
            getDurationEntityList(numOfGeneratedElements, firstId, firstStartDate, firstEndDate);
        durationEntityResponseList =
            getDurationEntityResponseListWithRepeats(numOfGeneratedElements, firstStartDate, firstEndDate);
        durationEntitySet =
            getDurationEntitySet(numOfGeneratedElements, firstId, firstStartDate, firstEndDate);
    }

    public void getPreparedDataForCreateDurationEntity(long id, LocalDate startDate, LocalDate endDate) {
        durationEntity = getDurationEntity(id, startDate, endDate);
    }

    public void getPreparedDataForArchiveRestoreModel(long id, LocalDate startDate, LocalDate endDate) {
        durationEntity = getDurationEntity(id, startDate, endDate);
        durationEntityArch = getDurationEntityArch(id, startDate, endDate);
    }
}