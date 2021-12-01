package com.softserve.teachua.model.archivable;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.marker.Archivable;
import com.softserve.teachua.service.impl.UserServiceImpl;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@With
@Data
public class UserArch implements Convertible, Archivable {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private String urlLogo;
    private String roleName;
    private String provider;
    private String providerId;
    private boolean status;
    private String verificationCode;

    @Override
    public Class getServiceClass() {
        return UserServiceImpl.class;
    }
}
