package com.softserve.teachua.security.service;

import com.softserve.teachua.dto.user.UserLogin;
import com.softserve.teachua.model.User;

public interface EncoderService {
    boolean isValidPassword(UserLogin userLogin, User userEntity);

    String encodePassword(String rawPassword);
}
