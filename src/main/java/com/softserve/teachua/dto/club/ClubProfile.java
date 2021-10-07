package com.softserve.teachua.dto.club;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softserve.teachua.dto.club.validation.ClubDescription;
import com.softserve.teachua.dto.gallery.GalleryPhotoProfile;
import com.softserve.teachua.dto.location.LocationProfile;
import com.softserve.teachua.dto.marker.Convertible;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@With
@JsonIgnoreProperties(ignoreUnknown = true)

public class ClubProfile implements Convertible {

    private Long id;

    private List<String> categoriesName;

    @Valid
    private List<LocationProfile> locations;

    @Valid
    @ClubDescription
    @JsonIgnoreProperties(ignoreUnknown = true)
//    @Pattern(regexp = "^[А-Яа-яёЁЇїІіЄєҐґa-zA-Z0-9()\\\\!\\\"\\\"#$%&'*\\n+\\r, ,\\-.:;\\\\<=>—«»„”“–’‘?|@_`{}№~^/\\[\\]]{40,1500}$" ,
//            message = "Це поле може містити тільки українські та англійські літери, цифри та спеціальні символи’")
    public String description;

    private String name;

    @Min(2)
    @Max(17)
    private Integer ageFrom;

    @Min(3)
    @Max(18)
    private Integer ageTo;

    private String urlBackground;

    private String urlLogo;

    private List<GalleryPhotoProfile> urlGallery;

    private Boolean isOnline;

    private String contacts;
  
    private Boolean isApproved;

    private Long userId;

    private Long centerId;

    private Long clubExternalId;

    private Long centerExternalId;
}
