package com.softserve.teachua.utils.test.validation.service;

import com.softserve.teachua.dto.test.question.QuestionProfile;
import com.softserve.teachua.dto.test.test.CreateTest;
import com.softserve.teachua.utils.test.validation.Violation;
import com.softserve.teachua.utils.test.validation.container.QuestionValidationContainer;
import com.softserve.teachua.utils.test.validation.container.TestValidationContainer;
import com.softserve.teachua.utils.test.validation.exception.TestValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestValidationService {
    private final Validator validator;

    public void validateTest(CreateTest test) throws TestValidationException {
        TestValidationContainer testValidationContainer = new TestValidationContainer();
        Set<ConstraintViolation<CreateTest>> testConstraintViolations = validator.validate(test);
        List<Violation> testViolations = buildListViolation(testConstraintViolations);
        testValidationContainer.setTestViolations(testViolations);
        boolean isValid = testConstraintViolations.isEmpty();

        for (QuestionProfile question : test.getQuestions()) {
            QuestionValidationContainer container = new QuestionValidationContainer();
            Set<ConstraintViolation<QuestionProfile>> questionConstraintViolations = validateQuestion(question);
            List<Violation> questionViolations = buildListViolation(questionConstraintViolations);
            container.setQuestionViolations(questionViolations);
            testValidationContainer.addQuestionValidationContainer(container);
            isValid = questionConstraintViolations.isEmpty();
        }

        if (!isValid){
            throw new TestValidationException(HttpStatus.BAD_REQUEST, testValidationContainer);
        }
    }

    private static <T> List<Violation> buildListViolation(Set<ConstraintViolation<T>> violations) {
        return violations.stream()
                .map(violation -> new Violation(
                        violation.getPropertyPath().toString(),
                        violation.getMessage()))
                .collect(Collectors.toList());
    }

    private Set<ConstraintViolation<QuestionProfile>> validateQuestion(QuestionProfile question) {
        return validator.validate(question);
    }
}
