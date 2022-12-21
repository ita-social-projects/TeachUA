package com.softserve.teachua.dto.test.question.questionExcel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExcelQuestionParsingMistake {
    private String errorDetails;
    private String cellValue;
    private Long rowIndex;
}
