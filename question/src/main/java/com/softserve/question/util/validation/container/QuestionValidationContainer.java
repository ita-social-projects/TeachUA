package com.softserve.question.util.validation.container;

import com.softserve.question.util.validation.Violation;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QuestionValidationContainer implements ValidationContainer {
    private List<Violation> questionViolations = new ArrayList<>();

    @Override
    public String toString() {
        return questionViolations.toString();
    }
}
