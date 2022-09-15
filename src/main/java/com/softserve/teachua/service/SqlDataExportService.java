package com.softserve.teachua.service;

import java.sql.SQLException;
import java.util.List;

/**
 * This interface contains all needed methods to manage sql data exporting.
 */

public interface SqlDataExportService {
    /**
     * The method return created script.
     */
    String createScript() throws SQLException;
}
