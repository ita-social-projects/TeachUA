package com.softserve.teachua.security;

import static com.softserve.teachua.TestConstants.USER_EMAIL;
import static com.softserve.teachua.TestUtils.getUser;
import static com.softserve.teachua.TestUtils.getUserPrincipal;
import com.softserve.teachua.exception.UserAuthenticationException;
import com.softserve.teachua.model.User;
import com.softserve.teachua.repository.UserRepository;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(USER_EMAIL))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    void shouldReturnUserPrincipalFromSecurityContext_whenGetUserPrincipal() {
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null));

        UserPrincipal actual = userDetailsService.getUserPrincipal();
        assertAll("Should return appropriate UserDetails",
                () -> assertEquals(userDetails.getUsername(), actual.getUsername()),
                () -> assertEquals(userDetails.getPassword(), actual.getPassword()),
                () -> assertEquals(userDetails.getAuthorities(), actual.getAuthorities())
        );
    }

    @Test
    void shouldThrowUserAuthenticationException_whenGetUserPrincipal() {
        SecurityContextHolder.getContext().setAuthentication(null);

        assertThatThrownBy(() -> userDetailsService.getUserPrincipal())
                .isInstanceOf(UserAuthenticationException.class);
    }
}
