package com.softserve.teachua.dto.test.question;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class QuestionResult {
    private String title;
    private String status;
    private int value;
    private Map<String, Boolean> answersCorrect;

    public void put(String answerTitle, Boolean correct) {
        answersCorrect.put(answerTitle, correct);
    }
}
