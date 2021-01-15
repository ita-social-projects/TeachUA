package com.softserve.teachua.service;

import java.util.List;

import com.softserve.teachua.dto.RoleResponce;
import com.softserve.teachua.dto.controller.SuccessLogin;
import com.softserve.teachua.dto.controller.SuccessRegistration;
import com.softserve.teachua.dto.security.UserEntity;
import com.softserve.teachua.dto.service.UserLogin;
import com.softserve.teachua.dto.service.UserProfile;

public interface UserService {

    UserEntity getUserEntity(String email);
    
	SuccessRegistration registerUser(UserProfile userProfile);
    
	SuccessLogin validateUser(UserLogin userLogin);

    //UserResponce findByLoginAndPassword(UserProfile userProfile);

    //String getExpirationLocalDate();
    
    List<RoleResponce> getAllRoles();
    
}
