package com.softserve.teachua.repository;

import com.softserve.teachua.model.SearchStatistics;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchStatisticsRepository extends JpaRepository<SearchStatistics, Long> {
    Optional<SearchStatistics> findFirstByQueryStringOrderByTimestampDesc(String query);

    @Query(value = "SELECT * FROM search_statistics ORDER BY search_date ASC LIMIT ?1", nativeQuery = true)
    List<SearchStatistics> findOldestRecords(int limit);

    List<SearchStatistics> findAllByTimestampBefore(Date timestamp);
}
