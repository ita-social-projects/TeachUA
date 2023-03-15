package com.softserve.teachua.model.archivable;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.marker.Archivable;
import com.softserve.teachua.service.impl.UserChallengeServiceImpl;
import java.time.LocalDate;
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
public class UserChallengeArch implements Convertible, Archivable {
    private Long id;

    private LocalDate registrationDate;

    private Long userId;

    private Long challengeDurationId;

    private Long userChallengeStatusId;

    @Override
    public Class getServiceClass() {
        return UserChallengeServiceImpl.class;
    }
}
