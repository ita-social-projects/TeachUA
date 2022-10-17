package com.softserve.teachua.dto.test.test;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class ViewTest extends RepresentationModel<ViewTest> {
    private String title;
    private String description;
    private int difficulty;
    private int duration;
    private boolean allowed;
}
