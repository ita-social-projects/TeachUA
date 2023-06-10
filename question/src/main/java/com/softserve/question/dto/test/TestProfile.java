package com.softserve.question.dto.test;

import com.softserve.commons.util.marker.Convertible;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class TestProfile extends RepresentationModel<TestProfile> implements Convertible {
    private String title;
    private String description;
    private int difficulty;
    private int duration;
    private boolean archived;
}
