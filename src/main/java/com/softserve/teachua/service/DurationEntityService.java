package com.softserve.teachua.service;

import com.softserve.teachua.dto.duration_entity.DurationEntityResponse;
import com.softserve.teachua.model.DurationEntity;
import java.util.List;
import java.util.Set;

public interface DurationEntityService {
    List<DurationEntity> getListDurationEntity();

    Set<DurationEntity> filterReceivedDurationForNewDuration(
        List<DurationEntityResponse> durationEntityResponseList);

    void createAllDurationEntityFromResponseList(
        List<DurationEntityResponse> durationEntityResponseList);

    List<DurationEntity> createAllDurationEntity(
        Set<DurationEntity> durationEntitySet);
}
