package com.softserve.teachua.utils.test.validation.service;

import com.softserve.teachua.utils.test.validation.Violation;
import com.softserve.teachua.utils.test.validation.annotation.MaxAmount;
import com.softserve.teachua.utils.test.validation.annotation.MinAmount;
import com.softserve.teachua.utils.test.validation.annotation.WithoutBlank;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This validator checks for constraint violations related to
 * {@link MaxAmount}, {@link MinAmount}, {@link WithoutBlank} annotations.
 */

@Service
@RequiredArgsConstructor
public class ValidationService {
    @SneakyThrows
    public static void amountValidation(List<Violation> violations, Object object) {
        Field[] objectFields = object.getClass().getDeclaredFields();

        for (Field field : objectFields) {
            field.setAccessible(true);
            List<?> elements;
            Violation violation;
            MaxAmount maxQuestionsAmount = field.getAnnotation(MaxAmount.class);
            MinAmount minQuestionsAmount = field.getAnnotation(MinAmount.class);

            if (!Objects.isNull(maxQuestionsAmount)) {
                violation = new Violation();
                violation.setProperty(field.getName() + "Amounts");
                elements = (List<?>) field.get(object);

                if (elements.size() > maxQuestionsAmount.max()) {
                    violation.setMessage(maxQuestionsAmount.message());
                    violations.add(violation);
                }
            }

            if (!Objects.isNull(minQuestionsAmount)) {
                violation = new Violation();
                violation.setProperty(field.getName() + "Amounts");
                elements = (List<?>) field.get(object);

                if (elements.size() < minQuestionsAmount.min()) {
                    violation.setMessage(minQuestionsAmount.message());
                    violations.add(violation);
                }
            }
        }
    }

    @SneakyThrows
    public static void checkForBlank(List<Violation> violations, Object object) {
        Field[] objectFields = object.getClass().getDeclaredFields();

        for (Field field : objectFields) {
            WithoutBlank annotation = field.getAnnotation(WithoutBlank.class);

            if (!Objects.isNull(annotation)) {
                field.setAccessible(true);
                List<?> elements = (List<?>) field.get(object);

                for (Object element : elements) {
                    if (!Objects.isNull(element) && !((String) element).isEmpty()) {
                        Violation answerViolation = new Violation();
                        answerViolation.setProperty("answer");
                        answerViolation.setMessage(annotation.message());
                        violations.add(answerViolation);
                        break;
                    }
                }
            }
        }
    }

    public static <T> List<Violation> buildListViolation(Set<ConstraintViolation<T>> violations) {
        return violations.stream()
                .map(violation -> new Violation(
                        violation.getPropertyPath().toString(),
                        violation.getMessage()))
                .collect(Collectors.toList());
    }
}
