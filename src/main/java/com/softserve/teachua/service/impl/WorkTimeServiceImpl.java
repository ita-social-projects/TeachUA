package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Club;
import com.softserve.teachua.model.WorkTime;
import com.softserve.teachua.repository.WorkTimeRepository;
import com.softserve.teachua.service.ArchiveService;
import com.softserve.teachua.service.WorkTimeService;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class WorkTimeServiceImpl implements WorkTimeService {
    private static final String WORK_TIME_NOT_FOUND_BY_ID = "Work time not found by id: %s";

    private final DtoConverter dtoConverter;
    private final ArchiveService archiveService;
    private final WorkTimeRepository workTimeRepository;

    private final ObjectMapper objectMapper;

    @Autowired
    public WorkTimeServiceImpl(DtoConverter dtoConverter, ArchiveService archiveService,
                               WorkTimeRepository workTimeRepository, ObjectMapper objectMapper) {
        this.dtoConverter = dtoConverter;
        this.archiveService = archiveService;
        this.workTimeRepository = workTimeRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public WorkTime getWorkTimeById(Long id) {
        Optional<WorkTime> optionalWorkTime = workTimeRepository.findById(id);
        if (optionalWorkTime.isEmpty()) {
            throw new NotExistException(String.format(WORK_TIME_NOT_FOUND_BY_ID, id));
        }

        WorkTime workTime = optionalWorkTime.get();

        log.debug("Getting category by name = {}", workTime);
        return workTime;
    }

    @Override
    public Set<WorkTime> updateWorkTimeByClub(Set<WorkTime> workTimes, Club club) {
        if (club.getWorkTimes() != null) {
            club.getWorkTimes().forEach(workTime -> {
                workTime.setClub(null);
                workTimeRepository.deleteById(workTime.getId());
            });
        }
        if (workTimes == null || workTimes.isEmpty()) {
            return new HashSet<WorkTime>();
        }
        return workTimes.stream()
                .map(workTime -> workTimeRepository
                        .save(dtoConverter.convertToEntity(workTime, new WorkTime())
                                .withDay(workTime.getDay())
                                .withClub(club)
                                .withStartTime(workTime.getStartTime())
                                .withEndTime(workTime.getEndTime())))
                .collect(Collectors.toSet());
    }
}
