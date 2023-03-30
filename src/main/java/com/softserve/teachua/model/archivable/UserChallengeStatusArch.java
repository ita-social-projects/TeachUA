package com.softserve.teachua.model.archivable;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.marker.Archivable;
import com.softserve.teachua.service.impl.UserChallengeStatusServiceImpl;
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
public class UserChallengeStatusArch implements Convertible, Archivable {
    private Long id;
    private String statusName;
    private String statusTitle;

    @Override
    public Class getServiceClass() {
        return UserChallengeStatusServiceImpl.class;
    }
}
