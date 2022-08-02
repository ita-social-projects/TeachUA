package com.softserve.teachua.dto.test.question;

import com.softserve.teachua.dto.test.answer.ResultAnswer;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class QuestionResult {
    private String title;
    private String status;
    private int value;
    private List<ResultAnswer> answers = new ArrayList<>();

    public void add(ResultAnswer resultAnswer) {
        answers.add(resultAnswer);
    }
}
