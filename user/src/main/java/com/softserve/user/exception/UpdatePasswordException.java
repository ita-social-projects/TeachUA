package com.softserve.user.exception;

/**
 * This class is custom exception for problem with password update. (Invalid old password, new password does not match
 * with verify, etc.) The constructor accepts message for Exception
 *
 * @author Klymus Roman
 */
public class UpdatePasswordException extends IllegalArgumentException {
    public UpdatePasswordException(String s) {
        super(s);
    }
}
