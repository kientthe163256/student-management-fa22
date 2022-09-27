package com.example.studentmanagementfa22.service.impl;

import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.Role;
import com.example.studentmanagementfa22.exception.UserAlreadyExistException;
import com.example.studentmanagementfa22.repository.AccountRepository;
import com.example.studentmanagementfa22.service.AccountService;
import com.example.studentmanagementfa22.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) {
        Account optionalAccount = accountRepository.findByUsername(username);
        if (optionalAccount == null) {
            throw new UsernameNotFoundException("Username is not found!");
        }

        Account validAccount = optionalAccount;
        UserDetails userDetails = new User(validAccount.getUsername(),
                validAccount.getPassword(),
                mapRoleToAuthorities(validAccount.getRoleId()));
        return userDetails;
    }

    public Collection<? extends GrantedAuthority> mapRoleToAuthorities(Integer roleId) {
        Role role = roleService.findRoleById(roleId);
        Collection<String> roleList = new ArrayList<>();
        roleList.add(role.getRoleName());

        return roleList.stream()
                .map(roleName -> new SimpleGrantedAuthority(roleName))
                .collect(Collectors.toList());
    }


    @Override
    public Account findAccountByUsername(String username) {
        Account optionalAccount = accountRepository.findByUsername(username);
        if (optionalAccount == null) {
            throw new UsernameNotFoundException("Username is not found!");
        }
        return optionalAccount;
    }

    @Override
    public void registerNewAccount(Account account) throws UserAlreadyExistException {
        Account existAccount = accountRepository.findByUsername(account.getUsername());
        if (existAccount != null) {
            throw new UserAlreadyExistException("There is already an account with that username!");
        }
        account.setEnabled(true);
        account.setRoleId(roleService.findByRoleName("ROLE_STUDENT").getId());
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        accountRepository.save(account);
    }
}
