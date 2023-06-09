package com.softserve.club.service;

import java.sql.SQLException;

/**
 * This interface contains all needed methods to manage sql data exporting.
 */

public interface SqlDataExportService {
    /**
     * The method return created script.
     */
    String createScript() throws SQLException;
}
