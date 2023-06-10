package com.softserve.question.dto.test;

import com.softserve.commons.util.marker.Convertible;
import lombok.Data;

@Data
public class SuccessCreatedTest implements Convertible {
    private String title;
    private String description;
    private int difficulty;
    private int duration;
    private String topicTitle;
}
