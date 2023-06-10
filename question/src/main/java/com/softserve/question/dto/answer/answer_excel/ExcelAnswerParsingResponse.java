package com.softserve.question.dto.answer.answer_excel;

import com.softserve.commons.dto.database_transfer.ExcelParsingMistake;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class ExcelAnswerParsingResponse {
    private List<ExcelParsingMistake> parsingMistakes = new ArrayList<>();
    private List<AnswerExcel> questionsInfo = new ArrayList<>();
}
