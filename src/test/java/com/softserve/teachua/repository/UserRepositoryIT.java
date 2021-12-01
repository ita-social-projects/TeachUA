package com.softserve.teachua.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
public class UserRepositoryIT {

    @Autowired
    private UserRepository userRepository;

    private static final String ADMIN_EMAIL = "admin@gmail.com";
    private static final String WRONG_EMAIL = "wrong@gmail.com";

    @Test
    public void existsByAdminEmailShouldReturnTrue(){
        assertThat(userRepository.existsByEmail(ADMIN_EMAIL))
                .isEqualTo(true);
    }

    @Test
    public void existsByWrongEmailShouldReturnFalse(){
        assertThat(userRepository.existsByEmail(WRONG_EMAIL))
                .isEqualTo(false);
    }

    @Test
    public void findByAdminEmailShouldReturnUser(){
        assertThat(userRepository.findByEmail(ADMIN_EMAIL).get().getEmail())
                .isEqualTo(ADMIN_EMAIL);
    }

    @Test
    public void findByAdminEmailShouldReturnOptionalEmpty(){
        assertThat(userRepository.findByEmail(WRONG_EMAIL))
                .isEqualTo(Optional.empty());
    }

}
