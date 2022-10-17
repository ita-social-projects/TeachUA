package com.softserve.teachua.repository;

import com.softserve.teachua.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@DataJpaTest
public class UserRepositoryIT {

    @Autowired
    private UserRepository userRepository;

    private static final String ADMIN_EMAIL = "admin@gmail.com";
    private static final String WRONG_EMAIL = "wrong@gmail.com";
    private static final String USER_EMAIL = "user@gmail.com";

    private static final Long EXISTING_ID = 1L;
    private static final Long WRONG_ID = 100L;
    private static final Long DELETED_ID = 199L;

    @Test
    public void existsByAdminEmailShouldReturnTrue() {
        assertTrue(userRepository.existsByEmail(ADMIN_EMAIL));
    }

    @Test
    public void existsByWrongEmailShouldReturnFalse() {
        assertFalse(userRepository.existsByEmail(WRONG_EMAIL));
    }

    @Test
    public void findByAdminEmailShouldReturnUser() {
        assertThat(userRepository.findByEmail(ADMIN_EMAIL).get().getEmail()).isEqualTo(ADMIN_EMAIL);
    }

    @Test
    public void findByAdminEmailShouldReturnOptionalEmpty() {
        assertThat(userRepository.findByEmail(WRONG_EMAIL)).isEqualTo(Optional.empty());
    }

    @Test
    public void findByExistingIdShouldReturnAdminEmail() {
        Optional<User> user = userRepository.findById(EXISTING_ID);
        String userEmail = user.isPresent() ? user.get().getEmail() : null;
        assertThat(userEmail).isEqualTo(ADMIN_EMAIL);
    }

    @Test
    public void findByExistingIdShouldThrowNotExistException() {
        assertThat(userRepository.findById(WRONG_ID)).isEqualTo(Optional.empty());
    }

    @Test
    public void existsByExistingIdShouldReturnTrue() {
        assertTrue(userRepository.existsById(EXISTING_ID));
    }

    @Test
    public void existsByWrongIdShouldReturnFalse() {
        assertFalse(userRepository.existsById(WRONG_ID));
    }

    @Test
    public void findAllShouldReturnListWithUserOnSecondPosition() {
        List<User> userList = userRepository.findAll();
        assertThat(userList).isNotEmpty();
        assertThat(userList.get(1).getEmail()).isEqualTo(USER_EMAIL);
    }

    @Test
    public void deleteByDeletedIdShouldDelete() {
        userRepository.deleteById(DELETED_ID);
        assertThat(userRepository.findById(DELETED_ID)).isEqualTo(Optional.empty());
    }
}
