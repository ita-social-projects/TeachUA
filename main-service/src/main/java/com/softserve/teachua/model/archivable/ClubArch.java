package com.softserve.teachua.model.archivable;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.clients.marker.Archivable;
import com.softserve.teachua.service.impl.ClubServiceImpl;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

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
    public Class<ClubServiceImpl> getServiceClass() {
        return ClubServiceImpl.class;
    }
}
