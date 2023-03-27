package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.duration_entity.DurationEntityExist;
import com.softserve.teachua.dto.duration_entity.DurationEntityResponse;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.model.DurationEntity;
import com.softserve.teachua.model.archivable.DurationEntityArch;
import com.softserve.teachua.repository.DurationEntityRepository;
import com.softserve.teachua.service.ArchiveMark;
import com.softserve.teachua.service.ArchiveService;
import com.softserve.teachua.service.DurationEntityService;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class DurationEntityServiceImpl implements DurationEntityService, ArchiveMark<DurationEntity> {
    private static final String DURATION_ENTITY_ALREADY_EXIST =
        "DurationEntity already exist with startDate: %s endDate: %s";
    private final DurationEntityRepository durationEntityRepository;
    private final DtoConverter dtoConverter;
    private final ArchiveService archiveService;
    private final ObjectMapper objectMapper;

    @Autowired
    public DurationEntityServiceImpl(DurationEntityRepository durationEntityRepository, DtoConverter dtoConverter,
                                     ArchiveService archiveService, ObjectMapper objectMapper) {
        this.durationEntityRepository = durationEntityRepository;
        this.dtoConverter = dtoConverter;
        this.archiveService = archiveService;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<DurationEntity> getListDurationEntity() {
        List<DurationEntity> durationEntityList =
            durationEntityRepository.findAll();
        log.debug("**/getting all DurationEntity ={}", durationEntityList);
        return durationEntityList;
    }

    @Override
    public DurationEntityExist isDurationEntityExistsByDates(LocalDate startDate, LocalDate endDate) {
        boolean existsDurationEntity = durationEntityRepository
            .existsDurationEntityByStartDateAndEndDate(startDate, endDate);
        DurationEntityExist durationEntityExist =
            DurationEntityExist.builder().durationEntityExist(existsDurationEntity).build();
        log.debug("**/checking existence DurationEntity with startDate = {} endDate = {} result = {}",
            startDate, endDate, durationEntityExist);
        return durationEntityExist;
    }

    @Override
    public Set<DurationEntity> mapDurationResponseListToDurationEntity(
            List<DurationEntityResponse> durationEntityResponseList) {
        Set<DurationEntity> durationEntitySet = durationEntityResponseList
            .stream()
            .map(durationEntityResponse -> new DurationEntity(
                0L,
                durationEntityResponse.getStartDate(),
                durationEntityResponse.getEndDate()))
            .collect(Collectors.toCollection(LinkedHashSet::new));
        log.debug("**/mapping durationEntitySet = {}", durationEntitySet);
        return durationEntitySet;
    }

    public Set<DurationEntity> filterReceivedDurationForNewDuration(
            List<DurationEntityResponse> durationEntityResponseList) {
        List<DurationEntity> durationEntityFromDBList = getListDurationEntity();
        Set<DurationEntity> durationEntityReceivedSet =
                mapDurationResponseListToDurationEntity(durationEntityResponseList);
        Set<DurationEntity> filteredDurationEntitySet = durationEntityReceivedSet
            .stream()
            .filter(durationEntity -> durationEntityFromDBList
                .stream()
                .noneMatch(durationEntityFromDB ->
                    durationEntityFromDB.getStartDate().equals(durationEntity.getStartDate())
                        && durationEntityFromDB.getEndDate().equals(durationEntity.getEndDate())))
            .collect(Collectors.toCollection(LinkedHashSet::new));
        log.debug("**/filtering filteredDurationEntitySet = {}", filteredDurationEntitySet);
        return filteredDurationEntitySet;
    }

    public Set<DurationEntity> filterDurationEntityFromResponseThatAdded(
        List<DurationEntityResponse> durationEntityResponseSet) {
        List<DurationEntity> durationEntityList = getListDurationEntity();
        Set<DurationEntity> durationEntitySet = durationEntityList.stream()
            .filter(durationEntity -> durationEntityResponseSet
                .stream()
                .anyMatch(response -> durationEntity.getStartDate().equals(response.getStartDate())
                    && durationEntity.getEndDate().equals(response.getEndDate())))
            .collect(Collectors.toCollection(LinkedHashSet::new));
        log.debug("**/filtering durationEntitySet= {} for new ChallengeDuration", durationEntitySet);
        return durationEntitySet;
    }

    @Override
    public Set<DurationEntity> createAllDurationEntityFromResponseList(
        List<DurationEntityResponse> durationEntityResponseList) {
        Set<DurationEntity> durationEntitySet = filterReceivedDurationForNewDuration(durationEntityResponseList);
        createAllDurationEntity(durationEntitySet);
        Set<DurationEntity> resultDurationEntity =
            filterDurationEntityFromResponseThatAdded(durationEntityResponseList);
        log.debug("**/creating All resultDurationEntity= {} for new Set<DurationEntity>", resultDurationEntity);
        return resultDurationEntity;
    }

    public Set<DurationEntity> createAllDurationEntity(Set<DurationEntity> durationEntitySet) {
        return durationEntitySet.stream()
            .map(this::createDurationEntity)
            .collect(Collectors.toSet());
    }

    @Override
    public DurationEntity createDurationEntity(DurationEntity durationEntity) {
        boolean existsDurationEntity = isDurationEntityExistsByDates(
            durationEntity.getStartDate(),durationEntity.getEndDate()).isDurationEntityExist();
        if (existsDurationEntity) {
            throw new AlreadyExistException(String.format(DURATION_ENTITY_ALREADY_EXIST,
                    durationEntity.getStartDate(),durationEntity.getEndDate()));
        }
        DurationEntity durationEntityAdded = durationEntityRepository.save(durationEntity);
        log.debug("**/adding new DurationEntity = {}", durationEntityAdded);
        return durationEntityAdded;
    }

    @Override
    public void archiveModel(DurationEntity durationEntity) {
        DurationEntityArch durationEntityArch =
            dtoConverter.convertToDto(durationEntity, DurationEntityArch.class);
        archiveService.saveModel(durationEntityArch);
    }

    @Override
    public void restoreModel(String archiveObject) throws JsonProcessingException {
        DurationEntityArch durationEntityArch =
            objectMapper.readValue(archiveObject, DurationEntityArch.class);
        durationEntityRepository.save(
            dtoConverter.convertToEntity(durationEntityArch, DurationEntity.builder().build()));
    }
}
