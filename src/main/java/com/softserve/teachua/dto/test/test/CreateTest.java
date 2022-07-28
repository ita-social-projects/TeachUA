package com.softserve.teachua.dto.test.test;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.softserve.teachua.dto.test.question.QuestionProfile;
import com.softserve.teachua.utils.deserializers.TrimDeserialize;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.*;
import java.util.List;

@Data
public class CreateTest {
    @JsonDeserialize(using = TrimDeserialize.class)
    @NotBlank
    private String title;
    @NotBlank
    @Size(min = 20, max = 1000, message = "must contain a minimum of 20 and a maximum of 1000 letters")
    private String description;
    @NotNull
    @Min(value = 1, message = "difficulty scale should range from 1 to 10 points")
    private int difficulty;
    @NotNull
    @Min(value = 1, message = "duration must be equals to or greater than 1 minute")
    private int duration;
    @NotBlank
    private String topicTitle;
    @ToString.Exclude
    @NotEmpty
    private List<QuestionProfile> questions;
}
