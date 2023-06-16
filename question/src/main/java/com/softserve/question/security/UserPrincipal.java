package com.softserve.question.security;

import com.softserve.commons.constant.RoleData;
import com.softserve.commons.exception.UserPermissionException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserPrincipal implements UserDetails {
    private final Long id;
    private final String username;
    private final List<? extends GrantedAuthority> authorities;
    private final RoleData role;

    public UserPrincipal(Long id, String username, RoleData authority) {
        this.id = id;
        this.username = username;
        this.role = authority;
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority(authority.getRoleName()));
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        throw new UserPermissionException();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public RoleData getRole() {
        return role;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "UserPrincipal{"
                + "username='" + username + '\''
                + ", authorities=" + authorities
                + '}';
    }
}
