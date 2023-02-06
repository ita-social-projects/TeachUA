package com.softserve.teachua.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.user.SuccessLogin;
import com.softserve.teachua.dto.user.SuccessUpdatedUser;
import com.softserve.teachua.dto.user.SuccessUserPasswordReset;
import com.softserve.teachua.dto.user.SuccessVerification;
import com.softserve.teachua.dto.user.UserLogin;
import com.softserve.teachua.dto.user.UserPasswordUpdate;
import com.softserve.teachua.dto.user.UserProfile;
import com.softserve.teachua.dto.user.UserResponse;
import com.softserve.teachua.dto.user.UserUpdateProfile;
import com.softserve.teachua.dto.user.UserVerifyPassword;
import com.softserve.teachua.exception.IncorrectInputException;
import com.softserve.teachua.exception.MatchingPasswordException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.exception.NotVerifiedUserException;
import com.softserve.teachua.exception.UserAuthenticationException;
import com.softserve.teachua.exception.UpdatePasswordException;
import com.softserve.teachua.exception.UserPermissionException;
import com.softserve.teachua.model.Archive;
import com.softserve.teachua.model.Role;
import com.softserve.teachua.model.User;
import com.softserve.teachua.model.archivable.UserArch;
import com.softserve.teachua.repository.UserRepository;
import com.softserve.teachua.security.CustomUserDetailsService;
import com.softserve.teachua.security.JwtProvider;
import com.softserve.teachua.security.UserPrincipal;
import com.softserve.teachua.security.service.EncoderService;
import com.softserve.teachua.service.impl.UserServiceImpl;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static com.softserve.teachua.TestUtils.getUserPrincipal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private static final long EXISTING_ID = 3L;
    private static final long NOT_EXISTING_ID = 500L;
    private static final boolean IS_STATUS = true;
    private static final Integer ROLE_ID = 72;
    private static final String ROLE_NAME = "ROLE_ADMIN";
    private static final String MISSPELLING_ROLE_NAME = "ROLE_ADMIN";
    private static final String EXISTING_EMAIL = "someuser@mail.com";
    private static final String NEW_EMAIL = "newuser@mail.com";
    private static final String PASSWORD = "12345";
    private static final String ENCODED_PASSWORD = "$2a$10$ZEOgnNbaAhk3DvMB7k//d.1t92FEcdSc74mDWwduIaAxjFAAsRoku";
    private static final String TOKEN = "osfljlksdflkfjlsdfkldsfsdf";
    private static final String USERS_NOT_FOUND_BY_ROLE_NAME = "User not found by role name - %s";
    private static final String USER_NOT_FOUND_BY_EMAIL = "User not found by email %s";
    private static final String FIRST_NAME = "username";
    @Mock
    private UserRepository userRepository;
    @Mock
    private EncoderService encodeService;
    @Mock
    private DtoConverter dtoConverter;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private ArchiveService archiveService;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private JwtProvider jwtProvider;
    @Mock
    private RefreshTokenService refreshTokenService;
    @Mock
    private CustomUserDetailsService userDetailsService;
    @Mock
    private RoleService roleService;
    @InjectMocks
    private UserServiceImpl userService;
    private UserUpdateProfile userUpdateProfile;
    private User user;
    private User updUser;
    private UserProfile userProfile;
    private Role role;
    private UserArch userArch;
    private List<User> userList;
    private UserPasswordUpdate passwordUpdate;

    @BeforeEach
    void init() {
        role = Role.builder().id(ROLE_ID).name(ROLE_NAME).build();
        user = User.builder().id(EXISTING_ID).email(EXISTING_EMAIL).status(IS_STATUS).password(PASSWORD).role(role)
                .build();
        userProfile =
                UserProfile.builder().email(NEW_EMAIL).password(PASSWORD).roleName(ROLE_NAME).firstName(FIRST_NAME)
                        .build();
        updUser = User.builder().id(EXISTING_ID).email(EXISTING_EMAIL).password(PASSWORD).firstName(FIRST_NAME).build();
        userArch = UserArch.builder().email(EXISTING_EMAIL).password(PASSWORD)
                .firstName(FIRST_NAME).roleId(ROLE_ID).build();
        userUpdateProfile = UserUpdateProfile.builder().email(EXISTING_EMAIL).roleName(ROLE_NAME).build();
        userList = Arrays.asList(
                User.builder().id(1L).email("kbeech0@networkadvertising.org").firstName("Kitty").lastName("Beech")
                        .build(),
                User.builder().id(2L).email("wkuhnt2@state.tx.us").firstName("Will").lastName("Kuhnt").build(),
                User.builder().id(3L).email("kwilkenson4@skype.com").firstName("Kendall").lastName("Wilkenson")
                        .build());
        passwordUpdate =
                UserPasswordUpdate.builder().oldPassword("password").newPassword(PASSWORD).newPasswordVerify(PASSWORD)
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
    void givenUserId_whenGetUserProfileById_thenReturnUserEntity() {
        when(userRepository.findById(EXISTING_ID)).thenReturn(Optional.of(user));
        when(dtoConverter.convertToDto(user, UserResponse.class)).thenReturn(
                UserResponse.builder().id(EXISTING_ID).email(EXISTING_EMAIL).firstName(FIRST_NAME).build());

        UserResponse actual = userService.getUserProfileById(EXISTING_ID);
        assertThat(actual).isNotNull().hasFieldOrPropertyWithValue("Id", user.getId());
    }

    @Test
    void givenUserRoleName_whenGetUserResponsesByRole_thenListUserResponse() {
        when(userRepository.findByRoleName(anyString())).thenReturn(Optional.of(userList));

        List<UserResponse> actual = userService.getUserResponsesByRole(anyString());
        assertThat(actual).isNotNull().hasSize(userList.size());
    }

    @Test
    void givenNonexistentRole_whenGetUserResponsesByRole_thenThrowNotExistException() {
        when(userRepository.findByRoleName(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserResponsesByRole(ROLE_NAME)).isInstanceOf(NotExistException.class)
                .hasMessage(String.format(USERS_NOT_FOUND_BY_ROLE_NAME, ROLE_NAME));
    }

    @Test
    void givenVerificationCode_whenGetUserByVerificationCode_thenReturnUser() {
        when(userRepository.findByVerificationCode(anyString())).thenReturn(Optional.of(user));

        User actual = userService.getUserByVerificationCode(anyString());
        assertThat(actual).isNotNull().isEqualTo(user);
    }

    @Test
    void givenVerificationCode_whenUserByVerificationCode_thenThrowNotExistException() {
        when(userRepository.findByVerificationCode(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserByVerificationCode(TOKEN)).isInstanceOf(NotExistException.class)
                .hasMessage("User not found or invalid link");
    }

    @Test
    void givenEmail_whenGetUserByEmail_thenThrowNotExistException() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserByEmail(EXISTING_EMAIL)).isInstanceOf(NotExistException.class)
                .hasMessage(String.format(USER_NOT_FOUND_BY_EMAIL, EXISTING_EMAIL));
    }

    @Test
    void whenGetListOfUsers_thenReturnListUserResponse() {
        when(userRepository.findAll()).thenReturn(userList);

        List<UserResponse> actual = userService.getListOfUsers();
        assertThat(actual).isNotNull().hasSize(userList.size());
    }

    @Test
    void getUserByNotExistingEmailTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(User.builder().email("1@1.com").build()));
        UserUpdateProfile updateProfile = UserUpdateProfile.builder().email("2@2.com").build();

        assertThatThrownBy(() -> userService.updateUser(EXISTING_ID, updateProfile))
                .isInstanceOf(IncorrectInputException.class)
                .hasMessage("Email can`t be updated");
    }

    @Test
    void givenUserVerifyPasswordWithoutPassword_whenValidateUser_thenReturnUserVerifyPassword() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        UserVerifyPassword actual = userService.validateUser(new UserVerifyPassword(EXISTING_ID, null));
        assertThat(actual).isNotNull().extracting(UserVerifyPassword::getPassword).isEqualTo(user.getPassword());
    }

    @Test
    void givenUserVerifyPasswordAndNonExistedUserId_whenValidateUser_thenThrowNotVerifiedUserException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        UserVerifyPassword verifyPassword = new UserVerifyPassword(NOT_EXISTING_ID, null);

        assertThatThrownBy(() -> userService.validateUser(verifyPassword))
                .isInstanceOf(NotVerifiedUserException.class).hasMessage("User is not verified");
    }

    @Test
    void givenUserVerifyPasswordWithRightPassword_whenValidateUser_thenReturnUserVerifyPassword() {
        when(userRepository.findById(anyLong())).thenReturn(
                Optional.of(User.builder().id(EXISTING_ID).password(ENCODED_PASSWORD).build()));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        UserVerifyPassword actual = userService.validateUser(new UserVerifyPassword(EXISTING_ID, PASSWORD));
        assertThat(actual).isNotNull().extracting(UserVerifyPassword::getPassword).isEqualTo(PASSWORD);
    }

    @Test
    void givenUserVerifyPasswordWithWrongPassword_whenValidateUser_thenThrowNotVerifiedUserException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
        UserVerifyPassword userVerifyPassword = new UserVerifyPassword(EXISTING_ID, PASSWORD);

        assertThatThrownBy(() -> userService.validateUser(userVerifyPassword)).isInstanceOf(
                        NotVerifiedUserException.class);
    }

    @Test
    void registerExistingUserTest() {
        when(userRepository.existsByEmail(EXISTING_EMAIL)).thenReturn(true);
        userProfile.setEmail(EXISTING_EMAIL);

        assertThatThrownBy(() -> userService.registerUser(userProfile)).isInstanceOf(UserAuthenticationException.class)
                .hasMessage(String.format("Email %s already exist", EXISTING_EMAIL));
    }

    @Test
    void givenUserProfileWithWrongRole_thenRegisterUser_thenThrowIncorrectInputException() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        userProfile.setRoleName(MISSPELLING_ROLE_NAME);

        assertThatThrownBy(() -> userService.registerUser(userProfile)).isInstanceOf(IncorrectInputException.class)
                .hasMessage(String.format("Illegal role argument: %s", MISSPELLING_ROLE_NAME));
    }

    @Test
    void loginUserWithValidPasswordTest() {
        UserLogin userLogin = new UserLogin(NEW_EMAIL, PASSWORD);
        User newUser = User.builder().email(NEW_EMAIL).password(PASSWORD)
                .role(role).status(IS_STATUS).build();
        when(userRepository.findByEmail(NEW_EMAIL)).thenReturn(Optional.of(newUser));

        when(encodeService.isValidPassword(userLogin, newUser)).thenReturn(true);
        when(jwtProvider.generateAccessToken(newUser.getEmail())).thenReturn(TOKEN);
        when(refreshTokenService.assignRefreshToken(newUser)).thenReturn(TOKEN);

        SuccessLogin actual = userService.loginUser(userLogin);
        assertEquals(actual.getEmail(), userLogin.getEmail());
    }

    @Test
    void loginUserWithInvalidPasswordTest() {
        String invalidPassword = "invalid password";
        UserLogin userLogin = new UserLogin(NEW_EMAIL, invalidPassword);
        User newUser = User.builder().email(NEW_EMAIL).password(invalidPassword).status(IS_STATUS).build();
        when(userRepository.findByEmail(NEW_EMAIL)).thenReturn(Optional.of(newUser));

        when(encodeService.isValidPassword(userLogin, newUser)).thenReturn(false);

        assertThatThrownBy(() -> userService.loginUser(userLogin)).isInstanceOf(UserAuthenticationException.class);
    }

    @Test
    void loginUserWithStatusFalse() {
        UserLogin userLogin = new UserLogin(NEW_EMAIL, PASSWORD);
        User newUser = User.builder().email(NEW_EMAIL).password(PASSWORD)
                .role(role).status(false).build();
        when(userRepository.findByEmail(NEW_EMAIL)).thenReturn(Optional.of(newUser));

        assertThatThrownBy(() -> userService.loginUser(userLogin)).isInstanceOf(NotVerifiedUserException.class);
    }

    @Test
    void updateUserTest() {
        when(userRepository.findById(EXISTING_ID)).thenReturn(Optional.of(user));
        when(dtoConverter.convertToEntity(userUpdateProfile, user)).thenReturn(updUser);
        when(userRepository.save(any())).thenReturn(user);
        when(dtoConverter.convertToDto(user, SuccessUpdatedUser.class)).thenReturn(
                SuccessUpdatedUser.builder().email(EXISTING_EMAIL).build());

        SuccessUpdatedUser updatedUser = userService.updateUser(EXISTING_ID, userUpdateProfile);
        assertEquals(updatedUser.getEmail(), userUpdateProfile.getEmail());
    }

    @Test
    void updateUserWithWrongIdTest() {
        when(userRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUser(NOT_EXISTING_ID, userUpdateProfile))
                .isInstanceOf(NotExistException.class);
    }

    @Test
    void updateUserWithWrongEmail() {
        when(userRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUser(NOT_EXISTING_ID, userUpdateProfile))
                .isInstanceOf(NotExistException.class);
    }

    @Test
    void deleteUserByIdTest() {
        when(userRepository.findById(EXISTING_ID)).thenReturn(Optional.of(user));
        // when(archiveService.saveModel(user)).thenReturn(user);
        doNothing().when(userRepository).deleteById(EXISTING_ID);
        doNothing().when(userRepository).flush();
        when(dtoConverter.convertToDto(user, UserResponse.class)).thenReturn(
                UserResponse.builder().id(user.getId()).email(user.getEmail()).firstName(user.getFirstName()).build());
        when(dtoConverter.convertToDto(user, UserArch.class)).thenReturn(userArch);
        when(archiveService.saveModel(userArch)).thenReturn(Archive.builder().build());
        UserResponse userResponse = userService.deleteUserById(EXISTING_ID);
        assertEquals(userResponse.getEmail(), user.getEmail());
    }

    @Test
    void deleteNotExistingUserTest() {
        when(userRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.deleteUserById(NOT_EXISTING_ID))
                .isInstanceOf(NotExistException.class);
    }

    @Test
    void givenUser_whenUpdateUser_thenReturnUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.updateUser(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void givenArchiveObject_whenRestoreModel_thenNothing() throws JsonProcessingException {
        when(objectMapper.readValue(TOKEN, UserArch.class)).thenReturn(userArch);
        when(dtoConverter.convertToEntity(userArch, User.builder().build())).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(roleService.getRoleById(anyInt())).thenReturn(role);

        userService.restoreModel(TOKEN);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void givenVerificationCode_whenVerifyChange_thenSuccessVerification() {
        when(userRepository.findByVerificationCode(anyString())).thenReturn(Optional.of(user));
        when(dtoConverter.convertToDto(user, SuccessVerification.class)).thenReturn(
                SuccessVerification.builder().id(EXISTING_ID).build());

        SuccessVerification actual = userService.verifyChange(TOKEN);
        assertThat(actual).isNotNull().extracting(SuccessVerification::getMessage)
                .isEqualTo(String.format("Користувач %s %s верифікований", user.getFirstName(), user.getLastName()));
    }

    @Test
    void givenVerificationCode_whenVerify_thenReturnSuccessVerification() {
        when(userRepository.findByVerificationCode(anyString())).thenReturn(Optional.of(user));
        when(dtoConverter.convertToDto(user, SuccessVerification.class)).thenReturn(
                SuccessVerification.builder().build());

        SuccessVerification actual = userService.verify(TOKEN);
        assertThat(actual).extracting(SuccessVerification::getMessage).isEqualTo(
                String.format("Користувач %s %s успішно зареєстрований", user.getFirstName(), user.getLastName()));
    }

    @Test
    void givenUserPrincipal_whenGetAuthenticatedUser_thenReturnUserEntity() {
        UserPrincipal userPrincipal = getUserPrincipal(user);
        when(userDetailsService.getUserPrincipal()).thenReturn(userPrincipal);
        when(userRepository.findById(userPrincipal.getId())).thenReturn(Optional.of(user));

        User actual = userService.getAuthenticatedUser();
        assertEquals(user, actual);
    }

    @Test
    void givenNotExistingUserPrincipal_whenGetAuthenticatedUser_thenThrowNotExistException() {
        UserPrincipal userPrincipal = getUserPrincipal(user);
        when(userDetailsService.getUserPrincipal()).thenReturn(userPrincipal);
        when(userRepository.findById(userPrincipal.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getAuthenticatedUser())
                .isInstanceOf(NotExistException.class);
    }

    @Test
    void givenAdminUserPrincipal_whenVerifyIsUserAdmin_thenDoNotThrow() {
        UserPrincipal userPrincipal = getUserPrincipal(user);
        when(userDetailsService.getUserPrincipal()).thenReturn(userPrincipal);

        assertThatNoException().isThrownBy(() -> userService.verifyIsUserAdmin());
    }

    @Test
    void givenUserUserPrincipal_whenVerifyIsUserAdmin_thenThrow() {
        UserPrincipal userPrincipal = getUserPrincipal(user.withRole(role.withName("ROLE_USER")));
        when(userDetailsService.getUserPrincipal()).thenReturn(userPrincipal);

        assertThatThrownBy(() -> userService.verifyIsUserAdmin())
                .isInstanceOf(UserPermissionException.class);
    }

    @Test
    void givenUserIdAndUserPasswordUpdateWithWrongOldPassword_whenUpdatePassword_thenThrowUpdatePasswordException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThatThrownBy(() -> userService.updatePassword(EXISTING_ID, passwordUpdate)).isInstanceOf(
                UpdatePasswordException.class).hasMessage("Wrong old password");
    }

    @Test
    void givenUserIdAndUserPasswordUpdateWithWrongVerify_whenUpdatePassword_thenThrowUpdatePasswordException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        passwordUpdate.setNewPasswordVerify("54321");

        assertThatThrownBy(() -> userService.updatePassword(EXISTING_ID, passwordUpdate)).isInstanceOf(
                UpdatePasswordException.class).hasMessage("Verify password doesnt match to new");
    }

    @Test
    void givenUserIdAndUserPasswordUpdateWithSamePassword_whenUpdatePassword_thenThrowUpdatePasswordException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        assertThatThrownBy(() -> userService.updatePassword(EXISTING_ID, passwordUpdate)).isInstanceOf(
                UpdatePasswordException.class).hasMessage("New password equals to old");
    }

    @Test
    void givenUserIdAndUserPasswordUpdate_whenUpdatePassword_thenOkay() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(passwordUpdate.getOldPassword(), user.getPassword())).thenReturn(true);
        when(passwordEncoder.matches(passwordUpdate.getNewPassword(), user.getPassword())).thenReturn(false);

        userService.updatePassword(EXISTING_ID, passwordUpdate);
        verify(userRepository).save(user);
    }

    @Test
    void givenSuccessUserPasswordReset_whenVerifyChangePassword_thenThrowMatchingPasswordException() {
        SuccessUserPasswordReset userResetPassword =
                SuccessUserPasswordReset.builder().password(PASSWORD).verificationCode(TOKEN).build();
        user.setPassword(ENCODED_PASSWORD);
        when(userRepository.findByVerificationCode(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(userResetPassword.getPassword(), user.getPassword()))
                .thenReturn(true);

        assertThatThrownBy(() -> userService.verifyChangePassword(userResetPassword)).isInstanceOf(
                MatchingPasswordException.class).hasMessage("Новий пароль співпадає з старим");
    }

    @Test
    void givenSuccessUserPasswordReset_whenVerifyChangePassword_thenUserResetPassword() {
        user.setPassword(ENCODED_PASSWORD);
        when(userRepository.findByVerificationCode(anyString())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        SuccessUserPasswordReset userResetPassword =
                SuccessUserPasswordReset.builder().password("Wingardium Leviosa").verificationCode(TOKEN).build();

        SuccessUserPasswordReset actual = userService.verifyChangePassword(userResetPassword);
        assertThat(actual).isNotNull().extracting(SuccessUserPasswordReset::getPassword)
                .isEqualTo("Wingardium Leviosa");
        verify(userRepository).save(any(User.class));
    }
}
