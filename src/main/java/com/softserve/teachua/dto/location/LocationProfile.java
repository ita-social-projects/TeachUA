package com.softserve.teachua.dto.location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softserve.teachua.utils.validations.CheckRussian;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@With
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationProfile {
    private Long id;

    @NotBlank
    @Size(min = 5, max = 100, message = "Length should be between 5 and 100 chars")
    @CheckRussian
    private String name;

    @NotBlank
    @CheckRussian
    private String address;
    private Long cityId;
    private Long districtId;
    private Long stationId;

    @NotBlank
    @CheckRussian
    private String cityName;

    @CheckRussian
    private String districtName;

    @CheckRussian
    private String stationName;

    @NotNull(message = " cannot be null")
    @NotBlank(message = " cannot be blank")
    private String coordinates;
    private Double longitude;
    private Double latitude;

    private Long centerId;
    private Long clubId;

    @NotNull(message = "number cannot be null")
    @NotBlank(message = "number cannot be blank")
    @Pattern(regexp = "0[\\d]{9}", message = "number should contain 10 digits start with 0")
    private String phone;
}
