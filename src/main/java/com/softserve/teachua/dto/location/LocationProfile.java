package com.softserve.teachua.dto.location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softserve.teachua.model.Club;
import lombok.*;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
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

    @NotNull
    private Long id;

    @NotEmpty
    @Size(
            min=5,
            max=100,
            message = "Length should be between 5 and 100 character"
    )
    @Pattern(regexp = "^[А-Яа-яіІєЄїЇґҐa-zA-Z0-9()\\\\!\\\"\\\"#$%&'*\\n+\\r, ,\\-.:;\\\\<=>—«»„”“–’‘?|@_`{}№~^/\\[\\]]+[^эЭъЪыЫёЁ]$" ,
             message = "Це поле може містити тільки українські та англійські літери, цифри та спеціальні символи’")
    private String name;

    private String address;
    private Long cityId;
    private Long districtId;
    private Long stationId;

    private String cityName;
    private String districtName;
    private String stationName;

    private String coordinates;
    private Double longitude;
    private Double latitude;

    private Long centerId;
    private Long clubId;

}
