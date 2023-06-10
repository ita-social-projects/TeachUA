package com.softserve.question.dto.question.question_excel;

import com.softserve.commons.dto.database_transfer.ExcelParsingMistake;
import com.softserve.question.dto.answer.answer_excel.AnswerExcel;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class ExcelQuestionParsingResponse {
    private List<ExcelParsingMistake> questionParsingMistakes = new ArrayList<>();
    private List<QuestionExcel> questionsInfo = new ArrayList<>();
    private List<AnswerExcel> answersInfo = new ArrayList<>();
}
