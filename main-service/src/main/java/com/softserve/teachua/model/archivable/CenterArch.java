package com.softserve.teachua.model.archivable;

import com.softserve.commons.util.marker.Convertible;
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
public class CenterArch implements Convertible/*, Archivable*/ {
    private String name;
    private String contacts;
    private String urlBackgroundPicture;
    private String description;
    private String urlWeb;
    private String urlLogo;
    private Set<Long> locationsIds;
    private Set<Long> clubsIds;
    private Long userId;
    private Long centerExternalId;
    private Double rating;
    private Long clubCount;

    //todo@
    /*
    @Override
    public Class<CenterServiceImpl> getServiceClass() {
        return CenterServiceImpl.class;
    }
    */
}
