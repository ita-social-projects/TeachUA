package com.softserve.teachua.dto.test.answer.answerExcel;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class ExcelAnswerParsingResponse {
    private List<ExcelAnswerParsingMistake> parsingMistakes = new ArrayList<>();
    private List<AnswerExcel> questionsInfo = new ArrayList<>();
}
