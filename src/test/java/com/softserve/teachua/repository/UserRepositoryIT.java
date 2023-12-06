package com.softserve.teachua.repository;

import com.softserve.teachua.model.User;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@Slf4j
@DataJpaTest
class UserRepositoryIT {
    private static final String ADMIN_EMAIL = "admin@gmail.com";
    private static final String WRONG_EMAIL = "wrong@gmail.com";
    private static final String USER_EMAIL = "user@gmail.com";
    private static final Long EXISTING_ID = 1L;
    private static final Long WRONG_ID = 100L;
    private static final Long DELETED_ID = 199L;
    @Autowired
    private UserRepository userRepository;

    @Test
    void existsByAdminEmailShouldReturnTrue() {
        assertTrue(userRepository.existsByEmail(ADMIN_EMAIL));
    }

    @Test
    void existsByWrongEmailShouldReturnFalse() {
        assertFalse(userRepository.existsByEmail(WRONG_EMAIL));
    }

    @Test
    void findByAdminEmailShouldReturnUser() {
        assertThat(userRepository.findByEmail(ADMIN_EMAIL).orElseThrow().getEmail()).isEqualTo(ADMIN_EMAIL);
    }

    @Test
    void findByAdminEmailShouldReturnOptionalEmpty() {
        assertThat(userRepository.findByEmail(WRONG_EMAIL)).isEmpty();
    }

    @Test
    void findByExistingIdShouldReturnAdminEmail() {
        Optional<User> user = userRepository.findById(EXISTING_ID);
        String userEmail = user.map(User::getEmail).orElse(null);
        assertThat(userEmail).isEqualTo(ADMIN_EMAIL);
    }

    @Test
    void findByExistingIdShouldThrowNotExistException() {
        assertThat(userRepository.findById(WRONG_ID)).isEmpty();
    }

    @Test
    void existsByExistingIdShouldReturnTrue() {
        assertTrue(userRepository.existsById(EXISTING_ID));
    }

    @Test
    void existsByWrongIdShouldReturnFalse() {
        assertFalse(userRepository.existsById(WRONG_ID));
    }

    @Test
    void findAllShouldReturnListWithUserOnSecondPosition() {
        List<User> userList = userRepository.findAll();
        assertThat(userList).isNotEmpty();
        assertThat(userList.get(1).getEmail()).isEqualTo(USER_EMAIL);
    }

    @Test
    void deleteByDeletedIdShouldDelete() {
        userRepository.deleteById(DELETED_ID);
        assertThat(userRepository.findById(DELETED_ID)).isEmpty();
    }
}
