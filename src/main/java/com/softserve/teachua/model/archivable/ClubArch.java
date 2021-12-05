package com.softserve.teachua.model.archivable;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.marker.Archivable;
import com.softserve.teachua.service.impl.ClubServiceImpl;
import lombok.*;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@With
@Builder
public class ClubArch implements Convertible, Archivable {
    private Long id;
    private Integer ageFrom;
    private Integer ageTo;
    private String name;
    private String description;
    private String urlWeb;
    private String urlLogo;
    private String urlBackground;
    private List<Long> urlGalleriesIds;
    private String workTime;
    private Double rating;
    private Long feedbackCount;
    private Boolean isOnline;
    private Set<Long> locationsIds;
    private Set<Long> categoriesIds;
    private Set<Long> feedbacksIds;
    private Long userId;
    private Long centerId;
    private Boolean isApproved;
    private String contacts;
    private Long clubExternalId;
    private Long centerExternalId;

    @Override
    public Class getServiceClass() {
        return ClubServiceImpl.class;
    }
}
