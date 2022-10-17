package com.softserve.teachua.dto.test.group;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@Builder
public class ResponseGroup extends RepresentationModel<ResponseGroup> {
    private String title;
}
