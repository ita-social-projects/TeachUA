package com.softserve.teachua.dto.test.test;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
public class TestProfile extends RepresentationModel<TestProfile> implements Convertible {
    private String title;
    private String description;
    private int difficulty;
    private int duration;
}
