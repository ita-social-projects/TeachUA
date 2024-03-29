package com.softserve.teachua.dto.test.question.question_excel;

import com.softserve.teachua.dto.database_transfer.ExcelParsingMistake;
import com.softserve.teachua.dto.test.answer.answer_excel.AnswerExcel;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class ExcelQuestionParsingResponse {
    private List<ExcelParsingMistake> questionParsingMistakes = new ArrayList<>();
    private List<QuestionExcel> questionsInfo = new ArrayList<>();
    private List<AnswerExcel> answersInfo = new ArrayList<>();
}
