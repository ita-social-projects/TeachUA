package com.softserve.teachua.service;

import com.softserve.teachua.dto.duration_entity.DurationEntityExist;
import com.softserve.teachua.dto.duration_entity.DurationEntityResponse;
import com.softserve.teachua.model.DurationEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface DurationEntityService {
    List<DurationEntity> getListDurationEntity();

    Set<DurationEntity> createAllDurationEntityFromResponseList(
        List<DurationEntityResponse> durationEntityResponseList);

    Set<DurationEntity> mapDurationResponseListToDurationEntity(
        List<DurationEntityResponse> durationEntityResponseList);

    DurationEntityExist isDurationEntityExistsByDates(LocalDate startDate, LocalDate endDate);

    DurationEntity createDurationEntity(DurationEntity durationEntity);
}
