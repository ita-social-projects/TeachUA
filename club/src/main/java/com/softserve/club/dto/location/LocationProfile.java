package com.softserve.club.dto.location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softserve.commons.util.validation.CheckRussian;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @NotBlank
    @CheckRussian
    private String cityName;
    @NotBlank
    @CheckRussian
    private String districtName;
    @NotBlank
    @CheckRussian
    private String stationName;

    private Long cityId;
    private Long districtId;
    private Long stationId;

    private Long centerId;
    private Long clubId;

    @NotNull(message = " cannot be null")
    @NotBlank(message = " cannot be blank")
    private String coordinates;
    private Double longitude;
    private Double latitude;


    @NotNull(message = "number cannot be null")
    @NotBlank(message = "number cannot be blank")
    @Pattern(regexp = "0[\\d]{9}", message = "number should contain 10 digits start with 0")
    private String phone;
}
