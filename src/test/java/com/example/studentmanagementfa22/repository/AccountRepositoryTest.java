package com.example.studentmanagementfa22.repository;

import com.example.studentmanagementfa22.entity.Account;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AccountRepositoryTest {
    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void saveAccount(){
//        accountRepository.save(account);
        Account optionalAccount = accountRepository.findByUsername("HE163256");
        Assertions.assertNotNull(optionalAccount);
    }

    @Test
    public void findAccountWithExistUsername(){
        Account account = accountRepository.findByUsername("HE163256");
        assertThat(account).isNotNull();
    }

    @Test
    public void findAccountWithUnknownUsername(){
        Account account = accountRepository.findByUsername("HE163257");
        assertThat(account).isNull();
    }

    @Test
    public void getAllAccount(){
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts).isNotNull();
    }
}
