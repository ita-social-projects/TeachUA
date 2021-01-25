package com.softserve.teachua.service.impl;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.dto.RoleResponce;
import com.softserve.teachua.dto.controller.SuccessLogin;
import com.softserve.teachua.dto.controller.SuccessRegistration;
import com.softserve.teachua.dto.security.UserEntity;
import com.softserve.teachua.dto.service.UserLogin;
import com.softserve.teachua.dto.service.UserProfile;
import com.softserve.teachua.exception.WrongAuthenticationException;
import com.softserve.teachua.model.Role;
import com.softserve.teachua.model.User;
import com.softserve.teachua.repository.RoleRepository;
import com.softserve.teachua.repository.UserRepository;
import com.softserve.teachua.security.service.EncoderService;
import com.softserve.teachua.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

//@AllArgsConstructor
@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
    private final String EMAIL_ALREADY_EXIST = "Email %s already exist";

    // @Autowired
    private UserRepository userRepository;

    // @Autowired
    private RoleRepository roleRepository;

    // @Autowired
    private EncoderService encodeService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           EncoderService encodeService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encodeService = encodeService;
        initDefaultData();
    }

    // TODO  move another class
    private void initDefaultData() {
        if ((roleRepository.count() == 0)
                && (userRepository.count() == 0)) {
            roleRepository.save(new Role(1, RoleData.USER.getDBRoleName()));
            Role adminRole = roleRepository.save(new Role(2, RoleData.ADMIN.getDBRoleName()));
            User admin = new User(1L, "admin@gmail.com",
                    encodeService.encodePassword("admin"), "adminName", adminRole);
            userRepository.save(admin);
        }
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id);
    }

//    @Override
//    public List<UserEntity> getListOfUserEntities() {
//        List<User> users = getListOfUsers();
//        return (List<UserEntity>) users.stream();
//    }

    @Override
    public List<User> getListOfUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserProfile updateUserProfileById(Long id) {
        if (!isUserExistById(id)) {
            String USER_NOT_FOUND_BY_ID = "User not found by id %s";
            log.error(String.format(USER_NOT_FOUND_BY_ID, id));
            throw new WrongAuthenticationException(String.format(USER_NOT_FOUND_BY_ID, id));
        }
        return null;
    }

    @Override
    public ResponseEntity<UserProfile> deleteUserById(Long id) {
        userRepository.deleteById(id);
        return null;
    }

    @Override
    public UserProfile getUserProfileById(Long id) {
        if (!isUserExistById(id)) {
            String USER_NOT_FOUND_BY_ID = "User not found by id %s";
            log.error(String.format(USER_NOT_FOUND_BY_ID, id));
            throw new WrongAuthenticationException(String.format(USER_NOT_FOUND_BY_ID, id));
        }

        User user = getUserById(id);
        return UserProfile.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }


    private boolean isUserExist(String email) {
        return getUser(email) != null;
    }

    private boolean isUserExistById(Long id) {
        return getUserById(id) != null;
    }

    public UserEntity getUserEntity(String email) {
        if (!isUserExist(email)) {
            String USER_NOT_FOUND_BY_EMAIL = "User not found by email %s";
            log.error(String.format(USER_NOT_FOUND_BY_EMAIL, email));
            throw new WrongAuthenticationException(String.format(USER_NOT_FOUND_BY_EMAIL, email));
        }
        User user = getUser(email);
        return UserEntity.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .roleName(user.getRole().getName())
                .build();
    }

    public SuccessRegistration registerUser(UserProfile userProfile) {
        if (isUserExist(userProfile.getEmail())) {
            log.error(String.format(EMAIL_ALREADY_EXIST, userProfile.getEmail()));
            // TODO Develop DuplicateEmailException
            throw new WrongAuthenticationException(String.format(EMAIL_ALREADY_EXIST, userProfile.getEmail()));
        }
        User user = User.builder()
                .email(userProfile.getEmail())
                .password(encodeService.encodePassword(userProfile.getPassword()))
                .role(roleRepository.findByName(RoleData.USER.getDBRoleName()))
                .build();
        user = userRepository.save(user);
        if (user == null) {
            // TODO Develop Custom Exception
            throw new RuntimeException("Invalid User");
        }
        // TODO user.getRole().getName() delete ROLE_
        return new SuccessRegistration(user.getId(), user.getEmail(), user.getRole().getName());
    }

    public SuccessLogin validateUser(UserLogin userLogin) {
        UserEntity userEntity = getUserEntity(userLogin.getEmail());
        if (!encodeService.isValidPassword(userLogin, userEntity)) {
            // TODO Develop Custom Exception
            // else timestamp email status 403  "message": "The user does not exist by this email: string@aaa.com",
            throw new RuntimeException("BAD_PASSWORD");
        }
        return SuccessLogin.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .accessToken(encodeService.createToken(userEntity.getEmail()))
                .build();
    }


    /*-
    // TODO move security package
    public String getExpirationLocalDate() {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LocalDateTime localDate = customUserDetails.getExpirationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy 'at' hh:mm");
        return localDate.format(formatter);
    }
    */

    // TODO Role Service
    public List<RoleResponce> getAllRoles() {
        List<RoleResponce> result = new ArrayList<>();
        for (Role role : roleRepository.findAll()) {
            result.add(new RoleResponce(role.getName()));
        }
        return result;
    }

}
