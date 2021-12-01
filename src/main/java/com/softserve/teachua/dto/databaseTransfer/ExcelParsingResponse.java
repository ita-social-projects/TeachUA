package com.softserve.teachua.dto.databaseTransfer;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Data
public class ExcelParsingResponse {
    private final List<ExcelParsingMistake> parsingMistakes = new ArrayList<>();

    // total records count including records with mistakes
    private final Map<String, Long> sheetRowsCount = new HashMap<>();

    private final ExcelParsingData data = new ExcelParsingData();
}
