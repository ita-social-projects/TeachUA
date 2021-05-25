package com.softserve.teachua.dto.location;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class LocationProfile {
    private Long id;

//    @Pattern(regexp = "^(?!\\s)([\\wА-ЩЬЮЯҐЄІЇа-щьюяґєії !\\\"#$%&'()*+,\\-.\\/:;<=>?@\\]\\[^_`{}~]){5,100}$" ,
//             message = "Це поле може містити тільки українські та англійські літери, цифри та спеціальні символи’")
//    @Pattern(regexp = "^.*\\S$",
//            message = "Це поле може містити тільки українські та англійські літери, цифри та спеціальні символи’")
    private String name;

    private String address;
    private String cityName;
    private String districtName;
    private String stationName;
    private Double longitude;
    private Double latitude;
    private String phone;
    private Double key;
}
