package com.softserve.teachua.dto.certificateExcel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExcelParsingMistake {

    private String errorDetails;
    private String cellValue;
    private Long rowIndex;

}
