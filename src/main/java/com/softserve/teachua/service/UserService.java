package com.softserve.teachua.service;

import com.softserve.teachua.dto.user.SuccessLogin;
import com.softserve.teachua.dto.user.SuccessRegistration;
import com.softserve.teachua.dto.user.UserResponse;
import com.softserve.teachua.dto.security.UserEntity;
import com.softserve.teachua.dto.user.UserLogin;
import com.softserve.teachua.dto.user.UserProfile;
import com.softserve.teachua.model.User;

import java.util.List;

public interface UserService {
    UserResponse getUserProfileById(Long id);

    UserEntity getUserEntity(String email);

    User getUserById(Long id);

    User getUserByEmail(String email);

    List<UserResponse> getListOfUsers();

    SuccessRegistration registerUser(UserProfile userProfile);

    SuccessLogin validateUser(UserLogin userLogin);

    UserProfile updateUserProfileById(Long id);

    UserResponse deleteUserById(Long id);
}
