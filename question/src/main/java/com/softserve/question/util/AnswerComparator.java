package com.softserve.question.util;

import com.softserve.question.model.Answer;
import java.util.Comparator;

public class AnswerComparator implements Comparator<Answer> {
    @Override
    public int compare(Answer o1, Answer o2) {
        return o1.getText().compareTo(o2.getText());
    }
}
