package com.softserve.teachua.security.service.impl;

import com.softserve.teachua.dto.security.UserEntity;
import com.softserve.teachua.dto.user.UserLogin;
import com.softserve.teachua.security.JwtProvider;
import com.softserve.teachua.security.service.EncoderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EncoderServiceImpl implements EncoderService {

    private JwtProvider jwtProvider;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public EncoderServiceImpl(JwtProvider jwtProvider, PasswordEncoder passwordEncoder) {
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean isValidPassword(UserLogin userLogin, UserEntity userEntity) {
        return passwordEncoder.matches(userLogin.getPassword(),
                userEntity.getPassword());
    }

    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public String createToken(String email) {
        return jwtProvider.generateToken(email);
    }
}
