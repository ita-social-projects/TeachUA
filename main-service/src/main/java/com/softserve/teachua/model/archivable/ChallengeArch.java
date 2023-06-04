package com.softserve.teachua.model.archivable;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.clients.marker.Archivable;
import com.softserve.teachua.service.impl.ChallengeServiceImpl;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@NoArgsConstructor
@AllArgsConstructor
@Data
@With
@Builder
public class ChallengeArch implements Convertible, Archivable {
    private String name;
    private String title;
    private String description;
    private String picture;
    private Long sortNumber;
    private String registrationLink;
    private Long userId;
    private Boolean isActive;
    private Set<Long> tasksIds;

    @Override
    public Class<ChallengeServiceImpl> getServiceClass() {
        return ChallengeServiceImpl.class;
    }
}
