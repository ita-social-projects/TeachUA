package com.softserve.teachua.utils.test.validation.container;

import com.softserve.teachua.utils.test.validation.Violation;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class QuestionValidationContainer implements ValidationContainer {
    private List<Violation> questionViolations = new ArrayList<>();
    
    @Override
    public String toString() {
        return questionViolations.toString();
    }
}
