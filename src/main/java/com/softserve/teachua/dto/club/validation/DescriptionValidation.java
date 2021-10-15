package com.softserve.teachua.dto.club.validation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class DescriptionValidation implements ConstraintValidator<ClubDescription, String> {

    @Getter
    @NoArgsConstructor
    static class Description {
        List<Block> blocks;
        Object entityMap;
        String key;
        String type;
        String inlineStyleRanges[];
        String entityRanges[];
        Object data;
    }

    @Getter
    @NoArgsConstructor
    static class Block {
        String text;
    }

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    @JsonIgnoreProperties(ignoreUnknown = true)
    public DescriptionValidation() throws JsonProcessingException {
        String description = "{\"blocks\":[" +
                "{\"key\":\"brl63\"," +
                "\"text\":\"dscsdcsdcsdcvsdvsdvsvsrvervgergfergergergergerg\"," +
                "\"type\":\"unstyled\"," +
                "\"depth\":0," +
                "\"inlineStyleRanges\":[]," +
                "\"entityRanges\":[]," +
                "\"data\":{}}" +
                "]," +
                "\"entityMap\":{}}";
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        Description descriptionClub = objectMapper.readValue(description, Description.class);
        System.out.println(descriptionClub);//?
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        try {
            objectMapper.readValue(s, Description.class);
            return true;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return false;
    }


}

