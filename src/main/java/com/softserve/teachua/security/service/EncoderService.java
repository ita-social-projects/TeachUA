package com.softserve.teachua.security.service;

import com.softserve.teachua.dto.security.UserEntity;
import com.softserve.teachua.dto.user.UserLogin;

public interface EncoderService {

    boolean isValidPassword(UserLogin userLogin, UserEntity userEntity);

    boolean isValidStatus(UserEntity userEntity);

    String encodePassword(String rawPassword);

}
