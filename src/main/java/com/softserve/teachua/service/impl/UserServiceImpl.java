package com.softserve.teachua.service.impl;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.dto.controller.SuccessLogin;
import com.softserve.teachua.dto.controller.SuccessRegistration;
import com.softserve.teachua.dto.controller.UserResponse;
import com.softserve.teachua.dto.security.UserEntity;
import com.softserve.teachua.dto.service.UserLogin;
import com.softserve.teachua.dto.service.UserProfile;
import com.softserve.teachua.exception.WrongAuthenticationException;
import com.softserve.teachua.model.User;
import com.softserve.teachua.repository.UserRepository;
import com.softserve.teachua.security.service.EncoderService;
import com.softserve.teachua.service.RoleService;
import com.softserve.teachua.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
    private static final String EMAIL_ALREADY_EXIST = "Email %s already exist";
    private static final String USER_NOT_FOUND_BY_ID = "User not found by id %s";
    private static final String USER_NOT_FOUND_BY_EMAIL = "User not found by email %s";

    private final UserRepository userRepository;
    private final EncoderService encodeService;
    private final RoleService roleService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           EncoderService encodeService,
                           RoleService roleService) {
        this.userRepository = userRepository;
        this.encodeService = encodeService;
        this.roleService = roleService;
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.getById(id);
    }

    @Override
    public List<UserResponse> getListOfUsers() {
        List<UserResponse> userResponses = userRepository.findAll()
                .stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getPassword(),
                        user.getRole().getName()
                ))
                .collect(Collectors.toList());

        log.info("**/getting list of users = " + userResponses);
        return userResponses;
    }

    @Override
    public UserProfile updateUserProfileById(Long id) {
        if (!isUserExistById(id)) {
            log.error(String.format(USER_NOT_FOUND_BY_ID, id));
            throw new WrongAuthenticationException(String.format(USER_NOT_FOUND_BY_ID, id));
        }

        log.info("**/ update user = " + id);
        return null;
    }

    @Override
    public UserResponse deleteUserById(Long id) {
        UserResponse userResponse = getUserProfileById(id);
        userRepository.deleteById(id);

        log.info("**/delete user = " + id);
        return userResponse;
    }

    @Override
    public UserResponse getUserProfileById(Long id) {
        if (!isUserExistById(id)) {
            log.error(String.format(USER_NOT_FOUND_BY_ID, id));
            throw new WrongAuthenticationException(String.format(USER_NOT_FOUND_BY_ID, id));
        }

        User user = getUserById(id);

        log.info("**/getting user by id = " + user);
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .password(user.getPassword())
                .roleName(user.getRole().getName())
                .build();
    }

    public UserEntity getUserEntity(String email) {
        if (!isUserExistByEmail(email)) {
            log.error(String.format(USER_NOT_FOUND_BY_EMAIL, email));
            throw new WrongAuthenticationException(String.format(USER_NOT_FOUND_BY_EMAIL, email));
        }

        User user = getUserByEmail(email);
        return UserEntity.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .roleName(user.getRole().getName())
                .build();
    }

    public SuccessRegistration registerUser(UserProfile userProfile) {
        if (isUserExistByEmail(userProfile.getEmail())) {
            log.error(String.format(EMAIL_ALREADY_EXIST, userProfile.getEmail()));
            // TODO Develop DuplicateEmailException
            throw new WrongAuthenticationException(String.format(EMAIL_ALREADY_EXIST, userProfile.getEmail()));
        }

        User user = User.builder()
                .email(userProfile.getEmail())
                .password(encodeService.encodePassword(userProfile.getPassword()))
                .role(roleService.findByName(RoleData.USER.getDBRoleName()))
                .build();

        user = userRepository.save(user);
        if (user == null) {
            // TODO Develop Custom Exception
            throw new RuntimeException("Invalid User");
        }
        // TODO user.getRole().getName() delete ROLE_
        return new SuccessRegistration(user.getId(), user.getEmail(), user.getRole().getName());
    }

    public SuccessLogin validateUser(UserLogin userLogin) {
        UserEntity userEntity = getUserEntity(userLogin.getEmail());
        if (!encodeService.isValidPassword(userLogin, userEntity)) {
            // TODO Develop Custom Exception
            // else timestamp email status 403  "message": "The user does not exist by this email: string@aaa.com",
            throw new RuntimeException("BAD_PASSWORD");
        }
        return SuccessLogin.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .accessToken(encodeService.createToken(userEntity.getEmail()))
                .build();
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    private boolean isUserExistByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    private boolean isUserExistById(Long id) {
        return userRepository.existsById(id);
    }
}
