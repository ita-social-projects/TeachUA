package com.softserve.teachua.service.impl;

import com.softserve.teachua.dto.duration_entity.DurationEntityResponse;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.DurationEntity;
import com.softserve.teachua.repository.DurationEntityRepository;
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
public class DurationEntityServiceImpl implements DurationEntityService {
    private static final String DURATION_ENTITY_NOT_FOUND =
        "DurationEntity not found";
    private static final String DURATION_ENTITY_ALREADY_EXIST =
        "DurationEntity already exist with startDate: %s endDate: %s";
    private final DurationEntityRepository durationEntityRepository;

    @Autowired
    public DurationEntityServiceImpl(DurationEntityRepository durationEntityRepository) {
        this.durationEntityRepository = durationEntityRepository;
    }

    @Override
    public List<DurationEntity> getListDurationEntity() {
        List<DurationEntity> durationEntityList =
            durationEntityRepository.findAll();
        if (durationEntityList.isEmpty()) {
            throw new NotExistException(DURATION_ENTITY_NOT_FOUND);
        }
        log.debug("**/getting all DurationEntity");
        return durationEntityList;
    }

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

    public void createAllDurationEntityFromResponseList(
            List<DurationEntityResponse> durationEntityResponseList) {
        Set<DurationEntity> durationEntitySet = filterReceivedDurationForNewDuration(durationEntityResponseList);
        createAllDurationEntity(durationEntitySet);
    }

    @Override
    public List<DurationEntity> createAllDurationEntity(Set<DurationEntity> durationEntitySet) {
        return durationEntitySet.stream()
            .map(this::createDurationEntity)
            .collect(Collectors.toList());
    }

    public DurationEntity createDurationEntity(DurationEntity durationEntity) {
        boolean existsDurationEntity = isDurationEntityExistsByDates(
            durationEntity.getStartDate(),durationEntity.getEndDate());
        if (existsDurationEntity) {
            throw new AlreadyExistException(String.format(DURATION_ENTITY_ALREADY_EXIST,
                    durationEntity.getStartDate(),durationEntity.getEndDate()));
        }
        DurationEntity durationEntityAdded = durationEntityRepository.save(durationEntity);
        log.debug("**/adding new DurationEntity = {}", durationEntityAdded);
        return durationEntityAdded;
    }

    public boolean isDurationEntityExistsByDates(LocalDate startDate, LocalDate endDate) {
        boolean existsDurationEntity = durationEntityRepository
            .existsDurationEntityByStartDateAndEndDate(startDate, endDate);
        log.debug("**/checking existence DurationEntity with startDate= {} endDate= {}", startDate, endDate);
        return existsDurationEntity;
    }
}
