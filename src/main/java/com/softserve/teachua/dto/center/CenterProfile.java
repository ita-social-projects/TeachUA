package com.softserve.teachua.dto.center;

import com.softserve.teachua.dto.location.LocationProfile;
import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.Club;
import com.softserve.teachua.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CenterProfile implements Convertible {

    private Long id;

    @NotEmpty
    private String name;

    private List<LocationProfile> locations;

    private String description;

    private String urlWeb;

    private String urlLogo;

    private User user;

    private List<Long> clubsId;

    private Long userId;

    private String contacts;

    private Long externalId;

}
