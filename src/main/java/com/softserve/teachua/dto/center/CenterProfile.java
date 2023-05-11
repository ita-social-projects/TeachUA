package com.softserve.teachua.dto.center;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softserve.teachua.dto.location.LocationProfile;
import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.utils.validations.CheckRussian;
import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CenterProfile implements Convertible {
    private Long id;

    @Valid
    @NotBlank
    @Size(min = 5, max = 100, message = "Length should be between 5 and 100 chars")
    @CheckRussian
    private String name;

    @Valid
    @NotNull
    private List<LocationProfile> locations;

    @Valid
    @NotBlank
    @Size(min = 40, max = 1500, message = "Length should be between 40 and 1500 chars")
    @CheckRussian
    private String description;

    private String urlWeb;

    private String urlBackgroundPicture;

    private String urlLogo;

    @NotEmpty(message = "Clubs are not selected")
    private List<Long> clubsId;

    private Long userId;

    @NotNull
    @NotBlank
    private String contacts;

    private Long centerExternalId;
}
