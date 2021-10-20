package com.softserve.teachua.dto.club;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    private List<String> categoriesName;

    @Valid
    private List<LocationProfile> locations;

    @Valid
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Pattern(regexp = "^[А-Яа-яЇїІіЄєҐґa-zA-Z0-9()\\\\!\\\"\\\"#$%&'*\\n+\\r, ,\\-.:;\\\\<=>—«»„”“–’‘?|@_`{}№~^/\\[\\]]{40,1500}$" ,
            message = "Це поле може містити тільки українські та англійські літери, цифри та спеціальні символи. " +
                    "Розмір поля повинен бути не менше 40 і не більше 1500 символів.’")
    public String description;

    @Valid
    @Pattern(regexp = "^[А-Яа-яЇїІіЄєҐґa-zA-Z0-9()/\\[\\]!\\\"#$%&'*+\\n, ,\\-.:\\r;<=>?]{5,100}$" ,
            message = "Це поле може містити тільки українські та англійські літери, цифри та спеціальні символи. " +
                    "Розмір поля повинен бути не менше 5 і не більше 100 символів.’")
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
