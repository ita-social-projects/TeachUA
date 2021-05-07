package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.security.UserEntity;
import com.softserve.teachua.dto.user.*;
import com.softserve.teachua.exception.*;
import com.softserve.teachua.model.User;
import com.softserve.teachua.repository.UserRepository;
import com.softserve.teachua.security.JwtProvider;
import com.softserve.teachua.security.service.EncoderService;
import com.softserve.teachua.service.ArchiveService;
import com.softserve.teachua.service.RoleService;
import com.softserve.teachua.service.UserService;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class UserServiceImpl implements UserService {
    private static final String EMAIL_ALREADY_EXIST = "Email %s already exist";
    private static final String USER_NOT_FOUND_BY_ID = "User not found by id %s";
    private static final String USER_NOT_FOUND_BY_EMAIL = "User not found by email %s";
    private static final String USER_NOT_FOUND_BY_VERIFICATION_CODE = "User not found or invalid link";
    private static final String WRONG_PASSWORD = "Wrong password: %s";
    private static final String NOT_VERIFIED = "User is not verified: %s";
    private static final String USER_DELETING_ERROR = "Can't delete user cause of relationship";
    private static final String USER_REGISTRATION_ERROR = "Can't register user";

    @Value("${baseURL}")
    private String baseUrl;

    private final UserRepository userRepository;
    private final EncoderService encodeService;
    private final RoleService roleService;
    private final DtoConverter dtoConverter;
    private final ArchiveService archiveService;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final JavaMailSender javaMailSender;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           EncoderService encodeService,
                           RoleService roleService,
                           DtoConverter dtoConverter,
                           ArchiveService archiveService,
                           JwtProvider jwtProvider,
                           AuthenticationManager authenticationManager,
                           JavaMailSender javaMailSender) {
        this.userRepository = userRepository;
        this.encodeService = encodeService;
        this.roleService = roleService;
        this.dtoConverter = dtoConverter;
        this.archiveService = archiveService;
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
        this.javaMailSender = javaMailSender;
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
     * The method returns entity {@code User} of user by id.
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
     * The method returns entity {@code User} of user by verificationCode.
     *
     * @param verificationCode - put user verificationCode.
     * @return new {@code User}.
     * @throws NotExistException if user not exists.
     */
    @Override
    public User getUserByVerificationCode(String verificationCode) {
        Optional<User> optionalUser = getOptionalUserByVerificationCode(verificationCode);
        if (!optionalUser.isPresent()) {
            throw new NotExistException(USER_NOT_FOUND_BY_VERIFICATION_CODE);
        }

        log.info("getting user by verificationCode {}", userRepository.findByVerificationCode(verificationCode));
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
     * @param userProfile- place dto with all params
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
                .withRole(roleService.findByName(userProfile.getRoleName()));

        user.setVerificationCode(RandomString.make(64));
        user.setStatus(false);
        user = userRepository.save(user);
        log.info("user {} registered successfully", user);
        try {
            sendVerificationEmail(user);
        } catch (UnsupportedEncodingException | MessagingException ignored) {
            throw new DatabaseRepositoryException(USER_REGISTRATION_ERROR);
        }

        return dtoConverter.convertToDto(user, SuccessRegistration.class);
    }

    /**
     * The method returns dto {@code SuccessLogin} if user successfully logged.
     *
     * @param userLogin- place dto with all params.
     * @return new {@code SuccessLogin}.
     * @throws WrongAuthenticationException if user password is incorrect.
     * @throws NotVerifiedUserException     if user verification code is incorrect.
     */
    @Override
    public SuccessLogin validateUser(UserLogin userLogin) {
        UserEntity userEntity = getUserEntity(userLogin.getEmail());
        if (encodeService.isValidStatus(userLogin, userEntity)) {
            throw new NotVerifiedUserException(String.format(NOT_VERIFIED, userLogin.getEmail()));
        } else if (!encodeService.isValidPassword(userLogin, userEntity)) {
            throw new WrongAuthenticationException(String.format(WRONG_PASSWORD, userLogin.getPassword()));
        }
        log.info("user {} logged successfully", userLogin);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLogin.getEmail(),
                        userLogin.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return dtoConverter.convertFromDtoToDto(userEntity, new SuccessLogin())
                .withAccessToken(jwtProvider.generateToken(authentication));
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
                .withId(id)
                .withRole(roleService.findByName(userProfile.getRoleName()));

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
            userRepository.flush();
        } catch (DataAccessException | ValidationException e) {
            throw new DatabaseRepositoryException(USER_DELETING_ERROR);
        }

        log.info("user {} was successfully deleted", user);
        return dtoConverter.convertToDto(user, UserResponse.class);
    }

    /**
     * The method returns dto {@code SuccessVerification} of user by verification code
     *
     * @param verificationCode - put user verificationCode.
     * @return new {@code SuccessVerification}.
     * @throws NotExistException        if user not exist
     * @throws NotVerifiedUserException if user status is inactive
     */
    @Override
    public SuccessVerification verify(String verificationCode) {
        User user = getUserByVerificationCode(verificationCode);

        user.setStatus(true);
        user.setVerificationCode(null);
        log.info("user {} was successfully registered", user);
        userRepository.save(user);

        SuccessVerification successVerificationUser = dtoConverter.convertToDto(user, SuccessVerification.class);
        successVerificationUser.setMessage(String.format("Користувач %s %s успішно зареєстрований",
                                                        user.getFirstName(),
                                                        user.getLastName()));
        return successVerificationUser;
    }

    /**
     * The method send message {@code message} to new user after registration
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
    private void sendVerificationEmail(User user)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = (System.getenv("USER_EMAIL"));
        String senderName = "TeachUA";
        String subject = "Підтвердіть Вашу реєстрацію";
        String content = "Шановний/а [[userFullName]]!<br>"
                + "Для підтвердження Вашої реєстрації, будь ласка, перейдіть за посиланням нижче: \n<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">Підтвердити реєстрацію</a></h3>"
                + "Дякуємо!<br>"
                + "Ініціатива \"Навчай українською\"";

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[userFullName]]", user.getLastName() + " " + user.getFirstName());

        String verifyURL = baseUrl + "/verify?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);
        helper.setText(content, true);

        javaMailSender.send(message);
        log.info("Email has been sent\" {}", user.getEmail());
    }

    @Override
    public void validateUserId(Long id, HttpServletRequest httpServletRequest) {
        String token = jwtProvider.getJwtFromRequest(httpServletRequest);
        if (jwtProvider.getUserIdFromToken(token) != id) {
            throw new BadRequestException("Wrong id");
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
        return userRepository.findByVerificationCode(verificationCode);
    }

    private Optional<User> getOptionalUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}