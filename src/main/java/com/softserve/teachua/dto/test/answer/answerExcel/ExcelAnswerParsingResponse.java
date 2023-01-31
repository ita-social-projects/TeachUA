package com.softserve.teachua.dto.test.answer.answerExcel;

import com.softserve.teachua.dto.databaseTransfer.ExcelParsingMistake;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class ExcelAnswerParsingResponse {
    private List<ExcelParsingMistake> parsingMistakes = new ArrayList<>();
    private List<AnswerExcel> questionsInfo = new ArrayList<>();
}
