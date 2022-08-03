package com.softserve.teachua.dto.test.group;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
public class ResponseGroup extends RepresentationModel<ResponseGroup> {
    private String title;
}
