package com.softserve.teachua.dto.challenge;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

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
    private String picture;
    private Long sortNumber;
    private Boolean isActive;
}
