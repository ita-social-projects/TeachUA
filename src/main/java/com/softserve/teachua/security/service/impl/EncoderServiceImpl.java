package com.softserve.teachua.security.service.impl;

import com.softserve.teachua.dto.security.UserEntity;
import com.softserve.teachua.dto.user.UserLogin;
import com.softserve.teachua.security.JwtProvider;
import com.softserve.teachua.security.service.EncoderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EncoderServiceImpl implements EncoderService {
    //private JwtProvider jwtProvider;

    private PasswordEncoder passwordEncoder;

//    @Autowired
//    public EncoderServiceImpl(JwtProvider jwtProvider, PasswordEncoder passwordEncoder) {
//        this.jwtProvider = jwtProvider;
//        this.passwordEncoder = passwordEncoder;
//    }

    @Autowired
    public void setPasswordEncoder(@Lazy PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public boolean isValidPassword(UserLogin userLogin, UserEntity userEntity) {
        return passwordEncoder.matches(userLogin.getPassword(), userEntity.getPassword());
    }

    public boolean isValidStatus(UserEntity userEntity) {
        return (userEntity.isStatus());
    }

    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}
