package com.softserve.teachua.dto.test.question.questionExcel;

import com.softserve.teachua.dto.databaseTransfer.ExcelParsingMistake;
import com.softserve.teachua.dto.test.answer.answerExcel.AnswerExcel;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class ExcelQuestionParsingResponse {
    private List<ExcelParsingMistake> questionParsingMistakes = new ArrayList<>();
    private List<QuestionExcel> questionsInfo = new ArrayList<>();
    private List<AnswerExcel> answersInfo = new ArrayList<>();
}
