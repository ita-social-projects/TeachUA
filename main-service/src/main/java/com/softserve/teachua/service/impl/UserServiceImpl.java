package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.user.SuccessLogin;
import com.softserve.teachua.dto.user.SuccessRegistration;
import com.softserve.teachua.dto.user.SuccessUpdatedUser;
import com.softserve.teachua.dto.user.SuccessUserPasswordReset;
import com.softserve.teachua.dto.user.SuccessVerification;
import com.softserve.teachua.dto.user.UserLogin;
import com.softserve.teachua.dto.user.UserPasswordUpdate;
import com.softserve.teachua.dto.user.UserProfile;
import com.softserve.teachua.dto.user.UserResetPassword;
import com.softserve.teachua.dto.user.UserResponse;
import com.softserve.teachua.dto.user.UserUpdateProfile;
import com.softserve.teachua.dto.user.UserVerifyPassword;
import com.softserve.teachua.exception.DatabaseRepositoryException;
import com.softserve.teachua.exception.IncorrectInputException;
import com.softserve.teachua.exception.MatchingPasswordException;
import com.softserve.clients.exception.NotExistException;
import com.softserve.teachua.exception.NotVerifiedUserException;
import com.softserve.teachua.exception.UpdatePasswordException;
import com.softserve.teachua.exception.UserAuthenticationException;
import com.softserve.teachua.exception.UserPermissionException;
import com.softserve.teachua.model.AuthProvider;
import com.softserve.teachua.model.User;
import com.softserve.teachua.model.archivable.UserArch;
import com.softserve.teachua.repository.UserRepository;
import com.softserve.teachua.security.CustomUserDetailsService;
import com.softserve.teachua.security.JwtUtils;
import com.softserve.teachua.security.UserPrincipal;
import com.softserve.teachua.service.ArchiveMark;
import com.softserve.teachua.service.ArchiveService;
import com.softserve.teachua.service.RefreshTokenService;
import com.softserve.teachua.service.RoleService;
import com.softserve.teachua.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.ValidationException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, ArchiveMark<User> {
    private static final String EMAIL_ALREADY_EXIST = "Email %s already exist";
    private static final String EMAIL_UPDATING_ERROR = "Email can`t be updated";
    private static final String USER_NOT_FOUND_BY_ID = "User not found by id %s";
    private static final String USER_NOT_FOUND_BY_EMAIL = "User not found by email %s";
    private static final String USER_NOT_FOUND_BY_VERIFICATION_CODE = "User not found or invalid link";
    private static final String USERS_NOT_FOUND_BY_ROLE_NAME = "User not found by role name - %s";
    private static final String WRONG_PASSWORD = "Wrong password";
    private static final String NOT_VERIFIED = "Ви не підтвердили електронну пошту: %s";
    private static final String USER_DELETING_ERROR = "Can't delete user cause of relationship";
    private static final String USER_REGISTRATION_ERROR = "Can't register user";
    private static final String ONLY_ADMIN_CONTENT = "Only the admin have permit to view this content";
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final DtoConverter dtoConverter;
    private final ArchiveService archiveService;
    private final JwtUtils jwtUtils;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;
    private final CustomUserDetailsService userDetailsService;
    private final RefreshTokenService refreshTokenService;
    @Value("${baseURL}")
    private String baseUrl;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService, DtoConverter dtoConverter,
                           ArchiveService archiveService, JwtUtils jwtUtils,
                           JavaMailSender javaMailSender, @Lazy PasswordEncoder passwordEncoder,
                           ObjectMapper objectMapper, RefreshTokenService refreshTokenService,
                           CustomUserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.dtoConverter = dtoConverter;
        this.archiveService = archiveService;
        this.jwtUtils = jwtUtils;
        this.javaMailSender = javaMailSender;
        this.passwordEncoder = passwordEncoder;
        this.objectMapper = objectMapper;
        this.userDetailsService = userDetailsService;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public UserResponse getUserProfileById(Long id) {
        User user = getUserById(id);
        return dtoConverter.convertToDto(user, UserResponse.class);
    }

    @Override
    public User getUserById(Long id) {
        Optional<User> optionalUser = getOptionalUserById(id);
        if (optionalUser.isEmpty()) {
            throw new NotExistException(String.format(USER_NOT_FOUND_BY_ID, id));
        }

        User user = optionalUser.get();
        log.debug("getting user by id {}", user);
        return user;
    }

    @Override
    public List<UserResponse> getUserResponsesByRole(String roleName) {
        List<User> users = userRepository.findByRoleName(roleName).orElseThrow(() -> {
            log.error("users not found by role name - {}", roleName);
            return new NotExistException(String.format(USERS_NOT_FOUND_BY_ROLE_NAME, roleName));
        });
        List<UserResponse> userResponses = users.stream()
                .map(user -> (UserResponse) dtoConverter.convertToDto(user, UserResponse.class))
                .toList();
        log.debug("getting users by role name - {}", roleName);
        return userResponses;
    }

    @Override
    public User getUserByEmail(String email) {
        Optional<User> optionalUser = getOptionalUserByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new NotExistException(String.format(USER_NOT_FOUND_BY_EMAIL, email));
        }

        log.debug("getting user by email {}", userRepository.findByEmail(email));
        return optionalUser.get();
    }

    @Override
    public User getUserByVerificationCode(String verificationCode) {
        Optional<User> optionalUser = getOptionalUserByVerificationCode(verificationCode);
        if (optionalUser.isEmpty()) {
            throw new NotExistException(USER_NOT_FOUND_BY_VERIFICATION_CODE);
        }

        log.debug("getting user by verificationCode {}", userRepository.findByVerificationCode(verificationCode));
        return optionalUser.get();
    }

    @Override
    public List<UserResponse> getListOfUsers() {
        List<UserResponse> userResponses = userRepository.findAll().stream()
                .map(user -> (UserResponse) dtoConverter.convertToDto(user, UserResponse.class))
                .toList();

        log.debug("getting list of users {}", userResponses);
        return userResponses;
    }

    @Override
    public SuccessRegistration registerUser(UserProfile userProfile) {
        userProfile.setEmail(userProfile.getEmail().toLowerCase());
        if (isUserExistByEmail(userProfile.getEmail())) {
            throw new UserAuthenticationException(String.format(EMAIL_ALREADY_EXIST, userProfile.getEmail()));
        }

        if (RoleData.ADMIN.getDBRoleName().equals(userProfile.getRoleName())) {
            throw new IncorrectInputException("Illegal role argument: " + RoleData.ADMIN.getDBRoleName());
        }

        User user = dtoConverter.convertToEntity(userProfile, new User())
                .withPassword(passwordEncoder.encode(userProfile.getPassword()))
                .withRole(roleService.findByName(userProfile.getRoleName()));

        String phoneFormat = "38" + user.getPhone();

        user.setPhone(phoneFormat);

        log.debug(user.getPhone());

        user.setVerificationCode(RandomString.make(64));
        user.setStatus(false);
        user = userRepository.save(user);
        log.debug("user {} registered successfully", user);
        try {
            sendVerificationEmail(user);
        } catch (MailSendException ex) {
            throw new MailSendException("Email connection failed!");
        } catch (UnsupportedEncodingException | MessagingException ignored) {
            throw new DatabaseRepositoryException(USER_REGISTRATION_ERROR);
        }

        return dtoConverter.convertToDto(user, SuccessRegistration.class);
    }

    @Override
    public UserVerifyPassword validateUser(UserVerifyPassword userVerifyPassword) {
        if (userVerifyPassword.getPassword() == null) {
            userVerifyPassword.setPassword(
                    userRepository.findById(userVerifyPassword.getId())
                            .orElseThrow(NotVerifiedUserException::new)
                            .getPassword()
            );
            return userVerifyPassword;
        }

        if (passwordEncoder.matches(userVerifyPassword.getPassword(),
                getUserById(userVerifyPassword.getId()).getPassword())) {
            return userVerifyPassword;
        }
        throw new NotVerifiedUserException(String.format(NOT_VERIFIED, userVerifyPassword.getId()));
    }

    @Override
    public SuccessLogin loginUser(UserLogin userLogin) {
        userLogin.setEmail(userLogin.getEmail().toLowerCase());
        User user = getUserByEmail(userLogin.getEmail());
        if (!user.isStatus()) {
            throw new NotVerifiedUserException(String.format(NOT_VERIFIED, userLogin.getEmail()));
        } else if (!passwordEncoder.matches(userLogin.getPassword(), user.getPassword())) {
            throw new UserAuthenticationException(WRONG_PASSWORD);
        }
        log.debug("User {} logged successfully", userLogin);

        return SuccessLogin.builder()
                .id(user.getId())
                .email(user.getEmail())
                .roleName(user.getRole().getName())
                .accessToken(jwtUtils.generateAccessToken(userLogin.getEmail()))
                .refreshToken(refreshTokenService.assignRefreshToken(user))
                .build();
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public SuccessUpdatedUser updateUser(Long id, UserUpdateProfile userProfile) {
        User user = getUserById(id);

        if (!userProfile.getEmail().equals(user.getEmail())) {
            throw new IncorrectInputException(EMAIL_UPDATING_ERROR);
        }
        User newUser = dtoConverter.convertToEntity(userProfile, user).withPassword(user.getPassword()).withId(id)
                .withRole(roleService.findByName(userProfile.getRoleName())).withPhone(userProfile.getPhone());

        log.debug("updating role by id {}", newUser);

        return dtoConverter.convertToDto(userRepository.save(newUser), SuccessUpdatedUser.class);
    }

    @Override
    public UserResponse deleteUserById(Long id) {
        User user = getUserById(id);

        try {
            userRepository.deleteById(id);
            userRepository.flush();
        } catch (DataAccessException | ValidationException e) {
            throw new DatabaseRepositoryException(USER_DELETING_ERROR);
        }

        archiveModel(user);

        log.debug("user {} was successfully deleted", user);
        return dtoConverter.convertToDto(user, UserResponse.class);
    }

    @Override
    public SuccessVerification verify(String verificationCode) {
        User user = getUserByVerificationCode(verificationCode);

        user.setStatus(true);
        user.setVerificationCode(null);
        log.debug("user {} was successfully registered", user);
        userRepository.save(user);

        SuccessVerification successVerificationUser = dtoConverter.convertToDto(user, SuccessVerification.class);
        successVerificationUser.setMessage(
                String.format("Користувач %s %s успішно зареєстрований", user.getFirstName(), user.getLastName()));
        return successVerificationUser;
    }

    /**
     * The method send message {@code message} to new user after registration.
     *
     * @param user - put user entity
     * @throws MessagingException           if message isn`t sent
     * @throws UnsupportedEncodingException if there is wrong encoding
     * @value toAddress - an email of user to send verificationCode with httpRequest
     * @value fromAddress - an email of company getting from environment variables
     * @value senderName - name of company or name of user-sender
     * @value subject - email header
     * @value content - email body
     */
    private void sendVerificationEmail(User user) throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = (System.getenv("USER_EMAIL"));
        String senderName = "TeachUA";
        String subject = "Підтвердіть Вашу реєстрацію";

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        String verifyURL = baseUrl + "/verify?code=" + user.getVerificationCode();
        String content = "Шановний/а [[userFullName]]!<br>"
                + "Для підтвердження Вашої реєстрації, будь ласка, перейдіть за посиланням нижче: \n<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">Підтвердити реєстрацію</a></h3>" + "Дякуємо!<br>"
                + "Ініціатива \"Навчай українською\"";
        content = content.replace("[[userFullName]]", user.getLastName() + " " + user.getFirstName());
        content = content.replace("[[URL]]", verifyURL);
        message.setContent(content, "text/html; charset=UTF-8");

        javaMailSender.send(message);
        log.debug("Email has been sent\" {}", user.getEmail());
    }

    @Override
    public User getAuthenticatedUser() {
        UserPrincipal userPrincipal = userDetailsService.getUserPrincipal();
        return userRepository
                .findById(userPrincipal.getId())
                .orElseThrow(() ->
                        new NotExistException(String.format(USER_NOT_FOUND_BY_ID, userPrincipal.getId())));
    }

    @Override
    public void verifyIsUserAdmin() {
        if (!userDetailsService.getUserPrincipal().getAuthorities()
                .contains(new SimpleGrantedAuthority(RoleData.ADMIN.getDBRoleName()))) {
            throw new UserPermissionException(ONLY_ADMIN_CONTENT);
        }
    }

    private boolean isUserExistByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    private Optional<User> getOptionalUserById(Long id) {
        return userRepository.findById(id);
    }

    private Optional<User> getOptionalUserByVerificationCode(String verificationCode) {
        log.debug(verificationCode);
        return userRepository.findByVerificationCode(verificationCode);
    }

    private Optional<User> getOptionalUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public SuccessUserPasswordReset resetPassword(UserResetPassword userResetPassword) {
        User user = getUserByEmail(userResetPassword.getEmail());
        user.setVerificationCode(RandomString.make(64));
        try {
            String toAddress = user.getEmail();
            String fromAddress = (System.getenv("USER_EMAIL"));
            String senderName = "TeachUA";
            String subject = "Відновлення паролю";

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject(subject);

            String content = "Шановний/а [[userFullName]]!<br>"
                    + "Для відновлення Вашого паролю, будь ласка, перейдіть за посиланням нижче: \n<br> "
                    + "<h3><a href=\"[[URL]]\" target=\"_self\">Змінити пароль</a></h3>" + "Дякуємо!<br>"
                    + "Ініціатива \"Навчай українською\"";
            String verifyURL = baseUrl + "/verifyreset?code=" + user.getVerificationCode();
            content = content.replace("[[userFullName]]", user.getLastName() + " " + user.getFirstName());
            content = content.replace("[[URL]]", verifyURL);
            message.setContent(content, "text/html; charset=UTF-8");

            javaMailSender.send(message);
            log.debug("Email has been sent\" {}", user.getEmail());
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error(e.getMessage());
        }

        return null;
    }

    @Override
    public SuccessVerification verifyChange(String verificationCode) {
        log.debug("step1: " + verificationCode);
        User user = getUserByVerificationCode(verificationCode);
        user.setStatus(true);
        SuccessUserPasswordReset userPasswordReset = new SuccessUserPasswordReset();
        userPasswordReset.setVerificationCode(verificationCode);
        userPasswordReset.setEmail(user.getEmail());
        userPasswordReset.setId(user.getId());
        SuccessVerification successVerificationUser = dtoConverter.convertToDto(user, SuccessVerification.class);
        successVerificationUser
                .setMessage(String.format("Користувач %s %s верифікований", user.getFirstName(), user.getLastName()));

        log.debug("step 2: " + userPasswordReset.getVerificationCode() + " " + userPasswordReset.getEmail());
        return successVerificationUser;
    }

    @Override
    public void updatePassword(Long id, UserPasswordUpdate passwordUpdate) {
        User user = getUserById(id);

        if (!passwordEncoder.matches(passwordUpdate.getOldPassword(), user.getPassword())) {
            throw new UpdatePasswordException("Wrong old password");
        }

        if (!passwordUpdate.getNewPassword().equals(passwordUpdate.getNewPasswordVerify())) {
            throw new UpdatePasswordException("Verify password doesnt match to new");
        }

        if (passwordEncoder.matches(passwordUpdate.getNewPassword(), user.getPassword())) {
            throw new UpdatePasswordException("New password equals to old");
        }

        user.setPassword(passwordEncoder.encode(passwordUpdate.getNewPassword()));

        userRepository.save(user);
    }

    @Override
    public SuccessUserPasswordReset verifyChangePassword(SuccessUserPasswordReset userResetPassword) {
        log.debug("step 3: {} {}", userResetPassword.getVerificationCode(), userResetPassword.getEmail());
        User user = getUserByVerificationCode(userResetPassword.getVerificationCode());
        user.setStatus(true);
        if (passwordEncoder.matches(userResetPassword.getPassword(), user.getPassword())) {
            throw new MatchingPasswordException("Новий пароль співпадає з старим");
        }
        userResetPassword.setEmail(user.getEmail());
        userResetPassword.setId(user.getId());
        user.setPassword(passwordEncoder.encode(userResetPassword.getPassword()));
        user.setVerificationCode(null);

        userRepository.save(user);
        log.debug("Password reset {}", user);
        return userResetPassword;
    }

    @Override
    public void archiveModel(User user) {
        archiveService.saveModel(dtoConverter.convertToDto(user, UserArch.class));
    }

    @Override
    public void restoreModel(String archiveObject) throws JsonProcessingException {
        UserArch userArch = objectMapper.readValue(archiveObject, UserArch.class);
        User user = dtoConverter.convertToEntity(userArch, User.builder().build()).withId(null)
                .withRole(roleService.getRoleById(userArch.getRoleId()))
                .withProvider(Optional.ofNullable(userArch.getProvider()).isPresent()
                        ? AuthProvider.valueOf(userArch.getProvider()) : null);
        userRepository.save(user);
    }
}
