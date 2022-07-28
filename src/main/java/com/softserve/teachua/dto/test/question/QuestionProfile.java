package com.softserve.teachua.dto.test.question;

import lombok.Data;

import java.util.List;

@Data
public class QuestionProfile {
    private Long id;
    private String title;
    private String description;
    private List<String> answerTitles;
    private String categoryTitle;
    private Integer value;
    private List<Integer> correctAnswerIndexes;
}
