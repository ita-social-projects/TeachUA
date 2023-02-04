package com.softserve.teachua.security.service.impl;

import com.softserve.teachua.dto.user.UserLogin;
import com.softserve.teachua.model.User;
import com.softserve.teachua.security.service.EncoderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EncoderServiceImpl implements EncoderService {
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(@Lazy PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public boolean isValidPassword(UserLogin userLogin, User user) {
        return passwordEncoder.matches(userLogin.getPassword(), user.getPassword());
    }

    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}
