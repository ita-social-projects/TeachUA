package com.softserve.teachua.service;

import java.sql.SQLException;
import java.util.List;

/**
 * This interface contains all needed methods to alter certificate_dates table.
 */
public interface CertificateDateSqlService {

    /**
     * This method drops unused columns from certificate_dates.
     * Returns true if drop was successful
     * {@code List<String>} of columns
     *
     */
    void dropUnusedColumns(List<String> columns) throws SQLException;

}
