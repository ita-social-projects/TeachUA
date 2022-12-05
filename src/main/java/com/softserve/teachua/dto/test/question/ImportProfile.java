package com.softserve.teachua.dto.test.question;


import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotBlank;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImportProfile extends RepresentationModel<ImportProfile> {
    @NonNull
    @NotBlank
    private String formId;
}
