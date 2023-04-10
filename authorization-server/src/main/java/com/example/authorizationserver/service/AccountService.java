package com.example.authorizationserver.service;


import com.example.authorizationserver.model.Account;
import com.example.authorizationserver.model.Role;
import com.example.authorizationserver.repository.AccountRepository;
import com.example.authorizationserver.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class AccountService implements UserDetailsService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Account account = accountRepository.getByUsername(s);
        if (account == null) {
            throw new UsernameNotFoundException("Username is not found!");
        }
        if (!account.isEnabled()){
            throw new DisabledException("Your account is disabled");
        }
        return new User(account.getUsername(),
                account.getPassword(),
                mapRoleToAuthorities(account.getRoleId()));
    }

    public Collection<? extends GrantedAuthority> mapRoleToAuthorities(Integer roleId) {
        Role role = roleRepository.findById(roleId).get();
        Collection<String> roleList = new ArrayList<>();
        roleList.add(role.getRoleName());

        return roleList.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
