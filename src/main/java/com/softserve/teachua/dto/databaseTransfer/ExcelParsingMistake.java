package com.softserve.teachua.dto.databaseTransfer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ExcelParsingMistake {
    private String sheetName;
    private String columnName;
    private Long rowIndex;
    private String cellValue;
    private String errorDetails;
    private boolean critical;
}
