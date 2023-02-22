package com.softserve.teachua.security;

import com.softserve.teachua.exception.UserAuthenticationException;
import com.softserve.teachua.model.User;
import com.softserve.teachua.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {
    public static final String SECURITY_CONTEXT_ERROR =
            "There was attempt to get UserPrincipal, but Security Context is empty.";
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email : " + email));
        return UserPrincipal.create(user);
    }

    public UserPrincipal getUserPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return (UserPrincipal) authentication.getPrincipal();
        }
        log.error(SECURITY_CONTEXT_ERROR);
        throw new UserAuthenticationException();
    }
}
