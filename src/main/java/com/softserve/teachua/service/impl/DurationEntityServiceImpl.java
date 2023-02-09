package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.duration_entity.DurationEntityResponse;
import com.softserve.teachua.model.DurationEntity;
import com.softserve.teachua.repository.DurationEntityRepository;
import com.softserve.teachua.service.DurationEntityService;
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
    private final DurationEntityRepository durationEntityRepository;
    private final DtoConverter dtoConverter;

    @Autowired
    public DurationEntityServiceImpl(DurationEntityRepository durationEntityRepository, DtoConverter dtoConverter) {
        this.durationEntityRepository = durationEntityRepository;
        this.dtoConverter = dtoConverter;
    }

    @Override
    public List<DurationEntity> getListDurationEntity() {
        return durationEntityRepository.findAll();
    }

    public Set<DurationEntity> mapDurationResponseListToDurationEntity(
            List<DurationEntityResponse> durationEntityResponseList) {
        return durationEntityResponseList
                .stream()
                .map(durationEntityResponse -> new DurationEntity(
                        0L,
                        durationEntityResponse.getStartDate(),
                        durationEntityResponse.getEndDate()))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<DurationEntity> filterReceivedDurationForNewDuration(
            List<DurationEntityResponse> durationEntityResponseList) {
        List<DurationEntity> durationEntityFromDBList = getListDurationEntity();
        Set<DurationEntity> durationEntityReceivedSet =
                mapDurationResponseListToDurationEntity(durationEntityResponseList);

        Set<DurationEntity> durationEntityToAddSet = durationEntityReceivedSet
                .stream()
                .filter(durationEntity -> durationEntityFromDBList
                        .stream()
                        .noneMatch(durationEntityFromDB ->
                                durationEntityFromDB.getStartDate().equals(durationEntity.getStartDate())
                                    && durationEntityFromDB.getEndDate().equals(durationEntity.getEndDate())))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return durationEntityToAddSet;
    }

    public List<DurationEntity> createAllDurationFromResponseList(
            List<DurationEntityResponse> durationEntityResponseList) {
        Set<DurationEntity> durationEntitySet = filterReceivedDurationForNewDuration(durationEntityResponseList);
        return createAllDuration(durationEntitySet);
    }

    @Override
    public List<DurationEntity> createAllDuration(Set<DurationEntity> durationEntitySet) {
        return durationEntityRepository.saveAll(durationEntitySet);
    }
}
