package com.softserve.teachua.model.archivable;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.commons.marker.Archivable;
import com.softserve.teachua.service.impl.UserServiceImpl;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

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
    public Class<UserServiceImpl> getServiceClass() {
        return UserServiceImpl.class;
    }
}
