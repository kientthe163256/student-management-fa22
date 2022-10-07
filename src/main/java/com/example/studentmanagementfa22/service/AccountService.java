package com.example.studentmanagementfa22.service;

import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.exception.ElementAlreadyExistException;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public interface AccountService extends UserDetailsService {
    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    Account findAccountByUsername(String username);

    void registerNewAccount(Account account) throws ElementAlreadyExistException;

    Account findById(int id);

    Page<Account> findAllAccount(int pageNumber);
}
