package com.example.studentmanagementfa22.service;

import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.exception.UserAlreadyExistException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


public interface AccountService extends UserDetailsService {
    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    Account findAccountByUsername(String username);

    void registerNewAccount(Account account) throws UserAlreadyExistException;


}
