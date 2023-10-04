package com.softserve.teachua.service.impl;

import com.softserve.teachua.TestUtils;
import com.softserve.teachua.config.SearchStatisticsProperties;
import com.softserve.teachua.model.SearchStatistics;
import com.softserve.teachua.repository.SearchStatisticsRepository;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

class SearchStatisticsServiceImplTest {

    @Mock
    private SearchStatisticsRepository searchStatisticsRepository;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private SearchStatisticsProperties searchStatisticsProperties;
    @InjectMocks
    private SearchStatisticsServiceImpl searchStatisticsService;

    private final String SEARCH_DATE = "2023-10-01 00:00:00";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void testAddToStatistics() {
        String queryString = "query1";
        Long userId = 1L;

        when(searchStatisticsRepository.findFirstByQueryStringOrderByTimestampDesc(queryString))
                .thenReturn(Optional.ofNullable(TestUtils.getSearchStatistics()));
        when(searchStatisticsProperties.isToCache()).thenReturn(true);

        searchStatisticsService.addToStatistics(queryString, userId);

        ArgumentCaptor<SearchStatistics> captor = ArgumentCaptor.forClass(SearchStatistics.class);
        verify(searchStatisticsRepository).save(captor.capture());
        SearchStatistics savedRecord = captor.getValue();

        assertEquals(TestUtils.getSearchStatistics().getSearchCount() + 1, savedRecord.getSearchCount());
    }

    @Test
    public void testDeleteOldStatisticsByCount() {
        when(searchStatisticsRepository.count()).thenReturn(20L);
        when(searchStatisticsProperties.getMaxNumber()).thenReturn(10);

        List<SearchStatistics> oldRecords = new ArrayList<>();
        when(searchStatisticsRepository.findOldestRecords(5)).thenReturn(oldRecords);
        searchStatisticsService.deleteOldStatisticsByCount();

        verify(searchStatisticsRepository, times(1)).deleteAll(oldRecords);
    }

    @Test
    public void testDeleteOldStatisticsByTime() {
        List<SearchStatistics> oldRecords = new ArrayList<>();
        oldRecords.add(new SearchStatistics());
        oldRecords.add(new SearchStatistics());
        when(searchStatisticsProperties.getMaxTime()).thenReturn(SEARCH_DATE);

        Date date = searchStatisticsService.validateDate();

        when(searchStatisticsRepository.findAllByTimestampBefore(date)).thenReturn(oldRecords);

        searchStatisticsService.deleteOldStatisticsByTime();

        Mockito.verify(searchStatisticsRepository).findAllByTimestampBefore(date);
        verify(searchStatisticsRepository, times(1)).deleteAll(oldRecords);
    }

    @Test
    void testValidateDateWithInvalidFormat() {
        String invalidDate = "Invalid Date String";
        when(searchStatisticsProperties.getMaxTime()).thenReturn(invalidDate);

        assertThrows(IllegalArgumentException.class, () -> {
            searchStatisticsService.validateDate();
        });
    }
}
