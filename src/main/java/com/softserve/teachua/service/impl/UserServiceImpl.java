package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.security.UserEntity;
import com.softserve.teachua.dto.user.*;
import com.softserve.teachua.exception.*;
import com.softserve.teachua.model.AuthProvider;
import com.softserve.teachua.model.User;
import com.softserve.teachua.model.archivable.UserArch;
import com.softserve.teachua.repository.UserRepository;
import com.softserve.teachua.security.JwtProvider;
import com.softserve.teachua.security.service.EncoderService;
import com.softserve.teachua.service.ArchiveMark;
import com.softserve.teachua.service.ArchiveService;
import com.softserve.teachua.service.RoleService;
import com.softserve.teachua.service.UserService;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, ArchiveMark<User> {
    private static final String EMAIL_ALREADY_EXIST = "Email %s already exist";
    private static final String EMAIL_UPDATING_ERROR = "Email can`t be updated";
    private static final String ROLE_UPDATING_ERROR = "Role can`t be changed to Admin";
    private static final String USER_NOT_FOUND_BY_ID = "User not found by id %s";
    private static final String USER_NOT_FOUND_BY_EMAIL = "User not found by email %s";
    private static final String USER_NOT_FOUND_BY_VERIFICATION_CODE = "User not found or invalid link";
    private static final String USERS_NOT_FOUND_BY_ROLE_NAME = "User not found by role name - %s";
    private static final String WRONG_PASSWORD = "Wrong password";
    private static final String NOT_VERIFIED = "User is not verified: %s";
    private static final String USER_DELETING_ERROR = "Can't delete user cause of relationship";
    private static final String USER_REGISTRATION_ERROR = "Can't register user";
    private static final String WRONG_ID = "Wrong id";
    private static final String INACCESSIBLE_ADMIN_PROFILE = "No one have access to admin profile";
    private static final String ONLY_ADMIN_CONTENT = "Only the admin have permit to view this content";
    private final UserRepository userRepository;
    private final EncoderService encodeService;
    private final RoleService roleService;
    private final DtoConverter dtoConverter;
    private final ArchiveService archiveService;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;
    @Value("${baseURL}")
    private String baseUrl;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, EncoderService encodeService, RoleService roleService,
            DtoConverter dtoConverter, ArchiveService archiveService, JwtProvider jwtProvider,
            @Lazy AuthenticationManager authenticationManager, JavaMailSender javaMailSender,
            @Lazy PasswordEncoder passwordEncoder, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.encodeService = encodeService;
        this.roleService = roleService;
        this.dtoConverter = dtoConverter;
        this.archiveService = archiveService;
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
        this.javaMailSender = javaMailSender;
        this.passwordEncoder = passwordEncoder;
        this.objectMapper = objectMapper;
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
        Optional<User> optionalUser = getOptionalUserById(id);
        if (!optionalUser.isPresent()) {
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
                .collect(Collectors.toList());
        log.debug("getting users by role name - {}", roleName);
        return userResponses;
    }

    @Override
    public User getUserByEmail(String email) {
        Optional<User> optionalUser = getOptionalUserByEmail(email);
        if (!optionalUser.isPresent()) {
            throw new NotExistException(String.format(USER_NOT_FOUND_BY_EMAIL, email));
        }

        log.debug("getting user by email {}", userRepository.findByEmail(email));
        return optionalUser.get();
    }

    @Override
    public User getUserByVerificationCode(String verificationCode) {
        Optional<User> optionalUser = getOptionalUserByVerificationCode(verificationCode);
        if (!optionalUser.isPresent()) {
            throw new NotExistException(USER_NOT_FOUND_BY_VERIFICATION_CODE);
        }

        log.debug("getting user by verificationCode {}", userRepository.findByVerificationCode(verificationCode));
        return optionalUser.get();
    }

    @Override
    public List<UserResponse> getListOfUsers() {
        List<UserResponse> userResponses = userRepository.findAll().stream()
                .map(user -> (UserResponse) dtoConverter.convertToDto(user, UserResponse.class))
                .collect(Collectors.toList());

        log.debug("getting list of users {}", userResponses);
        return userResponses;
    }

    @Override
    public SuccessRegistration registerUser(UserProfile userProfile) {
        userProfile.setEmail(userProfile.getEmail().toLowerCase());
        if (isUserExistByEmail(userProfile.getEmail())) {
            throw new WrongAuthenticationException(String.format(EMAIL_ALREADY_EXIST, userProfile.getEmail()));
        }

        if (RoleData.ADMIN.getDBRoleName().equals(userProfile.getRoleName())) {
            throw new IncorrectInputException("Illegal role argument: " + RoleData.ADMIN.getDBRoleName());
        }

        User user = dtoConverter.convertToEntity(userProfile, new User())
                .withPassword(encodeService.encodePassword(userProfile.getPassword()))
                .withRole(roleService.findByName(userProfile.getRoleName()));

        String phoneFormat = "38" + user.getPhone();
        // String Formated = String.format("%s (%s) %s %s %s", phoneFormat.substring(0, 3),
        // phoneFormat.substring(3, 6), phoneFormat.substring(6, 9), phoneFormat.substring(9, 11),
        // phoneFormat.substring(11, 13));

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
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
    public SuccessLogin validateUser(UserLogin userLogin) {
        userLogin.setEmail(userLogin.getEmail().toLowerCase());
        UserEntity userEntity = getUserEntity(userLogin.getEmail());
        if (!encodeService.isValidStatus(userEntity)) {
            throw new NotVerifiedUserException(String.format(NOT_VERIFIED, userLogin.getEmail()));
        } else if (!encodeService.isValidPassword(userLogin, userEntity)) {
            throw new WrongAuthenticationException(WRONG_PASSWORD);
        }
        log.debug("user {} logged successfully", userLogin);

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(userLogin.getEmail(), userLogin.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return dtoConverter.convertFromDtoToDto(userEntity, new SuccessLogin())
                .withAccessToken(jwtProvider.generateToken(authentication));
    }

    @Override
    public SuccessUpdatedUser updateUser(Long id, UserUpdateProfile userProfile) {
        User user = getUserById(id);

        if (!userProfile.getEmail().equals(user.getEmail())) {
            throw new IncorrectInputException(EMAIL_UPDATING_ERROR);
        }
        /*
         * Admin must be able to appoint someone else as the admin
         *
         * if (userProfile.getRoleName().equals(RoleData.ADMIN.getDBRoleName()) &&
         * !user.getRole().getName().equals(RoleData.ADMIN.getDBRoleName())) { throw new
         * IncorrectInputException(ROLE_UPDATING_ERROR); }
         */
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
     * @param user
     *            - put user entity
     *
     * @throws MessagingException
     *             if message isn`t sent
     * @throws UnsupportedEncodingException
     *             if there is wrong encoding
     *
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
        String content = "Шановний/а [[userFullName]]!<br>"
                + "Для підтвердження Вашої реєстрації, будь ласка, перейдіть за посиланням нижче: \n<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">Підтвердити реєстрацію</a></h3>" + "Дякуємо!<br>"
                + "Ініціатива \"Навчай українською\"";
        String verifyURL = baseUrl + "/verify?code=" + user.getVerificationCode();
        // String verifyURL = "http://localhost:3000/dev/verify?code=" + user.getVerificationCode();

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[userFullName]]", user.getLastName() + " " + user.getFirstName());
        content = content.replace("[[URL]]", verifyURL);
        message.setContent(content, "text/html; charset=UTF-8");

        javaMailSender.send(message);
        log.debug("Email has been sent\" {}", user.getEmail());
    }

    @Override
    public void validateUserId(Long id, HttpServletRequest httpServletRequest) {
        User userFromRequest = getUserFromRequest(httpServletRequest);
        User updUser = getUserById(id);

        if (!userFromRequest.getId().equals(id)) {
            if (updUser.getRole().getName().equals(RoleData.ADMIN.getDBRoleName())) {
                throw new IncorrectInputException(INACCESSIBLE_ADMIN_PROFILE);
            }
            if (!userFromRequest.getRole().getName().equals(RoleData.ADMIN.getDBRoleName())) {
                throw new IncorrectInputException(WRONG_ID);
            }
        }
    }

    @Override
    public User getUserFromRequest(HttpServletRequest httpServletRequest) {
        return userRepository
                .findById(jwtProvider.getUserIdFromToken(jwtProvider.getJwtFromRequest(httpServletRequest)))
                .orElseThrow(WrongAuthenticationException::new);
    }

    @Override
    public void verifyIsUserAdmin() {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new WrongAuthenticationException(ONLY_ADMIN_CONTENT));
        if (!user.getRole().getName().equals(RoleData.ADMIN.getDBRoleName())) {
            throw new WrongAuthenticationException(ONLY_ADMIN_CONTENT);
        }
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
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
            String content = "Шановний/а [[userFullName]]!<br>"
                    + "Для відновлення Вашого паролю, будь ласка, перейдіть за посиланням нижче: \n<br> "
                    + "<h3><a href=\"[[URL]]\" target=\"_self\">Змінити пароль</a></h3>" + "Дякуємо!<br>"
                    + "Ініціатива \"Навчай українською\"";
            String verifyURL = baseUrl + "/verifyreset?code=" + user.getVerificationCode();
            // String verifyURL = "http://localhost:3000/dev/verifyreset?code=" + user.getVerificationCode();

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject(subject);

            content = content.replace("[[userFullName]]", user.getLastName() + " " + user.getFirstName());
            content = content.replace("[[URL]]", verifyURL);
            message.setContent(content, "text/html; charset=UTF-8");

            javaMailSender.send(message);
            log.debug("Email has been sent\" {}", user.getEmail());
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error(e.getMessage());
        }

        // return dtoConverter.convertToDto(user, SuccessUserPasswordReset.class);
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
        // return userPasswordReset;
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
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        log.debug("step 3: " + userResetPassword.getVerificationCode() + " " + userResetPassword.getEmail());
        User user = getUserByVerificationCode(userResetPassword.getVerificationCode());
        user.setStatus(true);
        if (bCryptPasswordEncoder.matches(userResetPassword.getPassword(), user.getPassword())) {
            throw new MatchingPasswordException("Новий пароль співпадає з старим");
        }
        userResetPassword.setEmail(user.getEmail());
        userResetPassword.setId(user.getId());
        user.setPassword(encodeService.encodePassword(userResetPassword.getPassword()));
        user.setVerificationCode(null);

        userRepository.save(user);
        log.debug("password reset {}", user);
        return userResetPassword;
    }

    @Override
    public User getCurrentUser() {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        return getUserFromRequest(httpServletRequest);
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
