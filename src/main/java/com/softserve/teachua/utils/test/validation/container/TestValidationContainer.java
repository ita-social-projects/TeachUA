package com.softserve.teachua.utils.test.validation.container;


import com.softserve.teachua.utils.test.validation.Violation;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class TestValidationContainer implements ValidationContainer{
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
