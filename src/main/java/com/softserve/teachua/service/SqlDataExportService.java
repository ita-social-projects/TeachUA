package com.softserve.teachua.service;

import java.sql.SQLException;
import java.util.List;

public interface SqlDataExportService {
    List<String> getListOfTableNames() throws SQLException;
}
