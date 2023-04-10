package com.example.authorizationserver;

import com.example.authorizationserver.model.Account;
import com.example.authorizationserver.model.Role;
import com.example.authorizationserver.repository.AccountRepository;
import com.example.authorizationserver.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@RequiredArgsConstructor
public class AccountRepoTest {
    private AccountRepository accountRepository;
    private RoleRepository roleRepository;

    @Test
    public void getAccountByUsername(){
        Account account = accountRepository.getByUsername("AD000000");
        Assertions.assertNotNull(account);
    }

    @Test
    public void getRoleById(){
        Role role = roleRepository.findById(1).get();
        Assertions.assertNotNull(role);
    }
}
