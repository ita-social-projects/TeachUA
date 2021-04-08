package com.softserve.teachua.service;

import com.softserve.teachua.dto.databaseTransfer.ExcelParsingData;

public interface DataLoaderService {
    void loadToDatabase(ExcelParsingData excelParsingData);
}
