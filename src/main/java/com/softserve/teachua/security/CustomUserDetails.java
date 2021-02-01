package com.softserve.teachua.security;

import com.softserve.teachua.dto.security.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class CustomUserDetails implements UserDetails {

    private static final long serialVersionUID = 1L;

    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> grantedAuthorities;
    // TODO
    private Date expirationDate;

    private CustomUserDetails() {
    }

    public static CustomUserDetails fromUserEntityToCustomUserDetails(UserEntity userEntity) {
        CustomUserDetails customUserDetails = new CustomUserDetails();
        customUserDetails.email = userEntity.getEmail();
        customUserDetails.password = userEntity.getPassword();
        customUserDetails.grantedAuthorities = Collections
                .singletonList(new SimpleGrantedAuthority(userEntity.getRoleName()));
        // TODO add expirationDate
        return customUserDetails;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO
        return true;
    }

    @Override
    public boolean isEnabled() {
        // TODO
        return true;
    }

    // TODO  Remove/delete
    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }
}
