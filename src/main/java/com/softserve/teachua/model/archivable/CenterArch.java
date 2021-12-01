package com.softserve.teachua.model.archivable;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.Club;
import com.softserve.teachua.model.Location;
import com.softserve.teachua.model.User;
import com.softserve.teachua.model.marker.Archivable;
import com.softserve.teachua.service.CenterService;
import com.softserve.teachua.service.impl.CenterServiceImpl;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@With
@Builder
public class CenterArch implements Convertible, Archivable {

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

    @Override
    public Class getServiceClass() {
        return CenterServiceImpl.class;
    }
}
