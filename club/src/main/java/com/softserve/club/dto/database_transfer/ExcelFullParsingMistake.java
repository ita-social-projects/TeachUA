package com.softserve.club.dto.database_transfer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ExcelFullParsingMistake {
    private String sheetName;
    private String columnName;
    private Long rowIndex;
    private String cellValue;
    private String errorDetails;
    private boolean critical;
}
