package com.softserve.teachua.service.impl;

import com.softserve.teachua.config.SearchStatisticsProperties;
import com.softserve.teachua.model.SearchStatistics;
import com.softserve.teachua.model.User;
import com.softserve.teachua.repository.SearchStatisticsRepository;
import com.softserve.teachua.service.SearchStatisticsService;
import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SearchStatisticsServiceImpl implements SearchStatisticsService {
    private final SearchStatisticsRepository searchStatisticsRepository;
    private final UserServiceImpl userService;
    private final SearchStatisticsProperties searchStatisticsProperties;


    @Autowired
    SearchStatisticsServiceImpl(SearchStatisticsRepository searchStatisticsRepository,
                                UserServiceImpl userService,
                                SearchStatisticsProperties searchStatisticsProperties) {
        this.searchStatisticsRepository = searchStatisticsRepository;
        this.userService = userService;
        this.searchStatisticsProperties = searchStatisticsProperties;
    }

    @Async
    @Transactional
    @Override
    public void addToStatistics(String queryString, Long userId) {
        if (!searchStatisticsProperties.isToCache()) {
            return;
        }

        Optional<SearchStatistics> existingQuery =
                searchStatisticsRepository.findFirstByQueryStringOrderByTimestampDesc(queryString);
        long totalQueries = 0;
        Optional<User> author = userId != null
                ? Optional.ofNullable(userService.getUserById(userId))
                : Optional.empty();

        if (existingQuery.isPresent()) {
            SearchStatistics record = existingQuery.get();
            totalQueries = record.getSearchCount();
        }

        SearchStatistics newRecord = SearchStatistics.builder()
                .queryString(queryString)
                .searchCount(++totalQueries)
                .timestamp(new Date())
                .user(author.orElse(null))
                .build();

        searchStatisticsRepository.save(newRecord);
        deleteOldStatisticsByCount();
    }

    public void deleteOldStatisticsByCount() {
        int maxNumber = searchStatisticsProperties.getMaxNumber();
        long totalRecords = searchStatisticsRepository.count();

        if (totalRecords >= searchStatisticsProperties.getMaxNumber()) {
            int recordsToDelete = maxNumber / 2;
            List<SearchStatistics> oldRecords = searchStatisticsRepository.findOldestRecords(recordsToDelete);
            searchStatisticsRepository.deleteAll(oldRecords);
        }
    }

    @Scheduled(cron = "@weekly")
    public void deleteOldStatisticsByTime() {
        try {
            Date date = searchStatisticsProperties.validateAndParseMaxTime();
            List<SearchStatistics> oldRecords = searchStatisticsRepository.findAllByTimestampBefore(date);
            if (oldRecords.size() > 0) {
                searchStatisticsRepository.deleteAll(oldRecords);
            }
            log.info("Old statistics deleted: " + oldRecords.size() + " records");
        } catch (Exception e) {
            log.error("Error while deleting old statistics: {}", e.getMessage());
        }
    }
}


