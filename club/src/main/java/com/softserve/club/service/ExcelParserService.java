package com.softserve.club.service;

import com.softserve.club.dto.database_transfer.ExcelParsingResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * This interface contains all needed methods to manage exel parser.
 */
public interface ExcelParserService {
    /**
     * The method parses Excel file for initial db.
     *
     * @return new {@code ExcelParsingResponse}.
     */
    ExcelParsingResponse parseExcel(InputStream excelInputStream) throws IOException;
}
