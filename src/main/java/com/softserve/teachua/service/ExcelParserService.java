package com.softserve.teachua.service;

import com.softserve.teachua.dto.databaseTransfer.ExcelParsingResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * This interface contains all needed methods to manage exel parser.
 */

public interface ExcelParserService {
    /**
     * The method parses excel.
     */
    ExcelParsingResponse parseExcel(InputStream excelInputStream) throws IOException;
}
