package com.softserve.teachua.service;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.security.UserEntity;
import com.softserve.teachua.dto.user.*;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.exception.WrongAuthenticationException;
import com.softserve.teachua.model.Archive;
import com.softserve.teachua.model.Role;
import com.softserve.teachua.model.User;
import com.softserve.teachua.model.archivable.RoleArch;
import com.softserve.teachua.model.archivable.UserArch;
import com.softserve.teachua.repository.UserRepository;
import com.softserve.teachua.security.JwtProvider;
import com.softserve.teachua.security.service.EncoderService;
import com.softserve.teachua.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class UserServiceTest {

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

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private UserServiceImpl userService;

    private UserUpdateProfile userUpdateProfile;
    private User user;
    private User updUser;
    private UserProfile userProfile;
    private Role role;
    private UserArch userArch;

    private final long EXISTING_ID = 3L;
    private final long NOT_EXISTING_ID = 500L;
    private final boolean IS_STATUS = true;

    private final Integer ROLE_ID = 72;
    private final String ROLE_NAME = "ADMIN";
    private final String EXISTING_EMAIL = "someuser@mail.com";
    private final String NOT_EXISTING_EMAIL = "notexisting@mail.com";
    private final String NEW_EMAIL = "newuser@mail.com";
    private final String PASSWORD = "12345";
    private final String TOKEN = "osfljlksdflkfjlsdfkldsfsdf";


    @BeforeEach
     void init() {
        role = Role.builder()
                .id(ROLE_ID)
                .name(ROLE_NAME)
                .build();
        user = User.builder()
                .id(EXISTING_ID)
                .email(EXISTING_EMAIL)
                .status(IS_STATUS)
                .password(PASSWORD)
                .role(role)
                .build();
        userProfile = UserProfile.builder()
                .email(NEW_EMAIL)
                .password(PASSWORD)
                .roleName(ROLE_NAME)
                .firstName("username").build();
        updUser = User.builder()
                .id(EXISTING_ID)
                .email(EXISTING_EMAIL)
                .password(PASSWORD)
                .firstName("username").build();
        userArch = UserArch.builder()
                .email(EXISTING_EMAIL)
                .password(PASSWORD)
                .firstName("username").build();
        userUpdateProfile = UserUpdateProfile.builder()
                .email(EXISTING_EMAIL)
                .roleName(ROLE_NAME)
                .build();
    }

    @Test
     void getUserByIdTest() {
        when(userRepository.findById(EXISTING_ID)).thenReturn(Optional.of(user));

        User actual = userService.getUserById(EXISTING_ID);
        assertEquals(actual, user);
    }

    @Test
     void getUserByNotExistingIdTest() {
        when(userRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(NOT_EXISTING_ID)).isInstanceOf(NotExistException.class);
    }

    @Test
     void getUserByIdEmailTest() {
        when(userRepository.findByEmail(EXISTING_EMAIL)).thenReturn(Optional.of(user));

        User actual = userService.getUserByEmail(EXISTING_EMAIL);
        assertEquals(actual, user);
    }

    @Test
     void getUserByNotExistingEmailTest() {
        when(userRepository.findByEmail(NOT_EXISTING_EMAIL)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserByEmail(NOT_EXISTING_EMAIL)).isInstanceOf(NotExistException.class);
    }

//    @Test
//     void registerNewUserTest() {
//        User newUser = User.builder().email(NEW_EMAIL).build();
//
//        when(userRepository.existsByEmail(NEW_EMAIL)).thenReturn(false);
//        when(dtoConverter.convertToEntity(userProfile, new User())).thenReturn(newUser);
//        when(encodeService.encodePassword(PASSWORD)).thenReturn("encoded password");
//        when(userRepository.save(any())).thenReturn(newUser);
//        when(roleService.findByName(ROLE_NAME)).thenReturn(role);
//        when(dtoConverter.convertToDto(newUser, SuccessRegistration.class))
//                .thenReturn(SuccessRegistration.builder().email(NEW_EMAIL).build());
//
//        SuccessRegistration actual = userService.registerUser(userProfile);
//        assertEquals(actual.getEmail(), userProfile.getEmail());
//    }

    @Test
     void registerExistingUserTest() {
        userProfile.setEmail(EXISTING_EMAIL);
        when(userRepository.existsByEmail(EXISTING_EMAIL)).thenReturn(true);

        assertThatThrownBy(() -> {
            userService.registerUser(userProfile);
        }).isInstanceOf(WrongAuthenticationException.class);
    }

    @Test
     void validateUserWithValidPasswordTest() {
        UserLogin userLogin = new UserLogin(NEW_EMAIL, PASSWORD);
        User newUser = User.builder().email(NEW_EMAIL).password(PASSWORD).status(IS_STATUS).build();
        when(userRepository.findByEmail(NEW_EMAIL)).thenReturn(Optional.of(newUser));
        when(dtoConverter.convertToDto(newUser, UserEntity.class))
                .thenReturn(UserEntity.builder().email(NEW_EMAIL).password(PASSWORD).build());
        UserEntity userEntity = userService.getUserEntity(NEW_EMAIL);

        when(encodeService.isValidPassword(userLogin, userEntity)).thenReturn(true);
        when(dtoConverter.convertFromDtoToDto(userEntity, new SuccessLogin()))
                .thenReturn(SuccessLogin.builder().email(NEW_EMAIL).build());
        when(encodeService.isValidStatus(userEntity)).thenReturn(true);

        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    userLogin.getEmail(),
                    userLogin.getPassword()
                ))).thenReturn(authentication);

        when(jwtProvider.generateToken(authentication)).thenReturn(TOKEN);

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
        when(encodeService.isValidStatus(userEntity)).thenReturn(true);
        when(encodeService.isValidPassword(userLogin, userEntity)).thenReturn(false);

        assertThatThrownBy(() -> {
            userService.validateUser(userLogin);
        }).isInstanceOf(WrongAuthenticationException.class);
    }

   @Test
    public void updateUserTest() {
        when(userRepository.findById(EXISTING_ID)).thenReturn(Optional.of(user));
        when(dtoConverter.convertToEntity(userUpdateProfile, user))
                .thenReturn(updUser);
        when(userRepository.save(any())).thenReturn(user);
        when(dtoConverter.convertToDto(user, SuccessUpdatedUser.class))
                .thenReturn(SuccessUpdatedUser.builder().email(EXISTING_EMAIL).build());

        SuccessUpdatedUser updatedUser = userService.updateUser(EXISTING_ID, userUpdateProfile);
        assertEquals(updatedUser.getEmail(), userUpdateProfile.getEmail());
    }

    @Test
    public void updateUserWithWrongIdTest() {
        when(userRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            userService.updateUser(NOT_EXISTING_ID, userUpdateProfile);
        }).isInstanceOf(NotExistException.class);
    }

    @Test
    public void deleteUserByIdTest() {
        when(userRepository.findById(EXISTING_ID)).thenReturn(Optional.of(user));
//        when(archiveService.saveModel(user)).thenReturn(user);
        doNothing().when(userRepository).deleteById(EXISTING_ID);
        doNothing().when(userRepository).flush();
        when(dtoConverter.convertToDto(user, UserResponse.class)).thenReturn(UserResponse.builder()
                .id(user.getId()).email(user.getEmail()).firstName(user.getFirstName()).build());
        when(dtoConverter.convertToDto(user, UserArch.class)).thenReturn(userArch);
        when(archiveService.saveModel(userArch)).thenReturn(Archive.builder().build());
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
