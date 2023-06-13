package com.softserve.user.service;

import com.softserve.commons.exception.AlreadyExistException;
import com.softserve.commons.exception.DatabaseRepositoryException;
import com.softserve.commons.exception.NotExistException;
import com.softserve.commons.exception.NotVerifiedUserException;
import com.softserve.user.dto.SuccessLogin;
import com.softserve.user.dto.SuccessRegistration;
import com.softserve.user.dto.SuccessUpdatedUser;
import com.softserve.user.dto.SuccessUserPasswordReset;
import com.softserve.user.dto.SuccessVerification;
import com.softserve.user.dto.UserLogin;
import com.softserve.user.dto.UserPasswordUpdate;
import com.softserve.user.dto.UserProfile;
import com.softserve.user.dto.UserResetPassword;
import com.softserve.user.dto.UserResponse;
import com.softserve.user.dto.UserUpdateProfile;
import com.softserve.user.dto.UserVerifyPassword;
import com.softserve.user.exception.UserAuthenticationException;
import com.softserve.user.model.User;
import java.util.List;

/**
 * This interface contains all needed methods to manage users.
 */

public interface UserService {
    /**
     * The method checks whether a user exists.
     *
     * @param id - place user id.
     */
    boolean existsById(Long id);

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
     * @throws UserAuthenticationException if user password is incorrect.
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
