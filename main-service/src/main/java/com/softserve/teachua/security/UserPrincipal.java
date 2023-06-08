package com.softserve.teachua.security;

import com.softserve.commons.constant.RoleData;
import com.softserve.commons.exception.UserPermissionException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserPrincipal implements /*OAuth2User, */UserDetails {
    private final String username;
    private final List<? extends GrantedAuthority> authorities;

    public UserPrincipal(String username, RoleData authority) {
        this.username = username;
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority(authority.getRoleName()));
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

    //@Override
    //public Map<String, Object> getAttributes() {
    //    return attributes;
    //}

    //@Override
    //public String getName() {
    //    return String.valueOf(id);
    //}
}
