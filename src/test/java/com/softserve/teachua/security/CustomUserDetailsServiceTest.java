package com.softserve.teachua.security;

import static com.softserve.teachua.TestUtils.getUser;
import static com.softserve.teachua.TestUtils.getUserPrincipal;
import com.softserve.teachua.model.User;
import com.softserve.teachua.repository.UserRepository;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private CustomUserDetailsService userDetailsService;
    private User user;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        user = getUser();
        userDetails = getUserPrincipal(user);
    }

    @Test
    void shouldFindUserAndReturnUserDetails_whenLoadUserByUsername() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        UserDetails actual = userDetailsService.loadUserByUsername(user.getEmail());

        assertAll("Should return appropriate UserDetails",
                () -> assertEquals(userDetails.getUsername(), actual.getUsername()),
                () -> assertEquals(userDetails.getPassword(), actual.getPassword()),
                () -> assertEquals(userDetails.getAuthorities(), actual.getAuthorities())
        );
    }

    @Test
    void shouldThrowUsernameNotFoundException_whenLoadUserByUsername() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(user.getEmail()))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    void shouldThrowResponseStatusException_whenGetUserPrincipal_whileSecurityContextException() {
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () ->
                userDetailsService.getUserPrincipal());

        assertThat(thrown.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
