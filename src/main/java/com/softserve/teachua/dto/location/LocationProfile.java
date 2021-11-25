package com.softserve.teachua.dto.location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softserve.teachua.utils.validations.CheckRussian;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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
    @NotBlank
    @CheckRussian
    private String districtName;
    @NotBlank
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
    @Pattern(regexp = "[\\d]{9}", message = "Incorrect phone Number")
    private String phone;
}
