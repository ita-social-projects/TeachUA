package com.softserve.question.util.validation.container;

import com.softserve.question.util.validation.Violation;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TestValidationContainer implements ValidationContainer {
    private List<Violation> testViolations = new ArrayList<>();
    private List<QuestionValidationContainer> questions = new ArrayList<>();

    public void addQuestionValidationContainer(QuestionValidationContainer container) {
        questions.add(container);
    }

    @Override
    public String toString() {
        return testViolations.toString() + questions.toString();
    }
}
