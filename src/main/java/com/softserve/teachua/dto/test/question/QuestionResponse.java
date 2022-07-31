package com.softserve.teachua.dto.test.question;

import lombok.Data;

import java.util.List;

@Data
public class QuestionResponse {
    private String title;
    private String description;
    private List<String> answerTitles;
}
