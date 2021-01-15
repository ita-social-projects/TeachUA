package com.softserve.teachua.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.softserve.teachua.dto.security.UserEntity;
import com.softserve.teachua.service.UserService;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userService.getUserEntity(email);
        return CustomUserDetails.fromUserEntityToCustomUserDetails(userEntity);
    }
    
}
