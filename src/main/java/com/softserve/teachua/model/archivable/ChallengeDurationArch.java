package com.softserve.teachua.model.archivable;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.marker.Archivable;
import com.softserve.teachua.service.impl.ChallengeDurationServiceImpl;
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
public class ChallengeDurationArch implements Convertible, Archivable {
    private Long id;

    private boolean userExist;

    private Long challengeId;

    private Long durationEntityId;

    @Override
    public Class getServiceClass() {
        return ChallengeDurationServiceImpl.class;
    }
}
