package com.softserve.teachua.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.softserve.teachua.model.SearchStatistics;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class SearchStatisticsRepositoryTestIT {

    @Autowired
    private SearchStatisticsRepository searchStatisticsRepository;
    public static final String QUERY_STRING1 = "query1";
    public static final String QUERY_STRING3 = "query3";
    public static final String QUERY_STRING4 = "query4";
    public static final LocalDate SEARCH_DATE = LocalDate.of(2023, 6, 4);

    @Test
    public void testFindFirstByQueryStringOrderByTimestampDesc() {
        Optional<SearchStatistics> result =
                searchStatisticsRepository.findFirstByQueryStringOrderByTimestampDesc(QUERY_STRING1);
        assertTrue(result.isPresent());
        assertEquals(QUERY_STRING1, result.get().getQueryString());
    }

    @Test
    public void testFindOldestRecords() {
        List<SearchStatistics> oldestRecords = searchStatisticsRepository.findOldestRecords(2);
        assertEquals(2, oldestRecords.size());
        assertEquals(QUERY_STRING4, oldestRecords.get(0).getQueryString());
        assertEquals(QUERY_STRING3, oldestRecords.get(1).getQueryString());
    }

    @Test
    public void testFindAllByTimestampBefore() {
        Date date = java.sql.Date.valueOf(SEARCH_DATE);
        List<SearchStatistics> records = searchStatisticsRepository.findAllByTimestampBefore(date);
        assertEquals(1, records.size());
        assertEquals(records.get(0).getQueryString(), QUERY_STRING4);

    }
}
