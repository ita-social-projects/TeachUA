package com.softserve.teachua.service.impl;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.dto.controller.SuccessLogin;
import com.softserve.teachua.dto.controller.SuccessRegistration;
import com.softserve.teachua.dto.controller.UserResponse;
import com.softserve.teachua.dto.security.UserEntity;
import com.softserve.teachua.dto.service.UserLogin;
import com.softserve.teachua.dto.service.UserProfile;
import com.softserve.teachua.exception.DatabaseRepositoryException;
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
    private static final String EMAIL_ALREADY_EXIST = "email %s already exist";
    private static final String USER_NOT_FOUND_BY_ID = "user not found by id %s";
    private static final String USER_NOT_FOUND_BY_EMAIL = "user not found by email %s";
    private static final String WRONG_PASSWORD = "wrong password: %s";
    private static final String CANT_CREATE_USER = "cant create user %s";

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
    public UserResponse getUserProfileById(Long id) {
        User user = getUserById(id);
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .password(user.getPassword())
                .roleName(user.getRole().getName())
                .build();
    }

    @Override
    public UserEntity getUserEntity(String email) {
        User user = getUserByEmail(email);
        return UserEntity.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .roleName(user.getRole().getName())
                .build();
    }

    @Override
    public User getUserById(Long id) {
        if (!isUserExistById(id)) {
            String userNotFoundById = String.format(USER_NOT_FOUND_BY_ID, id);
            log.error(userNotFoundById);
            throw new WrongAuthenticationException(userNotFoundById);
        }

        User user = userRepository.getById(id);
        log.info("**/getting user by id = " + user);
        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        if (!isUserExistByEmail(email)) {
            String userNotFoundByEmail = String.format(USER_NOT_FOUND_BY_EMAIL, email);
            log.error(userNotFoundByEmail);
            throw new WrongAuthenticationException(userNotFoundByEmail);
        }

        return userRepository.findByEmail(email);
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
    public SuccessRegistration registerUser(UserProfile userProfile) {
        if (isUserExistByEmail(userProfile.getEmail())) {
            String emailAlreadyExist = String.format(EMAIL_ALREADY_EXIST, userProfile.getEmail());
            log.error(emailAlreadyExist);
            throw new WrongAuthenticationException(emailAlreadyExist);
        }

        User user = User.builder()
                .email(userProfile.getEmail())
                .name(userProfile.getName())
                .password(encodeService.encodePassword(userProfile.getPassword()))
                .role(roleService.findByName(RoleData.USER.getDBRoleName()))
                .build();

        user = userRepository.save(user);
        if (user == null) {
            String userIsNull = String.format(CANT_CREATE_USER, userProfile);
            log.error(userIsNull);
            throw new DatabaseRepositoryException(userIsNull);
        }
        // TODO user.getRole().getName() delete ROLE_
        return new SuccessRegistration(user.getId(), user.getEmail(), user.getRole().getName());
    }

    @Override
    public SuccessLogin validateUser(UserLogin userLogin) {
        UserEntity userEntity = getUserEntity(userLogin.getEmail());
        if (!encodeService.isValidPassword(userLogin, userEntity)) {
            String wrongPassword = String.format(WRONG_PASSWORD, userLogin.getPassword());
            log.error(wrongPassword);
            throw new WrongAuthenticationException(wrongPassword);
        }

        return SuccessLogin.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .accessToken(encodeService.createToken(userEntity.getEmail()))
                .build();
    }

    @Override
    public UserProfile updateUserProfileById(Long id) {
        if (!isUserExistById(id)) {
            log.error(String.format(USER_NOT_FOUND_BY_ID, id));
            throw new WrongAuthenticationException(String.format(USER_NOT_FOUND_BY_ID, id));
        }

        log.info("**/update user = " + id);
        return null;
    }

    @Override
    public UserResponse deleteUserById(Long id) {
        UserResponse userResponse = getUserProfileById(id);
        userRepository.deleteById(id);

        log.info("**/delete user = " + id);
        return userResponse;
    }

    private boolean isUserExistByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    private boolean isUserExistById(Long id) {
        return userRepository.existsById(id);
    }
}
