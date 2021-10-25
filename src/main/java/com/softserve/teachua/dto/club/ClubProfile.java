package com.softserve.teachua.dto.club;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softserve.teachua.dto.gallery.GalleryPhotoProfile;
import com.softserve.teachua.dto.location.LocationProfile;
import com.softserve.teachua.dto.marker.Convertible;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.*;
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
    @JsonIgnoreProperties(ignoreUnknown = true)
    @NotEmpty
    @Size(
            min=40,
            max=1500,
            message = "Довжина тексту не відповідає критеріям: від 40 до 1500 символів."
    )
    @Pattern(regexp = "^[А-Яа-яіІєЄїЇґҐa-zA-Z0-9()\\\\!\\\"\\\"#$%&'*\\n+\\r, ,\\-.:;\\\\<=>—«»„”“–’‘?|@_`{}№~^/\\[\\]]+[^:эЭъЪыЫёЁ]$" ,
            message = "Це поле може містити тільки українські та англійські літери, цифри та спеціальні символи’")
    public String description;

    @Valid
    @NotEmpty
    @Size(
            min = 5,
            max = 100,
            message = "Довжина назви має бути від 5 до 100 символів")
    @Pattern(
            regexp = "^[А-Яа-яіІєЄїЇґҐ\\'a-zA-Z0-9()\\\\!\\\"\\\"#$%&'*\\n+\\r, ,\\-.:;\\\\<=>—«»„”“–’‘?|@_`{}№~^/\\[\\]]+[^:эЭъЪыЫёЁ]$" ,
            message = "Це поле може містити тільки українські та англійські літери, цифри та спеціальні символи’")
    private String name;

    @Min(2)
    @Max(17)
    @NotNull(message = "'AgeFrom' поле не може бути пустим")
    private Integer ageFrom;

    @Min(3)
    @Max(18)
    @NotNull(message = "'AgeTo' поле не може бути пустим")
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
