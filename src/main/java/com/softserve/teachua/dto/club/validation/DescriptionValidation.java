package com.softserve.teachua.dto.club.validation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.exception.BadRequestException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

@Slf4j
public class DescriptionValidation implements ConstraintValidator<ClubDescription, String> {

    @Getter
    @NoArgsConstructor
    static class Block {
        String key;
        String text;
        String type;
        Long depth;
        Object inlineStyleRanges[];
        Object entityRanges[];
        Object data;
    }

    @Getter
    @NoArgsConstructor
    static class Description {
        Block blocks[];
        Object entityMap;
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Description descriptionClub = objectMapper.readValue(s, Description.class);

            Long summaryLength = 0L;
            String text = "";

            for(Block block : descriptionClub.blocks){
                text += block.text;
                summaryLength += block.text.length();
            }

            if(!text.matches("^[А-Яа-яіІєЄїЇґҐa-zA-Z0-9()\\\\!\\\"\\\"#$%&'*\\n+\\r, ,\\-.:;\\\\<=>—«»„”“–’‘?|@_`{}№~^/\\[\\]]+[^:эЭъЪыЫёЁ]$")){
                throw new BadRequestException("Це поле може містити тільки українські та англійські літери, цифри та спеціальні символи");
            }

            if(summaryLength < 40){
                throw new BadRequestException("Довжина опису не може бути меншою за 40 символів");
            }

            if(summaryLength > 1500){
                throw new BadRequestException("Довжина опису не може бути більшою за 1500 символів");
            }

            log.info("len: " + summaryLength);

            return true;
        } catch (JsonProcessingException e) {
            log.error("An exception occurred.");
        }
        return false;
    }


}

