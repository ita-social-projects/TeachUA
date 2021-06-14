package com.softserve.teachua.dto.location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softserve.teachua.model.Club;
import lombok.*;

import java.util.List;

import javax.validation.Valid;
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

    @Pattern(regexp = "^(?!\\s)([\\wА-ЩЬЮЯҐЄІЇа-щьюяґєії !\\\"#$%&'()*+,\\-.\\/:;<=>?@\\]\\[^_`{}~]){5,100}$" ,
             message = "Це поле може містити тільки українські та англійські літери, цифри та спеціальні символи’")
    @Pattern(regexp = "^.*\\S$",
            message = "Це поле може містити тільки українські та англійські літери, цифри та спеціальні символи’")
    private String name;

    private String address;
    private Long cityId;
    private Long districtId;
    private Long stationId;

    private String cityName;
    private String districtName;
    private String stationName;

    private Double longitude;
    private Double latitude;

    private Long centerId;
    private Long clubId;

}
