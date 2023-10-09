package com.softserve.teachua.service;

import com.softserve.teachua.model.Club;
import com.softserve.teachua.model.WorkTime;
import java.util.Set;

public interface WorkTimeService {
    WorkTime getWorkTimeById(Long id);

    Set<WorkTime> updateWorkTimeByClub(Set<WorkTime> workTimes, Club club);
}
