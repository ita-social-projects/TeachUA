package com.softserve.teachua.utils.test.validation.service;

import com.softserve.teachua.dto.test.question.QuestionProfile;
import com.softserve.teachua.dto.test.test.CreateTest;
import com.softserve.teachua.utils.test.validation.Violation;
import com.softserve.teachua.utils.test.validation.container.QuestionValidationContainer;
import com.softserve.teachua.utils.test.validation.container.TestValidationContainer;
import com.softserve.teachua.utils.test.validation.exception.CustomValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

import static com.softserve.teachua.utils.test.validation.service.ValidationService.*;

@Service
@RequiredArgsConstructor
public class TestValidationService {
    private final Validator validator;

    public void validateTest(CreateTest test) throws CustomValidationException {
        TestValidationContainer testValidationContainer = new TestValidationContainer();
        Set<ConstraintViolation<CreateTest>> testConstraintViolations = validator.validate(test);
        List<Violation> testViolations = buildListViolation(testConstraintViolations);
        amountValidation(testViolations, test);
        testValidationContainer.setTestViolations(testViolations);
        boolean isValid = testConstraintViolations.isEmpty();

        for (QuestionProfile question : test.getQuestions()) {
            QuestionValidationContainer container = new QuestionValidationContainer();
            Set<ConstraintViolation<QuestionProfile>> questionConstraintViolations = validateQuestion(question);
            List<Violation> questionViolations = buildListViolation(questionConstraintViolations);
            amountValidation(questionViolations, question);
            checkForBlank(questionViolations, question);
            container.setQuestionViolations(questionViolations);
            testValidationContainer.addQuestionValidationContainer(container);
            isValid = questionConstraintViolations.isEmpty();
        }

        if (!isValid){
            throw new CustomValidationException(HttpStatus.BAD_REQUEST, testValidationContainer);
        }
    }

    private Set<ConstraintViolation<QuestionProfile>> validateQuestion(QuestionProfile question) {
        return validator.validate(question);
    }
}
