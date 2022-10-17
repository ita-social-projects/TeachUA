package com.softserve.teachua.utils.test.containers;

import com.softserve.teachua.model.test.Answer;
import lombok.Getter;

import java.util.Set;

@Getter
public class AnswerResultContainer {
    private final int answerValue;
    private final long answerId;
    private final boolean correct;
    private boolean selected;

    public AnswerResultContainer(Answer answer) {
        answerValue = answer.getValue();
        correct = answer.isCorrect();
        answerId = answer.getId();
    }

    public void calculateGrade(QuestionResultContainer questionResultContainer, Set<Long> selectedAnswerIds) {
        if (correct) {
            questionResultContainer.incrementCorrectAmount();
        }
        if (selectedAnswerIds.contains(answerId)) {
            selected = true;
            if (correct) {
                questionResultContainer.increaseGrade(answerValue);
                questionResultContainer.incrementCorrectSelectedAmount();
            } else {
                questionResultContainer.decreaseGrade(answerValue);
            }
        }
    }
}
