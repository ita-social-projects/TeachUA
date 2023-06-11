package com.softserve.teachua.dto.challenge;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softserve.commons.dto.UserPreview;
import com.softserve.commons.util.marker.Convertible;
import com.softserve.teachua.dto.task.TaskPreview;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChallengeProfile implements Convertible {
    private Long id;
    private String name;
    private String title;
    private String description;
    private String picture;
    private Long sortNumber;
    private Boolean isActive;
    private List<TaskPreview> tasks;
    private UserPreview user;
    private String registrationLink;
}
