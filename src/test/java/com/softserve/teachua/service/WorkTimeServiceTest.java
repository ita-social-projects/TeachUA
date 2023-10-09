package com.softserve.teachua.service;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.exception.IncorrectInputException;
import com.softserve.teachua.model.Club;
import com.softserve.teachua.model.WorkTime;
import com.softserve.teachua.repository.WorkTimeRepository;
import com.softserve.teachua.service.impl.WorkTimeServiceImpl;
import java.util.HashSet;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class WorkTimeServiceTest {

    private static final Long CORRECT_WORK_TIME_ID = 1L;
    private static final Long CORRECT_CLUB_ID = 5L;
    private Club club;
    private Set<WorkTime> workTimeSet;
    @Mock
    private DtoConverter dtoConverter;
    private Set<Club> clubSet;
    private WorkTime workTime;
    @Mock
    private WorkTimeRepository workTimeRepository;
    @InjectMocks
    private WorkTimeServiceImpl workTimeServiceImpl;

    @BeforeEach
    void setUp() {
        club = Club.builder().id(CORRECT_CLUB_ID).workTimes(workTimeSet).build();
        clubSet = new HashSet<>();
        clubSet.add(club);

        workTime = WorkTime.builder().id(CORRECT_WORK_TIME_ID).build();

        workTimeSet = new HashSet<>();
        workTimeSet.add(workTime);
    }

    @Test
    void updateWorkTimeByClubShouldReturnWorkTimeSet() {

        when(dtoConverter.convertToEntity(workTimeSet, new WorkTime())).thenReturn(workTime);
        when(workTimeRepository.save(any(WorkTime.class))).thenReturn(workTime);
        Set<WorkTime> actual = workTimeServiceImpl.updateWorkTimeByClub(workTimeSet, club);
        assertThat(actual).isEqualTo(workTimeSet);
    }

    @Test
    void updateWorkTimeByEmptyWorkTimeSetShouldReturnIncorrectInputException() {
        assertThatThrownBy(() -> workTimeServiceImpl.updateWorkTimeByClub(null, club)).isInstanceOf(
                IncorrectInputException.class);
    }
}
