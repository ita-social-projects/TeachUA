package com.softserve.teachua.service.impl;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.security.UserEntity;
import com.softserve.teachua.dto.user.*;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.DatabaseRepositoryException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.exception.WrongAuthenticationException;
import com.softserve.teachua.model.User;
import com.softserve.teachua.repository.UserRepository;
import com.softserve.teachua.security.service.EncoderService;
import com.softserve.teachua.service.ArchiveService;
import com.softserve.teachua.service.RoleService;
import com.softserve.teachua.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
    private static final String EMAIL_ALREADY_EXIST = "Email %s already exist";
    private static final String USER_NOT_FOUND_BY_ID = "User not found by id %s";
    private static final String USER_NOT_FOUND_BY_EMAIL = "User not found by email %s";
    private static final String WRONG_PASSWORD = "Wrong password: %s";
    private static final String USER_DELETING_ERROR = "Can't delete user cause of relationship";


    private final UserRepository userRepository;
    private final EncoderService encodeService;
    private final RoleService roleService;
    private final DtoConverter dtoConverter;
    private final ArchiveService archiveService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           EncoderService encodeService,
                           RoleService roleService, DtoConverter dtoConverter, ArchiveService archiveService) {
        this.userRepository = userRepository;
        this.encodeService = encodeService;
        this.roleService = roleService;
        this.dtoConverter = dtoConverter;
        this.archiveService = archiveService;
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
        Optional<User> optionalUser = getOptionalUserById(id);
        if (!optionalUser.isPresent()) {
            throw new NotExistException(String.format(USER_NOT_FOUND_BY_ID, id));
        }

        User user = optionalUser.get();
        log.info("getting user by id {}", user);
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
        Optional<User> optionalUser = getOptionalUserByEmail(email);
        if (!optionalUser.isPresent()) {
            throw new NotExistException(String.format(USER_NOT_FOUND_BY_EMAIL, email));
        }

        log.info("getting user by email {}", userRepository.findByEmail(email));
        return optionalUser.get();
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

        log.info("getting list of users {}", userResponses);
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
            throw new WrongAuthenticationException(String.format(EMAIL_ALREADY_EXIST, userProfile.getEmail()));
        }

        User user = dtoConverter.convertToEntity(userProfile, new User())
                .withPassword(encodeService.encodePassword(userProfile.getPassword()))
                .withRole(roleService.findByName(RoleData.USER.getDBRoleName()));

        user = userRepository.save(user);
        log.info("user {} registered successfully", user);
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
            throw new WrongAuthenticationException(String.format(WRONG_PASSWORD, userLogin.getPassword()));
        }

        log.info("user {} logged successfully", userLogin);
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
    public SuccessUpdatedUser updateUser(Long id, UserProfile userProfile) {
        User user = getUserById(id);

        User newUser = dtoConverter.convertToEntity(userProfile, user)
                .withId(id);

        log.info("updating role by id {}", newUser);
        return dtoConverter.convertToDto(userRepository.save(newUser), SuccessUpdatedUser.class);
    }

    /**
     * The method returns dto {@code UserResponse} of deleted user by id.
     *
     * @param id - put user id.
     * @return new {@code UserResponse}.
     * @throws DatabaseRepositoryException if user contain foreign keys.
     */
    @Override
    public UserResponse deleteUserById(Long id) {
        User user = getUserById(id);

        archiveService.saveModel(user);

        try {
            userRepository.deleteById(id);
        } catch (DataAccessException | ValidationException e) {
            throw new DatabaseRepositoryException(USER_DELETING_ERROR);
        }

        log.info("user {} was successfully deleted", user);
        return dtoConverter.convertToDto(user, UserResponse.class);
    }

    private boolean isUserExistByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    private Optional<User> getOptionalUserById(Long id) {
        return userRepository.findById(id);
    }

    private Optional<User> getOptionalUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
