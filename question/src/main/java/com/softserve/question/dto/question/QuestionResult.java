package com.softserve.question.dto.question;

import com.softserve.question.dto.answer.AnswerResult;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@Setter
public class QuestionResult {
    private String title;
    private String status;
    private int value;
    private List<AnswerResult> answers = new ArrayList<>();

    public void add(AnswerResult answerResult) {
        answers.add(answerResult);
    }
}
