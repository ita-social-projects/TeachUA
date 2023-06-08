package com.softserve.teachua.dto.challenge;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softserve.commons.util.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SuccessUpdatedChallenge implements Convertible {
    private Long id;
    private String name;
    private String title;
    private String description;
    private String registrationLink;
    private String picture;
    private Long sortNumber;
    private Boolean isActive;
}
