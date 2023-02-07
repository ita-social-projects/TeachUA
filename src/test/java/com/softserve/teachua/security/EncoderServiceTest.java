package com.softserve.teachua.security;

import static com.softserve.teachua.TestConstants.NOT_EMPTY_STRING;
import static com.softserve.teachua.TestConstants.PASSWORD;
import static com.softserve.teachua.TestUtils.getUser;
import static com.softserve.teachua.TestUtils.getUserLogin;
import com.softserve.teachua.dto.user.UserLogin;
import com.softserve.teachua.model.User;
import com.softserve.teachua.security.service.impl.EncoderServiceImpl;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class EncoderServiceTest {
    UserLogin userLogin;
    User user;
    @Mock
    PasswordEncoder passwordEncoder;
    EncoderServiceImpl encoderService;

    @BeforeEach
    void setUp() {
        encoderService = new EncoderServiceImpl();
        encoderService.setPasswordEncoder(passwordEncoder);
        user = getUser();
        userLogin = getUserLogin();
    }

    @Test
    void isValidPassword() {
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        boolean actual = encoderService.isValidPassword(userLogin, user);
        assertTrue(actual);
    }

    @Test
    void encodePassword() {
        when(passwordEncoder.encode(anyString())).thenReturn(PASSWORD);
        String actual = encoderService.encodePassword(NOT_EMPTY_STRING);
        assertEquals(PASSWORD, actual);
    }
}
