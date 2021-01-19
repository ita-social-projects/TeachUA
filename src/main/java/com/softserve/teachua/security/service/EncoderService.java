package com.softserve.teachua.security.service;

import com.softserve.teachua.dto.security.UserEntity;
import com.softserve.teachua.dto.service.UserLogin;

public interface EncoderService {

    boolean isValidPassword(UserLogin userLogin, UserEntity userEntity);

    String encodePassword(String rawPassword);

    String createToken(String email);

}
