package com.softserve.teachua.service;

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
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.DatabaseRepositoryException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.exception.NotVerifiedUserException;
import com.softserve.teachua.exception.UserAuthenticationException;
import com.softserve.teachua.model.User;
import com.softserve.teachua.security.CustomUserDetailsService;
import java.util.List;

/**
 * This interface contains all needed methods to manage users.
 */

public interface UserService {
    /**
     * The method returns dto {@code UserResponse} of user by id.
     *
     * @param id - put user id.
     * @return new {@code UserResponse}.
     */
    UserResponse getUserProfileById(Long id);

    /**
     * The method returns entity {@code User} of user by id.
     *
     * @param id - put user id.
     * @return new {@code User}.
     * @throws NotExistException if user not exists.
     */
    User getUserById(Long id);

    /**
     * The method get entire entity of authenticated user from database.
     * Please, use {@link CustomUserDetailsService#getUserPrincipal()},
     * if you need only id or email of authenticated user.
     */
    User getAuthenticatedUser();

    /**
     * The method returns dto {@code UserResponse} of deleted user by id.
     *
     * @param id - put user id.
     * @return new {@code UserResponse}.
     * @throws DatabaseRepositoryException if user contain foreign keys.
     */
    UserResponse deleteUserById(Long id);

    /**
     * The method returns list of dto {@code UserResponse} from users by role name.
     *
     * @param roleName - put roleName.
     * @return new {@code List<UserResponse>}.
     * @throws NotExistException if users not exists.
     */
    List<UserResponse> getUserResponsesByRole(String roleName);

    /**
     * The method returns entity {@code User} of user by id.
     *
     * @param email - put user email.
     * @return new {@code User}.
     * @throws NotExistException if user not exists.
     */
    User getUserByEmail(String email);

    /**
     * The method returns entity {@code User} of user by verificationCode.
     *
     * @param verificationCode - put user verificationCode.
     * @return new {@code User}.
     * @throws NotExistException if user not exists.
     */
    User getUserByVerificationCode(String verificationCode);

    /**
     * The method returns list of dto {@code List<UserResponse>} of all users.
     *
     * @return new {@code List<UserResponse>}.
     */
    List<UserResponse> getListOfUsers();

    /**
     * The method returns dto {@code SuccessRegistration} if user successfully registered.
     *
     * @param userProfile - place dto with all params
     * @return new {@code SuccessRegistration}.
     * @throws AlreadyExistException if user with email already exists.
     */
    SuccessRegistration registerUser(UserProfile userProfile);

    /**
     * The method returns dto {@code SuccessLogin} if user successfully logged.
     *
     * @param userLogin - place dto with all params.
     * @return new {@code SuccessLogin}.
     * @throws UserAuthenticationException    if user password is incorrect.
     * @throws NotVerifiedUserException if user email is not verified.
     */
    SuccessLogin loginUser(UserLogin userLogin);

    /**
     * The method returns dto {@code UserVerifyPassword} if user successfully logged.
     *
     * @param userVerifyPassword - place dto with all params.
     * @return new {@code UserVerifyPassword}.
     * @throws NotVerifiedUserException if user password is incorrect.
     */
    UserVerifyPassword validateUser(UserVerifyPassword userVerifyPassword);

    /**
     * The method returns dto {@code SuccessUpdatedUser} of updated user.
     *
     * @param userProfile - place dto with all params.
     * @return new {@code SuccessUpdatedUser}.
     * @throws NotExistException if user id is incorrect.
     */
    SuccessUpdatedUser updateUser(Long id, UserUpdateProfile userProfile);

    /**
     * The method saves entity of user to database.
     */
    void updateUser(User user);

    /**
     * The method checks if user is admin.
     */
    void verifyIsUserAdmin();

    /**
     * The method returns dto {@code SuccessVerification} of user by verification code.
     *
     * @param verificationCode - put user verificationCode.
     * @return new {@code SuccessVerification}.
     * @throws NotExistException        if user not exist
     * @throws NotVerifiedUserException if user status is inactive
     */
    SuccessVerification verify(String verificationCode);

    /**
     * The method returns dto {@code SuccessUserPasswordReset} of updated password.
     *
     * @param userLogin - place dto {@code UserResetPassword} with all params.
     * @return new {@code SuccessUserPasswordReset}.
     */
    SuccessUserPasswordReset resetPassword(UserResetPassword userLogin);

    /**
     * The method verifies changed password and returns dto {@code SuccessUserPasswordReset} of updated password.
     *
     * @param userResetPassword - place dto {@code SuccessUserPasswordReset} with all params.
     * @return new {@code SuccessUserPasswordReset}
     */
    SuccessUserPasswordReset verifyChangePassword(SuccessUserPasswordReset userResetPassword);

    /**
     * The method verifies changes by verification code and returns dto {@code SuccessVerification}.
     *
     * @param verificationCode - verification code.
     * @return new {@code SuccessVerification}
     */
    SuccessVerification verifyChange(String verificationCode);

    /**
     * The method updates password.
     *
     * @param passwordUpdate - place dto {@code UserPasswordUpdate} with all params.
     */
    void updatePassword(Long id, UserPasswordUpdate passwordUpdate);
}
