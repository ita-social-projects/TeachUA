package com.softserve.teachua.model.archivable;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.marker.Archivable;
import com.softserve.teachua.service.impl.UserServiceImpl;
import lombok.*;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@With
@Data
public class UserArch implements Convertible, Archivable {
    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private String urlLogo;
    private Integer roleId;
    private String provider;
    private String providerId;
    private boolean status;
    private String verificationCode;
    private List<Long> certificatesIds;

    @Override
    public Class getServiceClass() {
        return UserServiceImpl.class;
    }
}
