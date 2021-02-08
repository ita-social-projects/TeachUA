package com.softserve.teachua.dto.center;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CenterProfile implements Convertible {

    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String email;

    @NotEmpty
    private String address;

    @NotEmpty
    private String phones;

    @NotEmpty
    private String description;

    @NotEmpty
    private String urlWeb;

    @NotEmpty
    private String urlLogo;

    @NotEmpty
    private String socialLinks;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    private User user;

}
