package com.softserve.teachua.dto.center;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softserve.teachua.dto.location.LocationProfile;
import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.Club;
import com.softserve.teachua.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CenterProfile implements Convertible {

    private Long id;

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

    private List<LocationProfile> locations;

    @Valid
    @NotEmpty
    @Size(
            min = 40,
            max = 1500,
            message = "Довжина опису має бути від 40 до 1500 символів")
    @Pattern(
            regexp = "^[А-Яа-яіІєЄїЇґҐa-zA-Z0-9()\\\\!\\\"\\\"#$%&'*\\n+\\r, ,\\-.:;\\\\<=>—«»„”“–’‘?|@_`{}№~^/\\[\\]]+[^:эЭъЪыЫёЁ]$" ,
            message = "Це поле може містити тільки українські та англійські літери, цифри та спеціальні символи’")
    private String description;

    private String urlWeb;

    private String urlLogo;

    private User user;

    @NotEmpty(message = "Clubs are not selected")
    private List<Long> clubsId;

    private Long userId;

    private String contacts;

    private Long centerExternalId;

}
