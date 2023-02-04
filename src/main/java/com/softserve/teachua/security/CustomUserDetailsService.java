package com.softserve.teachua.security;

import com.softserve.teachua.model.User;
import com.softserve.teachua.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {
    public static final String SECURITY_CONTEXT_ERROR =
            "Attempt to get authenticated user failed. Check Authentication";
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
        try {
            return ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        } catch (Exception e) {
            log.error(SECURITY_CONTEXT_ERROR, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, SECURITY_CONTEXT_ERROR);
        }
    }
}
