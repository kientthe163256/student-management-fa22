package com.example.studentmanagementfa22.service.impl;

import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.Role;
import com.example.studentmanagementfa22.repository.AccountRepository;
import com.example.studentmanagementfa22.service.AccountService;
import com.example.studentmanagementfa22.service.RoleService;
import com.example.studentmanagementfa22.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleService roleService;

    private Utility utility;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Account optionalAccount = accountRepository.findByUsername(username);
        if (optionalAccount == null){
            throw new UsernameNotFoundException("Username is not found!");
        }

        Account validAccount = optionalAccount;
        UserDetails userDetails = new User(validAccount.getUsername(),
                validAccount.getPassword(),
                utility.mapRoleToAuthorities(validAccount.getRole_id()));
        return userDetails;
    }


    @Override
    public Account findAccountByUsername(String username) {
        Account optionalAccount = accountRepository.findByUsername(username);
        if (optionalAccount==null){
            throw new UsernameNotFoundException("Username is not found!");
        }
        return optionalAccount;
    }
}
