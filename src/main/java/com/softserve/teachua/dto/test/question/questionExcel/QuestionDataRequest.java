package com.softserve.teachua.dto.test.question.questionExcel;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.softserve.teachua.dto.test.answer.answerExcel.AnswerExcel;
import java.util.List;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class QuestionDataRequest {
    @NotNull
    @JsonDeserialize
    List<QuestionExcel> questionsExcelList;

    @NotNull
    @JsonDeserialize
    List<AnswerExcel> answersExcelList;
}
