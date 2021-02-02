package com.softserve.teachua.service.impl;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.security.UserEntity;
import com.softserve.teachua.dto.user.*;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.NotExistException;
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

    /**
     * The method returns dto {@code UserResponse} of user by id.
     *
     * @param id - put user id.
     * @return new {@code UserResponse}.
     */
    @Override
    public UserResponse getUserProfileById(Long id) {
        User user = getUserById(id);
        return dtoConverter.convertToDto(user, UserResponse.class);
    }

    /**
     * The method returns entity {@code UserEntity} of user by email.
     *
     * @param email - put user email.
     * @return new {@code UserEntity}.
     */
    @Override
    public UserEntity getUserEntity(String email) {
        return dtoConverter.convertToDto(getUserByEmail(email), UserEntity.class);
    }

    /**
     * The method returns entity {@code User} of user by id.
     *
     * @param id - put user id.
     * @return new {@code User}.
     * @throws NotExistException if user not exists.
     */
    @Override
    public User getUserById(Long id) {
        if (isUserExistById(id)) {
            String userNotFoundById = String.format(USER_NOT_FOUND_BY_ID, id);
            log.error(userNotFoundById);
            throw new NotExistException(userNotFoundById);
        }

        User user = userRepository.getById(id);
        log.info("**/getting user by id = " + user);
        return user;
    }

    /**
     * The method returns entity {@code User} of user by email.
     *
     * @param email - put user email.
     * @return new {@code User}.
     * @throws NotExistException if user not exists.
     */
    @Override
    public User getUserByEmail(String email) {
        if (!isUserExistByEmail(email)) {
            String userNotFoundByEmail = String.format(USER_NOT_FOUND_BY_EMAIL, email);
            log.error(userNotFoundByEmail);
            throw new NotExistException(userNotFoundByEmail);
        }

        log.info("**/getting user by email = " + userRepository.findByEmail(email));
        return userRepository.findByEmail(email);
    }

    /**
     * The method returns list of dto {@code List<UserResponse>} of all users.
     *
     * @return new {@code List<UserResponse>}.
     */
    @Override
    public List<UserResponse> getListOfUsers() {
        List<UserResponse> userResponses = userRepository.findAll()
                .stream()
                .map(user -> (UserResponse) dtoConverter.convertToDto(user, UserResponse.class))
                .collect(Collectors.toList());

        log.info("**/getting list of users = " + userResponses);
        return userResponses;
    }

    /**
     * The method returns dto {@code SuccessRegistration} if user successfully registered.
     *
     * @param userProfile- place dto with all params.
     * @return new {@code SuccessRegistration}.
     * @throws AlreadyExistException if user with email already exists.
     */
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
        log.info("**/user registered successfully = " + user);
        return dtoConverter.convertToDto(user, SuccessRegistration.class);
    }

    /**
     * The method returns dto {@code SuccessLogin} if user successfully logged.
     *
     * @param userLogin- place dto with all params.
     * @return new {@code SuccessLogin}.
     * @throws WrongAuthenticationException if user password is incorrect.
     */
    @Override
    public SuccessLogin validateUser(UserLogin userLogin) {
        UserEntity userEntity = getUserEntity(userLogin.getEmail());
        if (!encodeService.isValidPassword(userLogin, userEntity)) {
            String wrongPassword = String.format(WRONG_PASSWORD, userLogin.getPassword());
            log.error(wrongPassword);
            throw new WrongAuthenticationException(wrongPassword);
        }

        log.info("**/user logged successfully = " + userLogin);
        return dtoConverter.convertFromDtoToDto(userEntity, new SuccessLogin())
                .withAccessToken(encodeService.createToken(userEntity.getEmail()));
    }

    /**
     * The method returns dto {@code SuccessUpdatedUser} of updated user.
     *
     * @param userProfile - place dto with all params.
     * @return new {@code SuccessUpdatedUser}.
     * @throws NotExistException if user id is incorrect.
     */
    @Override
    public SuccessUpdatedUser updateUser(UserProfile userProfile) {
        if (isUserExistById(userProfile.getId())) {
            String userNotFoundById = String.format(USER_NOT_FOUND_BY_ID, userProfile.getId());
            log.error(userNotFoundById);
            throw new NotExistException(userNotFoundById);
        }

        User user = userRepository.save(dtoConverter.convertToEntity(userProfile, new User())
                .withPassword(encodeService.encodePassword(userProfile.getPassword())));
        log.info("**/updating user = " + user);
        return dtoConverter.convertToDto(user, SuccessUpdatedUser.class);
    }

    private boolean isUserExistByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    private boolean isUserExistById(Long id) {
        return userRepository.existsById(id);
    }
}
