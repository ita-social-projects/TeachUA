package com.softserve.teachua.dto.test.question;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;


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
