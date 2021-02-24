package com.softserve.teachua.service;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.security.UserEntity;
import com.softserve.teachua.dto.user.*;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.exception.WrongAuthenticationException;
import com.softserve.teachua.model.Role;
import com.softserve.teachua.model.User;
import com.softserve.teachua.repository.UserRepository;
import com.softserve.teachua.security.service.EncoderService;
import com.softserve.teachua.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EncoderService encodeService;

    @Mock
    private RoleService roleService;

    @Mock
    private DtoConverter dtoConverter;

    @Mock
    private ArchiveService archiveService;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserProfile userProfile;

    private final long EXISTING_ID = 3L;
    private final long NOT_EXISTING_ID = 500L;

    private final String EXISTING_EMAIL = "someuser@mail.com";
    private final String NOT_EXISTING_EMAIL = "notexisting@mail.com";
    private final String NEW_EMAIL = "newuser@mail.com";
    private final String PASSWORD = "12345";


    @BeforeEach
    public void init() {
        user = User.builder().id(EXISTING_ID).email(EXISTING_EMAIL).build();
        userProfile = UserProfile.builder().email(NEW_EMAIL).password(PASSWORD).name("username").build();
    }

    @Test
    public void getUserByIdTest() {
        when(userRepository.findById(EXISTING_ID)).thenReturn(Optional.of(user));

        User actual = userService.getUserById(EXISTING_ID);
        assertEquals(actual, user);
    }

    @Test
    public void getUserByNotExistingIdTest() {
        when(userRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            userService.getUserById(NOT_EXISTING_ID);
        }).isInstanceOf(NotExistException.class);
    }

    @Test
    public void getUserByIdEmailTest() {
        when(userRepository.findByEmail(EXISTING_EMAIL)).thenReturn(Optional.of(user));

        User actual = userService.getUserByEmail(EXISTING_EMAIL);
        assertEquals(actual, user);
    }

    @Test
    public void getUserByNotExistingEmailTest() {
        when(userRepository.findByEmail(NOT_EXISTING_EMAIL)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            userService.getUserByEmail(NOT_EXISTING_EMAIL);
        }).isInstanceOf(NotExistException.class);
    }

    @Test
    public void registerNewUserTest() {
        User newUser = User.builder().email(NEW_EMAIL).build();

        when(userRepository.existsByEmail(NEW_EMAIL)).thenReturn(false);
        when(dtoConverter.convertToEntity(userProfile, new User())).thenReturn(newUser);
        when(encodeService.encodePassword(PASSWORD)).thenReturn("encoded password");
        when(userRepository.save(any())).thenReturn(newUser);
        String ROLE_NAME = "ROLE_USER";
        when(roleService.findByName(ROLE_NAME)).thenReturn(Role.builder().id(2).name(ROLE_NAME).build());
        when(dtoConverter.convertToDto(newUser, SuccessRegistration.class))
                .thenReturn(SuccessRegistration.builder().email(NEW_EMAIL).build());

        SuccessRegistration actual = userService.registerUser(userProfile);
        assertEquals(actual.getEmail(), userProfile.getEmail());
    }

    @Test
    public void registerExistingUserTest() {
        userProfile.setEmail(EXISTING_EMAIL);
        when(userRepository.existsByEmail(EXISTING_EMAIL)).thenReturn(true);

        assertThatThrownBy(() -> {
            userService.registerUser(userProfile);
        }).isInstanceOf(WrongAuthenticationException.class);
    }

    @Test
    public void validateUserWithValidPasswordTest() {
        UserLogin userLogin = new UserLogin(NEW_EMAIL, PASSWORD);
        User newUser = User.builder().email(NEW_EMAIL).password(PASSWORD).build();
        when(userRepository.findByEmail(NEW_EMAIL)).thenReturn(Optional.of(newUser));
        when(dtoConverter.convertToDto(newUser, UserEntity.class))
                .thenReturn(UserEntity.builder().email(NEW_EMAIL).password(PASSWORD).build());
        UserEntity userEntity = userService.getUserEntity(NEW_EMAIL);

        when(encodeService.isValidPassword(userLogin, userEntity)).thenReturn(true);
        when(dtoConverter.convertFromDtoToDto(userEntity, new SuccessLogin()))
                .thenReturn(SuccessLogin.builder().email(NEW_EMAIL).build());
        when(encodeService.createToken(userEntity.getEmail())).thenReturn("token");

        SuccessLogin actual = userService.validateUser(userLogin);
        assertEquals(actual.getEmail(), userLogin.getEmail());
    }

    @Test
    public void validateUserWithInvalidPasswordTest() {
        String invalidPassword = "invalid password";
        UserLogin userLogin = new UserLogin(NEW_EMAIL, invalidPassword);
        User newUser = User.builder().email(NEW_EMAIL).password(invalidPassword).build();
        when(userRepository.findByEmail(NEW_EMAIL)).thenReturn(Optional.of(newUser));
        when(dtoConverter.convertToDto(newUser, UserEntity.class))
                .thenReturn(UserEntity.builder().email(NEW_EMAIL).password(invalidPassword).build());

        UserEntity userEntity = userService.getUserEntity(NEW_EMAIL);
        when(encodeService.isValidPassword(userLogin, userEntity)).thenReturn(false);

        assertThatThrownBy(() -> {
            userService.validateUser(userLogin);
        }).isInstanceOf(WrongAuthenticationException.class);
    }

    @Test
    public void updateUserTest() {
        when(userRepository.findById(EXISTING_ID)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);
        when(dtoConverter.convertToEntity(userProfile, user)).thenReturn(User.builder()
                .email(userProfile.getEmail()).name(userProfile.getName()).build());
        when(dtoConverter.convertToDto(user, SuccessUpdatedUser.class)).thenReturn(SuccessUpdatedUser
                .builder().email(userProfile.getEmail()).build());

        SuccessUpdatedUser updatedUser = userService.updateUser(EXISTING_ID, userProfile);
        assertEquals(updatedUser.getEmail(), userProfile.getEmail());
    }

    @Test
    public void updateUserWithWrongIdTest() {
        when(userRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            userService.updateUser(NOT_EXISTING_ID, userProfile);
        }).isInstanceOf(NotExistException.class);
    }

    @Test
    public void deleteUserByIdTest() {
        when(userRepository.findById(EXISTING_ID)).thenReturn(Optional.of(user));
        when(archiveService.saveModel(user)).thenReturn(user);
        doNothing().when(userRepository).deleteById(EXISTING_ID);
        doNothing().when(userRepository).flush();
        when(dtoConverter.convertToDto(user, UserResponse.class)).thenReturn(UserResponse.builder()
                .id(user.getId()).email(user.getEmail()).name(user.getName()).build());

        UserResponse userResponse = userService.deleteUserById(EXISTING_ID);
        assertEquals(userResponse.getEmail(), user.getEmail());
    }

    @Test
    public void deleteNotExistingUserTest() {
        when(userRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            userService.deleteUserById(NOT_EXISTING_ID);
        }).isInstanceOf(NotExistException.class);
    }

}
