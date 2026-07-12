package com.example.picklaundry_backend.security;

import com.example.picklaundry_backend.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final User user;

    // Return user role
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of(
                new SimpleGrantedAuthority(
                        "ROLE_" + user.getRole().name()
                )
        );
    }

    // Return encrypted password
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // Username used by Spring Security
    // We will use MOBILE
    @Override
    public String getUsername() {
        return user.getMobile();
    }

    // Account not expired
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // Account not locked
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // Credentials not expired
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Active account
    @Override
    public boolean isEnabled() {
        return user.getActive();
    }

    // Extra helper methods
    public Long getUserId() {
        return user.getId();
    }

    public User getUser() {
        return user;
    }
}