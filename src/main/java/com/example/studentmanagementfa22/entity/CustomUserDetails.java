package com.example.studentmanagementfa22.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class CustomUserDetails implements UserDetails {
    private Account account;

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(getRoleNameById(account.getRoleId())));
    }

    private String getRoleNameById(int roleId){
        return switch (roleId) {
            case 1 -> "ROLE_ADMIN";
            case 2 -> "ROLE_TEACHER";
            case 3 -> "ROLE_STUDENT";
            default -> null;
        };
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        return account.getUsername();
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
        return account.isEnabled();
    }
}
