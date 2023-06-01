package com.softserve.teachua.dto.club.validation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.exception.IncorrectInputException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DescriptionValidation implements ConstraintValidator<ClubDescription, String> {
    @Getter
    @NoArgsConstructor
    static class Block {
        String key;
        String text;
        String type;
        Long depth;
        Object[] inlineStyleRanges;
        Object[] entityRanges;
        Object data;
    }

    @Getter
    @NoArgsConstructor
    static class Description {
        Block[] blocks;
        Object entityMap;
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Description descriptionClub = objectMapper.readValue(s, Description.class);

            StringBuilder text = new StringBuilder();

            for (Block block : descriptionClub.blocks) {
                text.append(block.text);
            }

            if (!text.toString().matches("^[А-Яа-яіІєЄїЇґҐa-zA-Z0-9()"
                    + "!\"#$%&'*\\n+\\r, \\-.:;\\\\<=>—«»„”“–’‘?|@_`{}№~^/\\[\\]]+$")) {
                throw new IncorrectInputException("Це поле може містити тільки українські та англійські літери, "
                        + "цифри та спеціальні символи");
            }

            if (!text.toString().matches("^[^эЭъЪыЫёЁ]+$")) {
                throw new IncorrectInputException("Опис гуртка не може містити російські літери");
            }

            if (text.length() < 40) {
                throw new IncorrectInputException("Довжина опису не може бути меншою за 40 символів");
            }

            if (text.length() > 1500) {
                throw new IncorrectInputException("Довжина опису не може бути більшою за 1500 символів");
            }

            return true;
        } catch (JsonProcessingException e) {
            throw new IncorrectInputException("Помилка під час парсингу опису. Неправильний JSON формат");
        }
    }
}
