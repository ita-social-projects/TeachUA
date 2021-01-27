package com.softserve.teachua.service.impl;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.converter.DtoConverter;
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
    private static final String EMAIL_ALREADY_EXIST = "Email %s already exist";
    private static final String USER_NOT_FOUND_BY_ID = "User not found by id %s";
    private static final String USER_NOT_FOUND_BY_EMAIL = "User not found by email %s";
    private static final String WRONG_PASSWORD = "Wrong password: %s";
    private static final String CANT_CREATE_USER = "Cant create user %s";

    private final UserRepository userRepository;
    private final EncoderService encodeService;
    private final RoleService roleService;
    private final DtoConverter dtoConverter;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           EncoderService encodeService,
                           RoleService roleService, DtoConverter dtoConverter) {
        this.userRepository = userRepository;
        this.encodeService = encodeService;
        this.roleService = roleService;
        this.dtoConverter = dtoConverter;
    }

    @Override
    public UserResponse getUserProfileById(Long id) {
        User user = getUserById(id);
        return dtoConverter.convertToDto(user, UserResponse.class);
    }

    @Override
    public UserEntity getUserEntity(String email) {
        return dtoConverter.convertToDto(getUserByEmail(email), UserEntity.class);
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
                .map(user -> (UserResponse) dtoConverter.convertToDto(user, UserResponse.class))
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

        User user = dtoConverter.convertToEntity(userProfile, new User())
                .withPassword(encodeService.encodePassword(userProfile.getPassword()))
                .withRole(roleService.findByName(RoleData.USER.getDBRoleName()));

        user = userRepository.save(user);
        if (user == null) {
            String userIsNull = String.format(CANT_CREATE_USER, userProfile);
            log.error(userIsNull);
            throw new DatabaseRepositoryException(userIsNull);
        }
        // TODO user.getRole().getName() delete ROLE_
        return dtoConverter.convertToDto(user, SuccessRegistration.class);
    }

    @Override
    public SuccessLogin validateUser(UserLogin userLogin) {
        UserEntity userEntity = getUserEntity(userLogin.getEmail());
        if (!encodeService.isValidPassword(userLogin, userEntity)) {
            String wrongPassword = String.format(WRONG_PASSWORD, userLogin.getPassword());
            log.error(wrongPassword);
            throw new WrongAuthenticationException(wrongPassword);
        }

        return dtoConverter.convertFromDtoToDto(userEntity, new SuccessLogin())
                .withAccessToken(encodeService.createToken(userEntity.getEmail()));
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
