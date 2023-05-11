package com.softserve.teachua.dto.club;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softserve.teachua.dto.category.CategoryResponse;
import com.softserve.teachua.dto.center.CenterForClub;
import com.softserve.teachua.dto.contact_data.ContactDataResponse;
import com.softserve.teachua.dto.location.LocationResponse;
import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.dto.user.UserPreview;
import com.softserve.teachua.model.GalleryPhoto;
import com.softserve.teachua.utils.validations.CheckRussian;
import java.util.List;
import java.util.Set;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClubResponse implements Convertible {
    private Long id;
    @NotNull(message = "Age from cannot be null")
    @Min(2)
    @Max(17)
    private Integer ageFrom;
    @NotNull(message = "Age to cannot be null")
    @Min(3)
    @Max(18)
    private Integer ageTo;
    @NotBlank(message = "name cannot be empty")
    @CheckRussian
    private String name;
    @NotBlank(message = "description cannot be empty")
    @CheckRussian
    @Size(min = 40, max = 1500, message = "description should be between 40 and 1500 chars")
    private String description;
    private String urlWeb;
    private String urlLogo;
    private String urlBackground;
    private List<GalleryPhoto> urlGallery;
    private String workTime;

    @NotNull(message = "categories cannot be null")
    private Set<CategoryResponse> categories;
    private UserPreview user;
    private CenterForClub center;
    private Double rating;
    private Set<LocationResponse> locations;
    private Boolean isApproved;
    private Boolean isOnline;
    private Long feedbackCount;
    @NotNull(message = "club should have contacts")
    private Set<ContactDataResponse> contacts;
}
