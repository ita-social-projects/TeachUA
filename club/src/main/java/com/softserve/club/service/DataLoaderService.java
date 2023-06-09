package com.softserve.club.service;

import com.softserve.club.dto.database_transfer.ExcelParsingData;

/**
 * This interface contains all needed methods to manage data loader.
 */

public interface DataLoaderService {
    /**
     * The method loads data from excel to database.
     */
    void loadToDatabase(ExcelParsingData excelParsingData);
}
