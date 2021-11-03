package com.softserve.teachua.dto.location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
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
    @Size(min=5, max=100,
            message = "Length should be between 5 and 100 character")
    @Pattern(regexp = "[a-zA-zа-яА-Я0-9іІїЇєЄґҐ!.,№;&'`~{}%:?*()_+=\"\\[\\]\\\\\\/\\-\\s&&[^ёЁъЁыЫэЭ]]+",
            message = "You can use Ukrainian,English alphabet and some special char like: !.,№;&'`~{}%:?*()_+=\"[]\\/-")
    private String name;

    private String address;
    private Long cityId;
    private Long districtId;
    private Long stationId;
    @NotNull
    @NotEmpty
    @Pattern(regexp = "[a-zA-zа-яА-Я0-9іІїЇєЄґҐ\\\\.,:()_\\-\\s&&[^ёЁъЁыЫэЭ]]+",
            message = "You can use Ukrainian,English alphabet and some special char like: .,:()_\\/-")
    private String cityName;
    @NotNull
    @NotEmpty
    @Pattern(regexp = "[a-zA-zа-яА-Я0-9іІїЇєЄґҐ\\\\.,:()_\\-\\s&&[^ёЁъЁыЫэЭ]]+",
            message = "You can use Ukrainian,English alphabet and some special char like: .,:()_\\/-")
    private String districtName;
    @NotNull
    @NotEmpty
    @Pattern(regexp = "[a-zA-zа-яА-Я0-9іІїЇєЄґҐ\\\\.,:()_\\-\\s&&[^ёЁъЁыЫэЭ]]+",
            message = "You can use Ukrainian,English alphabet and some special char like: .,:()_\\/-")
    private String stationName;

    private String coordinates;
    private Double longitude;
    private Double latitude;

    private Long centerId;
    private Long clubId;

}
