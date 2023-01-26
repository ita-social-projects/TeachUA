package com.softserve.teachua.utils.validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@Slf4j
@PropertySource(value = "classpath:validation.properties")
public class CheckForeignLanguageValidator implements ConstraintValidator<CheckForeignLanguage, String> {
    @Value("${teachua.validation.ukrainian.flag}")
    private String ukrainianFlag;

    @Value("${teachua.validation.face.with.monocle}")
    private String smileFaceWithMonocle;

    @Override
    public void initialize(CheckForeignLanguage constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String text, ConstraintValidatorContext constraintValidatorContext) {
        if (text == null) {
            return true;
        }
        return text.matches(
                "^[А-ЩЬЮЯҐЄІЇа-щьюяґєіїa-zA-Z0-9 \\n?><,:;—–“”«»\"./{}()\\-_+=!@#$%&*|'’‘`~№\\[\\]"
                        + ukrainianFlag + smileFaceWithMonocle + "^]*$");
    }
}
