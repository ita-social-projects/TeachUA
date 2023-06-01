package com.softserve.teachua.dto.test.test;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.Data;

@Data
public class SuccessCreatedTest implements Convertible {
    private String title;
    private String description;
    private int difficulty;
    private int duration;
    private String topicTitle;
}
