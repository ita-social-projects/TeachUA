package com.softserve.teachua.service;

import com.softserve.teachua.dto.controller.SuccessLogin;
import com.softserve.teachua.dto.controller.SuccessRegistration;
import com.softserve.teachua.dto.controller.UserResponse;
import com.softserve.teachua.dto.security.UserEntity;
import com.softserve.teachua.dto.service.UserLogin;
import com.softserve.teachua.dto.service.UserProfile;
import com.softserve.teachua.model.User;

import java.util.List;

public interface UserService {

    UserEntity getUserEntity(String email);

    SuccessRegistration registerUser(UserProfile userProfile);

    SuccessLogin validateUser(UserLogin userLogin);

    //UserResponce findByLoginAndPassword(UserProfile userProfile);

    User getUserById(Long id);

//    List<UserEntity> getListOfUserEntities();

    List<UserResponse> getListOfUsers();

    UserProfile updateUserProfileById(Long id);

    UserResponse deleteUserById(Long id);

    UserResponse getUserProfileById(Long id);

}
