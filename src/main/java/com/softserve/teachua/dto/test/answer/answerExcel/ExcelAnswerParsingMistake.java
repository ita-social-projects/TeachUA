package com.softserve.teachua.dto.test.answer.answerExcel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExcelAnswerParsingMistake {
    private String errorDetails;
    private String cellValue;
    private Long rowIndex;
}
