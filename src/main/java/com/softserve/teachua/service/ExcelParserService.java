package com.softserve.teachua.service;

import com.softserve.teachua.dto.databaseTransfer.ExcelParsingResponse;

import java.io.IOException;
import java.io.InputStream;

public interface ExcelParserService {
    ExcelParsingResponse parseExcel(InputStream excelInputStream) throws IOException;
}
