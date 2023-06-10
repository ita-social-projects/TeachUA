package com.softserve.question.dto.question.question_excel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class QuestionExcel {
    private String title;
    private String description;
    private String category;
    private String type;
}
