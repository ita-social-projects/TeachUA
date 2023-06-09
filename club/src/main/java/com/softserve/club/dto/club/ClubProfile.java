package com.softserve.club.dto.club;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softserve.club.dto.club.validation.ClubDescription;
import com.softserve.club.dto.gallery.GalleryPhotoProfile;
import com.softserve.club.dto.location.LocationProfile;
import com.softserve.commons.util.marker.Convertible;
import com.softserve.commons.util.validation.CheckRussian;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
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
public class ClubProfile implements Convertible {
    private Long id;

    @NotEmpty
    @Size(min = 5, max = 100, message = "Довжина назви має бути від 5 до 100 символів")
    @CheckRussian
    private String name;

    @CheckRussian
    @ClubDescription
    @Size(min = 40, max = 1500, message = "Description should be between 40 and 1500 chars")
    public String description;

    private Long centerId;

    @NotEmpty
    private List<String> categoriesName;

    @Valid
    private List<LocationProfile> locations;

    @Min(2)
    @Max(17)
    @NotNull(message = "поле не може бути пустим")
    private Integer ageFrom;

    @Min(3)
    @Max(18)
    @NotNull(message = "поле не може бути пустим")
    private Integer ageTo;

    private String urlBackground;

    private String urlLogo;

    private List<GalleryPhotoProfile> urlGallery;

    private Boolean isOnline;

    private String contacts;

    private Boolean isApproved;

    private Long userId;

    private Long clubExternalId;

    private Long centerExternalId;
}
