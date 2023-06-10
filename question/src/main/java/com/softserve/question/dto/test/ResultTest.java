package com.softserve.question.dto.test;

import com.softserve.question.dto.question.QuestionResult;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@Setter
public class ResultTest {
    private String title;
    private List<QuestionResult> questions = new ArrayList<>();

    public void addQuestion(QuestionResult question) {
        questions.add(question);
    }
}
