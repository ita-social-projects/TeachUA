package com.softserve.teachua.dto.club;

import com.softserve.teachua.dto.contact_data.ContactDataResponse;
import com.softserve.teachua.dto.gallery.GalleryPhotoProfile;
import com.softserve.teachua.dto.location.LocationProfile;
import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.Category;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@With
public class ClubProfile implements Convertible {

    private Long id;

    private List<String> categoriesName;

    @Valid
    private List<LocationProfile> locations;

//    @Pattern(regexp = "^(?!\\s)([\\wА-ЩЬЮЯҐЄІЇа-щьюяґєії \\/\\\\'’.,\"!?:*|><]){39,1500}\\S$" ,
//            message = "Це поле може містити тільки українські та англійські літери, цифри та спеціальні символи’")
//    @Pattern(regexp = "^.*\\S$",
//            message = "Це поле може містити тільки українські та англійські літери, цифри та спеціальні символи’")
    private String description;

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

    private Set<ContactDataResponse> contactsArray;
  
    private Boolean isApproved;

    private Long userId;

    private Long centerId;

    private Long clubExternalId;

    private Long centerExternalId;
}
