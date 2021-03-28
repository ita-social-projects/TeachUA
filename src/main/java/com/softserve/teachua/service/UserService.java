package com.softserve.teachua.service;

import com.softserve.teachua.dto.security.UserEntity;
import com.softserve.teachua.dto.user.*;
import com.softserve.teachua.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService {
    UserResponse getUserProfileById(Long id);

    UserEntity getUserEntity(String email);

    User getUserById(Long id);

    UserResponse deleteUserById(Long id);

    User getUserByEmail(String email);

    User getUserByVerificationCode(String verificationCode);

    List<UserResponse> getListOfUsers();

    SuccessRegistration registerUser(UserProfile userProfile, HttpServletRequest request);

    SuccessLogin validateUser(UserLogin userLogin);

    SuccessUpdatedUser updateUser(Long id, UserProfile userProfile);

    void updateUser(User user);

    SuccessVerification verify(String verificationCode);

    void validateUserId(Long id, HttpServletRequest httpServletRequest);
}
