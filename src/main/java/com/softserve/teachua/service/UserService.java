package com.softserve.teachua.service;

import com.softserve.teachua.dto.RoleResponce;
import com.softserve.teachua.dto.controller.SuccessLogin;
import com.softserve.teachua.dto.controller.SuccessRegistration;
import com.softserve.teachua.dto.security.UserEntity;
import com.softserve.teachua.dto.service.UserLogin;
import com.softserve.teachua.dto.service.UserProfile;
import com.softserve.teachua.model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    UserEntity getUserEntity(String email);

    SuccessRegistration registerUser(UserProfile userProfile);

    SuccessLogin validateUser(UserLogin userLogin);

    //UserResponce findByLoginAndPassword(UserProfile userProfile);


    User getUserById(Long id);

//    List<UserEntity> getListOfUserEntities();

    List<User> getListOfUsers();

    UserProfile updateUserProfileById(Long id);

    ResponseEntity<UserProfile> deleteUserById(Long id);


    UserProfile getUserProfileById(Long id);

    List<RoleResponce> getAllRoles();

}
