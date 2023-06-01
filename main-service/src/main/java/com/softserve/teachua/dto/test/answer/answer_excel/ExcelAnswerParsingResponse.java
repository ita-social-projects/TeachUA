package com.softserve.teachua.dto.test.answer.answer_excel;

import com.softserve.teachua.dto.database_transfer.ExcelParsingMistake;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class ExcelAnswerParsingResponse {
    private List<ExcelParsingMistake> parsingMistakes = new ArrayList<>();
    private List<AnswerExcel> questionsInfo = new ArrayList<>();
}
