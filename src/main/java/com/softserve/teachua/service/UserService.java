package com.softserve.teachua.service;

import com.softserve.teachua.dto.security.UserEntity;
import com.softserve.teachua.dto.user.*;
import com.softserve.teachua.model.User;

import java.util.List;

public interface UserService {
    UserResponse getUserProfileById(Long id);

    UserEntity getUserEntity(String email);

    User getUserById(Long id);

    UserResponse deleteUserById(Long id);

    User getUserByEmail(String email);

    List<UserResponse> getListOfUsers();

    SuccessRegistration registerUser(UserProfile userProfile);

    SuccessLogin validateUser(UserLogin userLogin);

    SuccessUpdatedUser updateUser(Long id, UserProfile userProfile);
}
