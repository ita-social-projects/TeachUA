package com.softserve.teachua.service;

/**
 * This interface contains all needed methods to manage search_statistics table.
 */
public interface SearchStatisticsService {
    /**
     * Adds a query to the statistics.
     *
     * @param queryString The user's query string.
     * @param userId      User ID, if available.
     */
    void addToStatistics(String queryString, Long userId);
}
