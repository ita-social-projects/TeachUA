package com.softserve.teachua.service;

import java.sql.SQLException;
import java.util.List;

/**
 * This interface contains all needed methods to alter certificate_dates table.
 */
public interface CertificateDateSqlService {

    /**
     * This method move misplaced data("course_description", "picture_path", "project_description") from certificate_dates table to certificate_templates table.
     */
    void moveData() throws SQLException;

    /**
     * This method drops unused columns from certificate_dates.
     * Returns true if drop was successful
     * {@code List<String>} of columns
     *
     */
    void dropUnusedColumns(List<String> columns) throws SQLException;

}
