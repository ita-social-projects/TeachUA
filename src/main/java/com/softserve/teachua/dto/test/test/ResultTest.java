package com.softserve.teachua.dto.test.test;

import com.softserve.teachua.dto.test.question.QuestionResult;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ResultTest {
    private String title;
    private List<QuestionResult> questions = new ArrayList<>();

    public void addQuestion(QuestionResult question) {
        questions.add(question);
    }
}
