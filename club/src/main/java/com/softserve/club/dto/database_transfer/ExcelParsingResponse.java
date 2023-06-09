package com.softserve.club.dto.database_transfer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class ExcelParsingResponse {
    private final List<ExcelFullParsingMistake> parsingMistakes = new ArrayList<>();

    /**
     * Total records count including records with mistakes.
     */
    private final Map<String, Long> sheetRowsCount = new HashMap<>();

    private final ExcelParsingData data = new ExcelParsingData();
}
